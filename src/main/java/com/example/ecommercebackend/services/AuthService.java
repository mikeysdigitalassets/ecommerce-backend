package com.example.ecommercebackend.services;



import com.example.ecommercebackend.dto.LoginResponseDTO;
import com.example.ecommercebackend.dto.UserDTO;
import com.example.ecommercebackend.models.User;
import com.example.ecommercebackend.repositories.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public void registerUser(UserDTO userDTO) {

        String errorMessage = "";

        if (authRepository.existsByUsername(userDTO.getUsername())) {
            errorMessage = "Username is already in use";
        }

        if (authRepository.existsByEmail(userDTO.getEmail())) {
            if (!errorMessage.isEmpty()) {
                errorMessage += " and Email is already in use";
            } else {
                errorMessage = "Email is already in use";
            }
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());

        authRepository.save(user);
    }

    public User getUser(String username) {
        return authRepository.findByUsername(username);
    }

    public boolean isUsernameTaken(String username) {
        return authRepository.existsByUsername(username);
    }

    public void deleteUser(String username) {
        User user = authRepository.findByUsername(username);
        if (user != null) {
            authRepository.delete(user);
        }
    }

    public void updateUser(UserDTO userDTO) {
        User user = authRepository.findByUsername(userDTO.getUsername());
        if (user != null) {
            user.setEmail(userDTO.getEmail());
            if (userDTO.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            authRepository.save(user);
        }
    }

    public LoginResponseDTO loginUser(String email, String password) {
        // Fetch user from repository
        Optional<User> userOptional = authRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if password matches
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Return LoginResponseDTO with id, username, and email
                return new LoginResponseDTO(user.getId(), user.getUsername(), user.getEmail());
            } else {
                // Log or return an error message for incorrect password
                System.out.println("Incorrect password for user: " + email);
            }
        } else {
            // Log or return an error message for user not found
            System.out.println("User not found with email: " + email);
        }

        // Return null or throw an exception with a message if user is not found or password is incorrect
        return null;
    }



}
