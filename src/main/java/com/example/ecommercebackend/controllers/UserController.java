package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.dto.ShippingAddressDTO;
import com.example.ecommercebackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/user-settings")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add-shipping")
    public ResponseEntity<String> saveShippingAddress(@RequestBody ShippingAddressDTO shippingAddressDTO) {
        userService.saveShippingAddress(shippingAddressDTO);
        return ResponseEntity.ok("Shipping address saved successfully");
    }
}
