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


    public void addItemToCart(CartItemRequest cartItemRequest) {
        CartItem cartItem = new CartItem();


        User user = authRepository.findById(cartItemRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));


        cartItem.setUser(user);
        cartItem.setProductId(cartItemRequest.getProductId()); // set the Product entity, not just the productId
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItem.setPrice(cartItemRequest.getPrice());
        cartItem.setName(cartItemRequest.getName());


        cartRepository.save(cartItem);
    }




    public List<CartItem> getCartItems(Long userId) {

        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        return cartRepository.findByUser(user);
    }


    public void clearCart(Long userId) {

        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        cartRepository.deleteByUser(user);
    }


    public void removeItemFromCart(Long userId, Long productId, int quantityToRemove) {

        User user = authRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));


        CartItem cartItem = cartRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found in cart for productId: " + productId));

        int updatedQuantity = cartItem.getQuantity() - quantityToRemove;

        if (updatedQuantity > 0) {

            cartItem.setQuantity(updatedQuantity);
            cartRepository.save(cartItem);
        } else {

            cartRepository.delete(cartItem);
        }
    }

}
