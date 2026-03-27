package com.example.restaurantservice.service;

import java.util.List;
import java.util.Map;

import com.example.restaurantservice.entity.MenuCategory;
import com.example.restaurantservice.entity.MenuItem;
import com.example.restaurantservice.entity.Restaurant;

public interface RestaurantService {
    List<Restaurant> getAllActiveRestaurants();
    Restaurant getRestaurantById(Long id);
    List<MenuItem> getMenuItemsByRestaurant(Long restaurantId);
    List<Restaurant> searchRestaurantsByCuisine(String cuisineType);
    
    // New menu-related methods
    Map<String, Object> getRestaurantMenu(Long restaurantId);
    List<MenuCategory> getRestaurantCategories(Long restaurantId);
    List<MenuItem> getMenuItemsByCategory(Long restaurantId, Long categoryId);
}
