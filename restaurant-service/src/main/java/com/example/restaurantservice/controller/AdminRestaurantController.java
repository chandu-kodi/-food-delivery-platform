package com.example.restaurantservice.controller;

import com.example.restaurantservice.dto.MenuItemRequest;
import com.example.restaurantservice.entity.MenuCategory;
import com.example.restaurantservice.entity.MenuItem;
import com.example.restaurantservice.entity.Restaurant;
import com.example.restaurantservice.service.AdminRestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminRestaurantController {
    private static final Logger logger = LoggerFactory.getLogger(AdminRestaurantController.class);
    private final AdminRestaurantService adminRestaurantService;

    public AdminRestaurantController(AdminRestaurantService adminRestaurantService) {
        this.adminRestaurantService = adminRestaurantService;
    }

    // Simple test endpoint
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        logger.info("Test endpoint called");
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Restaurant service is running");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    // Restaurant endpoints
    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        try {
            logger.info("Getting all restaurants...");
            List<Restaurant> restaurants = adminRestaurantService.getAllRestaurants();
            logger.info("Found {} restaurants", restaurants.size());
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            logger.error("Error fetching restaurants: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Simple restaurants test endpoint
    @GetMapping("/restaurants/simple")
    public ResponseEntity<Map<String, Object>> getSimpleRestaurants() {
        try {
            logger.info("Getting simple restaurants test...");
            Map<String, Object> response = new HashMap<>();
            response.put("status", "OK");
            response.put("message", "Simple restaurants test working");
            response.put("timestamp", LocalDateTime.now().toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error in simple restaurants test: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        try {
            Restaurant restaurant = adminRestaurantService.getRestaurantById(id);
            return ResponseEntity.ok(restaurant);
        } catch (RuntimeException e) {
            logger.error("Error fetching restaurant {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error fetching restaurant {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/restaurants")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        try {
            Restaurant savedRestaurant = adminRestaurantService.createRestaurant(restaurant);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRestaurant);
        } catch (Exception e) {
            logger.error("Error creating restaurant: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        try {
            Restaurant updatedRestaurant = adminRestaurantService.updateRestaurant(id, restaurant);
            return ResponseEntity.ok(updatedRestaurant);
        } catch (RuntimeException e) {
            logger.error("Error updating restaurant {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error updating restaurant {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        try {
            adminRestaurantService.deleteRestaurant(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting restaurant {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error deleting restaurant {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Menu Category endpoints
    @GetMapping("/restaurants/{restaurantId}/categories")
    public ResponseEntity<List<MenuCategory>> getMenuCategories(@PathVariable Long restaurantId) {
        try {
            logger.info("Getting categories for restaurant {}", restaurantId);
            List<MenuCategory> categories = adminRestaurantService.getMenuCategories(restaurantId);
            logger.info("Found {} categories for restaurant {}", categories.size(), restaurantId);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Error fetching categories for restaurant {}: {}", restaurantId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/restaurants/{restaurantId}/categories")
    public ResponseEntity<MenuCategory> createMenuCategory(@PathVariable Long restaurantId, @RequestBody MenuCategory category) {
        try {
            logger.info("Creating category for restaurant {}: {}", restaurantId, category.getName());
            MenuCategory savedCategory = adminRestaurantService.createMenuCategory(restaurantId, category);
            logger.info("Successfully created category with ID: {}", savedCategory.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (Exception e) {
            logger.error("Error creating category for restaurant {}: {}", restaurantId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/restaurants/{restaurantId}/categories/{categoryId}")
    public ResponseEntity<MenuCategory> updateMenuCategory(@PathVariable Long restaurantId, @PathVariable Long categoryId, @RequestBody MenuCategory category) {
        try {
            MenuCategory updatedCategory = adminRestaurantService.updateMenuCategory(restaurantId, categoryId, category);
            return ResponseEntity.ok(updatedCategory);
        } catch (RuntimeException e) {
            logger.error("Error updating category {} for restaurant {}: {}", categoryId, restaurantId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error updating category {} for restaurant {}: {}", categoryId, restaurantId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/restaurants/{restaurantId}/categories/{categoryId}")
    public ResponseEntity<Void> deleteMenuCategory(@PathVariable Long restaurantId, @PathVariable Long categoryId) {
        try {
            adminRestaurantService.deleteMenuCategory(restaurantId, categoryId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting category {} for restaurant {}: {}", categoryId, restaurantId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error deleting category {} for restaurant {}: {}", categoryId, restaurantId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Menu Item endpoints
    @GetMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<List<MenuItem>> getMenuItems(@PathVariable Long restaurantId) {
        try {
            List<MenuItem> menuItems = adminRestaurantService.getMenuItems(restaurantId);
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            logger.error("Error fetching menu items for restaurant {}: {}", restaurantId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<MenuItem> createMenuItem(@PathVariable Long restaurantId, @RequestBody MenuItemRequest menuItemRequest) {
        try {
            logger.info("Creating menu item for restaurant {}: {}", restaurantId, menuItemRequest.getName());
            MenuItem savedMenuItem = adminRestaurantService.createMenuItem(restaurantId, menuItemRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMenuItem);
        } catch (Exception e) {
            logger.error("Error creating menu item for restaurant {}: {}", restaurantId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/restaurants/{restaurantId}/menu-items/{menuItemId}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long restaurantId, @PathVariable Long menuItemId, @RequestBody MenuItem menuItem) {
        try {
            MenuItem updatedMenuItem = adminRestaurantService.updateMenuItem(restaurantId, menuItemId, menuItem);
            return ResponseEntity.ok(updatedMenuItem);
        } catch (RuntimeException e) {
            logger.error("Error updating menu item {} for restaurant {}: {}", menuItemId, restaurantId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error updating menu item {} for restaurant {}: {}", menuItemId, restaurantId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/restaurants/{restaurantId}/menu-items/{menuItemId}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long restaurantId, @PathVariable Long menuItemId) {
        try {
            adminRestaurantService.deleteMenuItem(restaurantId, menuItemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting menu item {} for restaurant {}: {}", menuItemId, restaurantId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error deleting menu item {} for restaurant {}: {}", menuItemId, restaurantId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
