package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    // Add this method to find a user by ID
    Optional<User> findById(Long id);
}
