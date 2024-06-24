package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    ShoppingCart shoppingCart = new ShoppingCart();
    ProductDao productDao;

    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {

        String sql = """
                SELECT *
                FROM shopping_cart
                WHERE user_id = ?;
                """;

        try(Connection connection = getConnection())
        {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet row = preparedStatement.executeQuery();

            while(row.next())
            {
                ShoppingCartItem shoppingCartItem = mapToItem(row);
                shoppingCart.add(shoppingCartItem);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return shoppingCart;
    }

    @Override
    public ShoppingCartItem addItem(int userId, int productId) {

        boolean isInShoppingCart = isInShoppingCart(userId, productId);

        if(isInShoppingCart)
        {
            String sql = """
                    UPDATE shopping_cart
                    SET quantity = ?
                    WHERE user_id = ?
                    AND product_Id = ?
                    """;

            try(Connection connection = getConnection())
            {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, +1);
                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, productId);

                preparedStatement.executeUpdate();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        else {
            String sql = """
                    INSERT INTO shopping_cart
                    (user_id, product_id, quantity)
                    VALUES
                    (?, ?, ?);
                    """;

            try (Connection connection = getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, productId);
                preparedStatement.setInt(3, 1);

                preparedStatement.executeUpdate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public Boolean isInShoppingCart(int userId, int productId)
    {
        boolean isInShoppingCart = false;
        String sql = """
                SELECT *
                FROM shopping_cart
                WHERE user_id = ?
                AND product_id = ?;
                """;

        try(Connection connection = getConnection())
        {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, productId);

            ResultSet row = preparedStatement.executeQuery();

            isInShoppingCart = row.next();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return isInShoppingCart;
    }


    public ShoppingCartItem mapToItem(ResultSet row) throws SQLException {
        int productId = row.getInt("product_id");
        int quantity = row.getInt("quantity");

        Product product = productDao.getById(productId);

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem()
        {{
           setProduct(product);
           setQuantity(quantity);
        }};

        return shoppingCartItem;
    }
}
