package com.example.ecommercebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000") // Allow requests from your frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "Accept", "Origin") // Add more headers if needed
                .exposedHeaders("Authorization") // Expose headers to the frontend, if needed
                .allowCredentials(true) // Allow credentials (cookies, etc.)
                .maxAge(1800); // Cache the preflight response for 30 minutes
    }
}
