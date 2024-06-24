package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    // return list of categories to controller
    @Override
    public List<Category> getAllCategories()
    {
        List<Category> allCategories = new ArrayList<>();
        try(Connection connection = getConnection())
        {
            String sql = """
                    SELECT category_id
                        , name
                        , description
                    FROM categories;
                    """;

            Statement statement = connection.createStatement();
            ResultSet row = statement.executeQuery(sql);

            while(row.next())
            {
                Category category = mapRow(row);
                allCategories.add(category);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return allCategories;
    }

    // return a category object by id
    @Override
    public Category getById(int categoryId)
    {
        Category category = null;
        try(Connection connection = getConnection())
        {
            String sql = """
                    SELECT category_id
                        , name
                        , description
                    FROM categories
                    WHERE category_id = ?;
                    """;

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            ResultSet row = preparedStatement.executeQuery();

            if(row.next())
            {
                category = mapRow(row);
                return category;
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    // returning the object inserted in the database
    @Override
    public Category create(Category category)
    {
        int newId = 0;
        try(Connection connection = getConnection())
        {
            String sql = """
                    INSERT INTO categories
                    (name, description)
                    VALUES
                    (?, ?);
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.executeUpdate();

            ResultSet row = preparedStatement.getGeneratedKeys();
            row.next();

            newId = row.getInt(1);

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return getById(newId);
    }

    // searching for category by id and updating it
    @Override
    public void update(int categoryId, Category category)
    {
        // update category
        try(Connection connection = getConnection())
        {
            String sql = """
                    UPDATE categories
                    SET name = ?
                        , description = ?
                    WHERE category_id = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, categoryId);

            preparedStatement.executeUpdate();

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    // searching for category by id and deleting it
    @Override
    public void delete(int categoryId)
    {
        // delete category
        try(Connection connection = getConnection())
        {
            String sql = """
                    DELETE FROM categories
                    WHERE category_id = ?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
