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
        User user = authRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return new LoginResponseDTO(user.getUsername(), user.getEmail());
        }
        return null;
    }

}
