package com.example.restaurantservice.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.restaurantservice.entity.MenuCategory;
import com.example.restaurantservice.entity.MenuItem;
import com.example.restaurantservice.entity.Restaurant;
import com.example.restaurantservice.service.RestaurantService;

// @RestController - DISABLED to avoid mapping conflicts
// @RequestMapping("/api/restaurants") - DISABLED to avoid mapping conflicts
// @CrossOrigin(origins = "*") - DISABLED to avoid mapping conflicts
public class RestaurantController {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        try {
            List<Restaurant> restaurants = restaurantService.getAllActiveRestaurants();
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            logger.error("Error fetching restaurants: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            return ResponseEntity.ok(restaurant);
        } catch (RuntimeException e) {
            logger.error("Error fetching restaurant {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error fetching restaurant {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<Map<String, Object>> getRestaurantMenu(@PathVariable Long id) {
        try {
            Map<String, Object> menu = restaurantService.getRestaurantMenu(id);
            return ResponseEntity.ok(menu);
        } catch (RuntimeException e) {
            logger.error("Error fetching menu for restaurant {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error fetching menu for restaurant {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/menu-items")
    public ResponseEntity<List<MenuItem>> getRestaurantMenuItems(@PathVariable Long id) {
        try {
            List<MenuItem> menuItems = restaurantService.getMenuItemsByRestaurant(id);
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            logger.error("Error fetching menu items for restaurant {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<List<MenuCategory>> getRestaurantCategories(@PathVariable Long id) {
        try {
            List<MenuCategory> categories = restaurantService.getRestaurantCategories(id);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Error fetching categories for restaurant {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/categories/{categoryId}/menu-items")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(
            @PathVariable Long id, 
            @PathVariable Long categoryId) {
        try {
            List<MenuItem> menuItems = restaurantService.getMenuItemsByCategory(id, categoryId);
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            logger.error("Error fetching menu items for category {} in restaurant {}: {}", categoryId, id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(@RequestParam String cuisine) {
        try {
            List<Restaurant> restaurants = restaurantService.searchRestaurantsByCuisine(cuisine);
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            logger.error("Error searching restaurants by cuisine {}: {}", cuisine, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
