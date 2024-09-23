package com.example.ecommercebackend.auth;

import com.example.ecommercebackend.dto.LoginResponseDTO;
import com.example.ecommercebackend.dto.UserDTO;
import com.example.ecommercebackend.models.User;
import com.example.ecommercebackend.services.AuthService;
import com.example.ecommercebackend.repositories.AuthRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/current-user")
    public ResponseEntity<LoginResponseDTO> currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            LoginResponseDTO currentUser = (LoginResponseDTO) session.getAttribute("user");
            if (currentUser != null) {
                return ResponseEntity.ok(currentUser);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidate the session
        }
        Cookie cookie = new Cookie("JSESSIONID", null); // Create a cookie with the same name
        cookie.setPath("/"); // Path must match the original path of the cookie
        cookie.setDomain("localhost"); // Optional: Set if domain was specified when creating the cookie
        cookie.setMaxAge(0); // Set the cookie's max age to 0 to delete it
        cookie.setHttpOnly(true); // Optional: Match the HttpOnly attribute if it was set
        response.addCookie(cookie); // Add the cookie to the response

        System.out.println("Cookie deleted");

        return ResponseEntity.ok("Logged out successfully");

    }
}
