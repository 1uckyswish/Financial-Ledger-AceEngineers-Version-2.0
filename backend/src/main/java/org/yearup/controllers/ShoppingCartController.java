package org.yearup.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import java.security.Principal;
/*
@CrossOrigin(origins = "http://localhost:8080")
@RestController

public class ShoppingCartController {
    //@Autowired
    private final ShoppingCartDao shoppingCartDao;
    //@Autowired
    private final UserDao userDao;
    //@Autowired
    private final ProductDao productDao;
   // @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;


    }
    @GetMapping ("/shopping_cart")
    //^ I FOUND THE PATHING ERROR!!!!!
    public ShoppingCart getCart(Principal principal) {
        try {
            if (principal != null) {
                String userName = principal.getName();
                User user = userDao.getByUserName(userName);
                int userId = user.getId();
                return shoppingCartDao.getByUserId(userId);
            }
        } catch (Exception e) {
            System.out.println("Ruh-Roh");
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... you're bad at this >=)");
        }
        return null;
    }
    @PostMapping({"https://localhost:8080/cart/products/", "shopping_cart/products/?"})
    public ShoppingCart addProductToCart(@PathVariable int productId, Principal principal)
    {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);

            if(cart == null) {
                cart = new ShoppingCart();

            } if(cart.contains(productId)) {
                ShoppingCartItem existingItem = cart.get(productId);
                existingItem.setQuantity(existingItem.getQuantity() + 1);

            } else {
                ShoppingCartItem newItem = new ShoppingCartItem();
                newItem.setProduct(productDao.getById(productId));
                newItem.setQuantity(1);
                cart.add(newItem);
            }
            return shoppingCartDao.addProductToCart(userId, productId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... your bad >=).");
        }
    }
    @PutMapping("shopping_cart/products/{productId}")
    public ShoppingCart updateProductInCart(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal) {
        try {
            if (principal == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "BEGONE! GET THEE BEHIND ME SATAN!");
            }
            try {
                String userName = principal.getName();
                User user = userDao.getByUserName(userName);
                int userId = user.getId();
                ShoppingCart cart = shoppingCartDao.getByUserId(userId);
                if (cart.contains(productId)) {
                    ShoppingCartItem existingItem = cart.get(productId);
                    existingItem.setQuantity(item.getQuantity());
                } else {
                    item.setProduct(productDao.getById(productId));
                    cart.add(item);
                }
                return shoppingCartDao.addProductToCart(userId, productId);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... your bad.");
            }
        } catch (ResponseStatusException e) {
            throw new RuntimeException(e);
        }
    }
    @DeleteMapping("/shopping_cart")
    public void clearCart(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't exist.");
        }
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            cart.getItems().clear();
            shoppingCartDao.clearCart(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
} */