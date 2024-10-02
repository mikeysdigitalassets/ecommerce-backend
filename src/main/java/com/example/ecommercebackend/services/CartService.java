package com.example.ecommercebackend.services;

import com.example.ecommercebackend.dto.CartItemRequest;
import com.example.ecommercebackend.models.CartItem;
import com.example.ecommercebackend.models.Product;
import com.example.ecommercebackend.models.User;
import com.example.ecommercebackend.repositories.AuthRepository;
import com.example.ecommercebackend.repositories.CartRepository;
import com.example.ecommercebackend.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
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

    // Add item to cart
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
        cartItem.setProductId(cartItemRequest.getProductId()); // Set the Product entity, not just the productId
        cartItem.setQuantity(cartItemRequest.getQuantity()); // Use correct quantity field
        cartItem.setPrice(cartItemRequest.getPrice());
        cartItem.setName(cartItemRequest.getName());

        // Save the cart item to the database
        cartRepository.save(cartItem);
    }



    // Get all cart items for a user
    public List<CartItem> getCartItems(Long userId) {
        // Find the user by userId
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Retrieve cart items for the user
        return cartRepository.findByUser(user);
    }

    // Clear all cart items for a user
    public void clearCart(Long userId) {
        // Find the user by userId
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete cart items for the user
        cartRepository.deleteByUser(user);
    }

    // Remove a specific item from the cart
    public void removeItemFromCart(Long userId, Long productId, int quantityToRemove) {
        // Find the user by userId
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Find the cart item by userId and productId
        CartItem cartItem = cartRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found in cart for productId: " + productId));

        int updatedQuantity = cartItem.getQuantity() - quantityToRemove;

        if (updatedQuantity > 0) {
            // Update the quantity of the item in the cart
            cartItem.setQuantity(updatedQuantity);
            cartRepository.save(cartItem);
        } else {
            // Remove the item from the cart if quantity is zero or less
            cartRepository.delete(cartItem);
        }
    }

}
