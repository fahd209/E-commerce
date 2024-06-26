package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Order;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class OrdersController {
    UserDao userDao;
    OrderDao orderDao;

    @Autowired
    public OrdersController(OrderDao orderDao, UserDao userDao)
    {
        this.orderDao = orderDao;
        this.userDao = userDao;
    }

    @PostMapping("/orders")
    public Order addOrder(Principal principal)
    {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return orderDao.addOrder(userId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops...our bad");
        }
    }
}
