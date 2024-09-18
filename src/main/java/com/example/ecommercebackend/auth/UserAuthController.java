package com.example.ecommercebackend.auth;

import com.example.ecommercebackend.dto.LoginResponseDTO;
import com.example.ecommercebackend.dto.UserDTO;
import com.example.ecommercebackend.models.User;
import com.example.ecommercebackend.services.AuthService;
import com.example.ecommercebackend.repositories.AuthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class UserAuthController {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthRepository authRepository;  // Inject AuthRepository

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        String errorMessage = "";

        if (authRepository.existsByUsername(userDTO.getUsername())) {
            errorMessage = "Username is already taken";
        }

        if (authRepository.existsByEmail(userDTO.getEmail())) {
            if (!errorMessage.isEmpty()) {
                errorMessage += " and Email is already in use";
            } else {
                errorMessage = "Email is already in use";
            }
        }

        if (!errorMessage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        // If no issues, proceed with registration logic
        authService.registerUser(userDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody UserDTO userDTO) {
        LoginResponseDTO user = authService.loginUser(userDTO.getEmail(), userDTO.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).body(null);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User user = authService.getUser(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        authService.deleteUser(username);
        return ResponseEntity.ok("User deleted successfully");
    }
}
