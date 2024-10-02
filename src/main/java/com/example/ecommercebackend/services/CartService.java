package com.example.ecommercebackend.services;

import com.example.ecommercebackend.dto.CartItemRequest;
import com.example.ecommercebackend.models.CartItem;
import com.example.ecommercebackend.models.Product;
import com.example.ecommercebackend.models.User;
import com.example.ecommercebackend.repositories.AuthRepository;
import com.example.ecommercebackend.repositories.CartRepository;
import com.example.ecommercebackend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthRepository authRepository;

    public void addItemToCart(CartItemRequest cartItemRequest) {
        CartItem cartItem = new CartItem();

        // Find the user by userId
        User user = authRepository.findById(cartItemRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the product by productId
        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Set fields on cartItem
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItem.setPrice(cartItemRequest.getPrice());
        cartItem.setName(cartItemRequest.getName());

        // Save the cart item to the database
        cartRepository.save(cartItem);
    }

    public List<CartItem> getCartItems(Long userId) {
        // Find the user by userId
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Retrieve cart items for the user
        return cartRepository.findByUser(user);
    }

    public void clearCart(Long userId) {
        // Find the user by userId
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete cart items for the user
        cartRepository.deleteByUser(user);
    }

    public void removeItemFromCart(Long userId, Long productId, int quantityToRemove) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartItem cartItem = cartRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new RuntimeException("item not found in cart"));

        int updatedQuantity = cartItem.getQuantity() - quantityToRemove;

        if (updatedQuantity > 0 ) {
            cartItem.setQuantity(updatedQuantity);
            cartRepository.save(cartItem);
        } else {
            cartRepository.delete(cartItem);
        }


    }
}
