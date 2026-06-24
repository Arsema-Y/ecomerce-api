package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();
        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);

        for (CartItem cartItem : cartItems) {
            Product product = productService.getById(cartItem.getProductId());
            ShoppingCartItem cartItem1 = new ShoppingCartItem();
            cartItem1.setProduct(product);
            cartItem1.setQuantity(cartItem.getQuantity());
            cart.add(cartItem1);
        }

        return cart;
    }

    public ShoppingCart addProduct(int userId, int productId) {

        CartItem itemInCart = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if (itemInCart == null) {
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(1);
            shoppingCartRepository.save(newItem);

        } else {
            itemInCart.setQuantity(itemInCart.getQuantity() + 1);
            shoppingCartRepository.save(itemInCart);

        }

        return getByUserId(userId);
    }

    public ShoppingCart updateProduct(int userId, int productId, ShoppingCartItem update) {
        CartItem existing = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if (existing != null) {
            existing.setQuantity(update.getQuantity());
            shoppingCartRepository.save(existing);
        }

        return getByUserId(userId);
    }

    public ShoppingCart emptyCart(int userId) {
        shoppingCartRepository.deleteByUserId(userId);
        return getByUserId(userId);
    }

}


