package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.ShoppingCart;

public interface OrderDao {
    ShoppingCart addOrder(int userId);

    void addOrderLineItem(int userId, ShoppingCart shoppingCart);

    Order getOrderByUserId(int userId);
}
