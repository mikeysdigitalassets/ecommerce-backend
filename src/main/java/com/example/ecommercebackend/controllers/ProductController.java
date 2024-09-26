package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.models.Product;
import com.example.ecommercebackend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/products")
public class ProductController {


    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        logger.info("Received request to get all products.");
        List<Product> products = productService.getAllProducts();
        logger.info("Returning {} products.", products.size());
        return products;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logger.info("Received request to get product with ID: {}", id);
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            logger.info("Product found: {}", product.get());
            return ResponseEntity.ok(product.get());
        } else {
            logger.warn("Product with ID: {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        logger.info("Received request to add a new product: {}", product);
        Product createdProduct = productService.addProduct(product);
        logger.info("Product added successfully: {}", createdProduct);
        return createdProduct;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        logger.info("Received request to update product with ID: {}", id);
        Optional<Product> updatedProduct = productService.updateProduct(id, productDetails);
        if (updatedProduct.isPresent()) {
            logger.info("Product updated successfully: {}", updatedProduct.get());
            return ResponseEntity.ok(updatedProduct.get());
        } else {
            logger.warn("Product with ID: {} not found for update.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Received request to delete product with ID: {}", id);
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            logger.info("Product with ID: {} deleted successfully.", id);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("Product with ID: {} not found for deletion.", id);
            return ResponseEntity.notFound().build();
        }
    }
}
