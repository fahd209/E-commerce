package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;


    // Autowired controller to inject the categoryDao and ProductDao

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao)
    {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    // getting all categorys from dao
    @GetMapping("")
    public List<Category> getAll()
    {
        // find and return all categories
        try {
            return categoryDao.getAllCategories();
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops... our bad.");
        }
    }

    // getting categories by id
    @GetMapping("{id}")
    public Category getById(@PathVariable int id)
    {
        // get the category by id
        try {
            return categoryDao.getById(id);
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops... our bad.");
        }
    }

    // the url to return all products in category 1 would look like this
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        try {
            return productDao.listByCategoryId(categoryId);
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops... our bad.");
        }
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category)
    {
        // insert the category
        try {
            return categoryDao.create(category);
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops...our bad");
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        // update the category by id
        try {
            categoryDao.update(id, category);
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops...our bad");
        }
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int id)
    {
        // delete the category by id
        try {
            categoryDao.delete(id);
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Oops...our bad");
        }
    }
}
