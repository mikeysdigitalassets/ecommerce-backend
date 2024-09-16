package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository provides all basic CRUD operations like findAll, save, deleteById, etc.
}
