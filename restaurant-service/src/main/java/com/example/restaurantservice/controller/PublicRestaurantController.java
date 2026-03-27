package com.example.restaurantservice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restaurantservice.entity.MenuCategory;
import com.example.restaurantservice.entity.MenuItem;
import com.example.restaurantservice.entity.Restaurant;
import com.example.restaurantservice.service.AdminRestaurantService;
import com.example.restaurantservice.service.RestaurantService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PublicRestaurantController {
    private static final Logger logger = LoggerFactory.getLogger(PublicRestaurantController.class);
    private final AdminRestaurantService adminRestaurantService;
    private final RestaurantService restaurantService;

    public PublicRestaurantController(AdminRestaurantService adminRestaurantService, RestaurantService restaurantService) {
        this.adminRestaurantService = adminRestaurantService;
        this.restaurantService = restaurantService;
    }

    // Public restaurant endpoints
    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        try {
            logger.info("Getting all restaurants for public API");
            List<Restaurant> restaurants = restaurantService.getAllActiveRestaurants();
            logger.info("Found {} active restaurants", restaurants.size());
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            logger.error("Error fetching restaurants: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        try {
            logger.info("Getting restaurant {} for public API", id);
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            if (restaurant.getIsActive() == null || !restaurant.getIsActive()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(restaurant);
        } catch (RuntimeException e) {
            logger.error("Restaurant {} not found: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error fetching restaurant {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/restaurants/{id}/menu")
    public ResponseEntity<Map<String, Object>> getRestaurantMenu(@PathVariable Long id) {
        try {
            logger.info("Getting menu for restaurant {}", id);
            
            // Verify restaurant exists and is active
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            if (restaurant.getIsActive() == null || !restaurant.getIsActive()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Get categories and menu items (only available items for public API)
            List<MenuCategory> categories = restaurantService.getRestaurantCategories(id);
            List<MenuItem> menuItems = restaurantService.getMenuItemsByRestaurant(id);

            logger.info("Found {} categories and {} available menu items for restaurant {}", 
                    categories.size(), menuItems.size(), id);

            // Log all menu items for debugging
            for (MenuItem item : menuItems) {
                logger.info("Menu Item: ID={}, Name={}, CategoryID={}, Available={}", 
                    item.getId(), item.getName(), 
                    item.getCategory() != null ? item.getCategory().getId() : "null", 
                    item.getIsAvailable());
            }

            // Group menu items by category
            Map<Long, List<MenuItem>> itemsByCategory = new HashMap<>();
            for (MenuItem item : menuItems) {
                if (item.getCategory() != null) {
                    Long categoryId = item.getCategory().getId();
                    itemsByCategory.computeIfAbsent(categoryId, k -> new ArrayList<>()).add(item);
                    logger.info("Grouped item '{}' under category {}", item.getName(), categoryId);
                } else {
                    logger.warn("Menu item '{}' has no category", item.getName());
                }
            }

            // Log the final grouping
            logger.info("Final itemsByCategory: {}", itemsByCategory.keySet());

            Map<String, Object> menu = new HashMap<>();
            menu.put("restaurant", restaurant);
            menu.put("categories", categories);
            menu.put("menuItems", menuItems);
            menu.put("itemsByCategory", itemsByCategory);
            
            return ResponseEntity.ok(menu);
        } catch (RuntimeException e) {
            logger.error("Restaurant {} not found: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error fetching menu for restaurant {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/restaurants/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(@RequestParam(required = false) String cuisine) {
        try {
            logger.info("Searching restaurants with cuisine: {}", cuisine);
            List<Restaurant> restaurants;
            
            if (cuisine == null || cuisine.isEmpty()) {
                restaurants = restaurantService.getAllActiveRestaurants();
            } else {
                restaurants = restaurantService.searchRestaurantsByCuisine(cuisine)
                    .stream()
                    .filter(restaurant -> restaurant.getIsActive() != null && restaurant.getIsActive())
                    .toList();
            }
            
            logger.info("Found {} restaurants matching criteria", restaurants.size());
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            logger.error("Error searching restaurants: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
