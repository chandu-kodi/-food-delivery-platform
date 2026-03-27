package com.example.restaurantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RestaurantServiceApplication {
    public static void main(String[] args) {
        System.out.println("Starting Restaurant Service...");
        try {
            ConfigurableApplicationContext context = SpringApplication.run(RestaurantServiceApplication.class, args);
            System.out.println("Restaurant Service started successfully!");
            System.out.println("Active profiles: " + String.join(", ", context.getEnvironment().getActiveProfiles()));
        } catch (Exception e) {
            System.err.println("Failed to start Restaurant Service: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
