package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.models.CartItem;
import com.example.ecommercebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    void deleteByUser(User user);

    // Find the cart item by user ID and product ID
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

    // Delete the cart item by user ID and product ID
    void deleteByUserIdAndProductId(Long userId, Long productId);
}

