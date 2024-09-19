package com.example.ecommercebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Use a method to define CORS configuration
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**", "/api/products/**").permitAll() // Allow public access to these endpoints
                        .anyRequest().authenticated() // All other requests need authentication
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        .defaultSuccessUrl("/api/auth/success", true) // Redirect on successful login
                        .failureUrl("/api/auth/login?error=true") // Redirect on login failure
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/") // Redirect to a public page after logout
                )
                .sessionManagement(session -> session
                        .maximumSessions(1) // Limit to one session per user
                        .expiredUrl("/api/auth/login") // Redirect to login on session expiration
                )
                .sessionManagement(session -> session
                        .sessionFixation(sessionFixation -> sessionFixation.migrateSession()) // Migrating session, ensuring new session
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Create a PasswordEncoder bean using BCrypt
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000"); // Allow requests from your frontend
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow cookies/credentials
        configuration.addExposedHeader("Authorization"); // Expose the Authorization header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration); // Apply CORS configuration to the API endpoints
        return source;
    }
}
