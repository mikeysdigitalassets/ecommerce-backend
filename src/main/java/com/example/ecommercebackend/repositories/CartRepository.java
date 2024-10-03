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

    // finds cart item by userId and productId, using this for functionality of cart
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

    // method for deleting individual cart item when pressing remove from cart
    void deleteByUserIdAndProductId(Long userId, Long productId);


}

