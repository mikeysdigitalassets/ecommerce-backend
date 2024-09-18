package com.example.ecommercebackend.dto;

public class LoginResponseDTO {
    private String username;
    private String email;

    public LoginResponseDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
