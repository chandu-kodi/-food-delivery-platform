package com.example.restaurantservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.restaurantservice.entity.MenuCategory;
import com.example.restaurantservice.entity.MenuItem;
import com.example.restaurantservice.entity.Restaurant;
import com.example.restaurantservice.repository.MenuCategoryRepository;
import com.example.restaurantservice.repository.MenuItemRepository;
import com.example.restaurantservice.repository.RestaurantRepository;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantServiceImpl.class);
    
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, 
                               MenuItemRepository menuItemRepository,
                               MenuCategoryRepository menuCategoryRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.menuCategoryRepository = menuCategoryRepository;
    }

    @Override
    public List<Restaurant> getAllActiveRestaurants() {
        logger.debug("Fetching all active restaurants");
        return restaurantRepository.findByIsActiveTrue();
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        logger.debug("Fetching restaurant with id: {}", id);
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
    }

    @Override
    public List<MenuItem> getMenuItemsByRestaurant(Long restaurantId) {
        logger.debug("Fetching menu items for restaurant: {}", restaurantId);
        return menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId);
    }

    @Override
    public List<Restaurant> searchRestaurantsByCuisine(String cuisineType) {
        logger.debug("Searching restaurants by cuisine: {}", cuisineType);
        return restaurantRepository.findByCuisineTypeContainingIgnoreCase(cuisineType);
    }

    @Override
    public Map<String, Object> getRestaurantMenu(Long restaurantId) {
        logger.debug("Fetching complete menu for restaurant: {}", restaurantId);
        
        // Verify restaurant exists
        getRestaurantById(restaurantId);
        
        // Get categories and menu items (only available items for public API)
        List<MenuCategory> categories = menuCategoryRepository.findByRestaurantIdOrderByDisplayOrderAsc(restaurantId);
        List<MenuItem> menuItems = menuItemRepository.findByRestaurantIdAndIsAvailableTrueOrderByDisplayOrderAsc(restaurantId);
        
        // Group menu items by category
        Map<Long, List<MenuItem>> itemsByCategory = menuItems.stream()
                .collect(Collectors.groupingBy(item -> {
                    MenuCategory category = item.getCategory();
                    return category != null ? category.getId() : 0L;
                }));
        
        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("categories", categories);
        response.put("menuItems", menuItems);
        response.put("itemsByCategory", itemsByCategory);
        
        return response;
    }

    @Override
    public List<MenuCategory> getRestaurantCategories(Long restaurantId) {
        logger.debug("Fetching categories for restaurant: {}", restaurantId);
        return menuCategoryRepository.findByRestaurantIdOrderByDisplayOrderAsc(restaurantId);
    }

    @Override
    public List<MenuItem> getMenuItemsByCategory(Long restaurantId, Long categoryId) {
        logger.debug("Fetching menu items for category {} in restaurant: {}", categoryId, restaurantId);
        return menuItemRepository.findByRestaurantIdAndCategoryIdAndIsAvailableTrueOrderByDisplayOrderAsc(restaurantId, categoryId);
    }
}
