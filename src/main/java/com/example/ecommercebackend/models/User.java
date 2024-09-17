package com.example.ecommercebackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "userauth")
public class User {
    @Id
    private String username; // or another unique identifier like Long id
    private String password;
    // Other fields like email, address, etc.

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getters and setters
}
