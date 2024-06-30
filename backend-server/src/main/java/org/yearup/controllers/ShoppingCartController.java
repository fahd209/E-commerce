package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import javax.annotation.security.DeclareRoles;
import java.security.Principal;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao)
    {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }


    // each method in this controller requires a Principal object as a parameter
    @GetMapping("/cart")
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            // getting username from current user
            String userName = principal.getName();

            // getting user by name then getting the user id
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops... our bad.");
        }
    }

    // post method to add a product to the shopping cart with the product id
    @PostMapping("/cart/products/{productId}")
    public ShoppingCart addItem(@PathVariable int productId, Principal principal)
    {
        try {
            // getting current username
            String userName = principal.getName();

            // getting use from the usedao by username
            User user = userDao.getByUserName(userName);

            // getting user id
            int userId = user.getId();

            // adding an item with user id and product id
            return shoppingCartDao.addItem(userId , productId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops... our bad.");
        }
    }

    // Put mapping for the shopping cart with a specific item id
    @PutMapping("/cart/products/{itemId}")
    public ShoppingCart updateCartItem(@PathVariable int itemId, @RequestBody ShoppingCartItem shoppingCartItem, Principal principal)
    {
        try
        {
            String userName = principal.getName();

            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.updateShoppingCartItem(itemId, shoppingCartItem, userId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops... our bad.");
        }
    }

    // delete the cart of the current user
    @DeleteMapping("/cart")
    public ShoppingCart clearCart(Principal principal)
    {
        try
        {
            String userName = principal.getName();

            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.clearCart(userId);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops...our bad");
        }
    }

}
