package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.dto.CartItemRequest;
import com.example.ecommercebackend.models.CartItem;
import com.example.ecommercebackend.models.User;
import com.example.ecommercebackend.services.CartService;
import com.example.ecommercebackend.repositories.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private AuthRepository authRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long userId) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<CartItem> cartItems = cartService.getCartItems(user.getId());
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestBody CartItemRequest cartItemRequest) {
        cartService.addItemToCart(cartItemRequest);
        return ResponseEntity.ok("Item added to cart");
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam Long userId) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartService.clearCart(user.getId());
        return ResponseEntity.ok("Cart cleared successfully.");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeItemFromCart(@RequestBody CartItemRequest cartItemRequest) {

        cartService.removeItemFromCart(cartItemRequest.getUserId(), cartItemRequest.getProductId(), cartItemRequest.getQuantity());
        return ResponseEntity.ok("Item removed from cart");
    }
}
