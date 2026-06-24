package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("cart")
@PreAuthorize("isAuthenticated()")
@CrossOrigin
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ShoppingCart> getCart(Principal principal) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        return ResponseEntity.ok(shoppingCartService.getByUserId(userId));
    }

    // https://localhost:8080/cart/products/15  (15 is the productId to be added)
    // return the updated cart with status 201 Created
    @PostMapping("products/{productId}")
    public ResponseEntity<ShoppingCart> addProduct(Principal principal,
                                                  @PathVariable int productId) {
        int userId = userService.getByUserName(principal.getName()).getId();
        return ResponseEntity.status(201).body(shoppingCartService.addProduct(userId, productId));
    }

    // https://localhost:8080/cart/products/15  (15 is the productId to be updated)
    @PutMapping("products/{productId}")
    public ShoppingCart updateProduct(Principal principal,
                                      @PathVariable int productId,
                                      @RequestBody ShoppingCartItem item) {
        int userId = userService.getByUserName(principal.getName()).getId();
        return shoppingCartService.updateProduct(userId, productId, item);
    }

    // https://localhost:8080/cart  - return the (now empty) cart so the front end can refresh it (200 OK)
    @DeleteMapping
    public ResponseEntity<ShoppingCart> emptyCart(Principal principal) {
        int userId = userService.getByUserName(principal.getName()).getId();
        return ResponseEntity.ok(shoppingCartService.emptyCart(userId));
    }


}


