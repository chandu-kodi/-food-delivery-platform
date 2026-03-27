package com.example.restaurantservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restaurantservice.dto.MenuItemRequest;
import com.example.restaurantservice.entity.MenuCategory;
import com.example.restaurantservice.entity.MenuItem;
import com.example.restaurantservice.entity.Restaurant;
import com.example.restaurantservice.repository.MenuCategoryRepository;
import com.example.restaurantservice.repository.MenuItemRepository;
import com.example.restaurantservice.repository.RestaurantRepository;

@Service
@Transactional
public class AdminRestaurantService {
    private static final Logger logger = LoggerFactory.getLogger(AdminRestaurantService.class);
    
    private final RestaurantRepository restaurantRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;

    public AdminRestaurantService(RestaurantRepository restaurantRepository, 
                                 MenuCategoryRepository menuCategoryRepository,
                                 MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuCategoryRepository = menuCategoryRepository;
        this.menuItemRepository = menuItemRepository;
    }

    // Restaurant operations
    public List<Restaurant> getAllRestaurants() {
        logger.debug("Fetching all restaurants");
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Long id) {
        logger.debug("Fetching restaurant with id: {}", id);
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        logger.info("Creating new restaurant: {}", restaurant.getName());
        restaurant.setId(null); // Ensure it's a new entity
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
        logger.info("Updating restaurant with id: {}", id);
        
        Restaurant existingRestaurant = getRestaurantById(id);
        
        // Update fields
        existingRestaurant.setName(restaurant.getName());
        existingRestaurant.setDescription(restaurant.getDescription());
        existingRestaurant.setCuisineType(restaurant.getCuisineType());
        existingRestaurant.setAddress(restaurant.getAddress());
        existingRestaurant.setPhone(restaurant.getPhone());
        existingRestaurant.setEmail(restaurant.getEmail());
        existingRestaurant.setRating(restaurant.getRating());
        existingRestaurant.setImageUrl(restaurant.getImageUrl());
        existingRestaurant.setIsActive(restaurant.getIsActive());
        
        return restaurantRepository.save(existingRestaurant);
    }

    public void deleteRestaurant(Long id) {
        logger.info("Deleting restaurant with id: {}", id);
        if (!restaurantRepository.existsById(id)) {
            throw new RuntimeException("Restaurant not found with id: " + id);
        }
        restaurantRepository.deleteById(id);
    }

    // Menu Category operations
    public List<MenuCategory> getMenuCategories(Long restaurantId) {
        logger.debug("Fetching categories for restaurant: {}", restaurantId);
        return menuCategoryRepository.findByRestaurantIdOrderByDisplayOrderAsc(restaurantId);
    }

    public MenuCategory createMenuCategory(Long restaurantId, MenuCategory category) {
        logger.info("Creating category for restaurant {}: {}", restaurantId, category.getName());
        
        Restaurant restaurant = getRestaurantById(restaurantId);
        category.setId(null); // Ensure it's a new entity
        category.setRestaurant(restaurant);
        
        return menuCategoryRepository.save(category);
    }

    public MenuCategory updateMenuCategory(Long restaurantId, Long categoryId, MenuCategory category) {
        logger.info("Updating category {} for restaurant {}: {}", categoryId, restaurantId, category.getName());
        
        MenuCategory existingCategory = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        
        // Verify the category belongs to the specified restaurant
        if (!existingCategory.getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Category does not belong to the specified restaurant");
        }
        
        // Update fields
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        existingCategory.setDisplayOrder(category.getDisplayOrder());
        
        return menuCategoryRepository.save(existingCategory);
    }

    public void deleteMenuCategory(Long restaurantId, Long categoryId) {
        logger.info("Deleting category {} for restaurant {}", categoryId, restaurantId);
        
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        
        // Verify the category belongs to the specified restaurant
        if (!category.getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Category does not belong to the specified restaurant");
        }
        
        menuCategoryRepository.deleteById(categoryId);
    }

    // Menu Item operations
    public List<MenuItem> getMenuItems(Long restaurantId) {
        logger.debug("Fetching menu items for restaurant: {}", restaurantId);
        return menuItemRepository.findByRestaurantIdOrderByDisplayOrderAsc(restaurantId);
    }

    public MenuItem createMenuItem(Long restaurantId, MenuItemRequest menuItemRequest) {
        logger.info("Creating menu item for restaurant {}: {}", restaurantId, menuItemRequest.getName());
        
        Restaurant restaurant = getRestaurantById(restaurantId);
        
        // Create new MenuItem entity
        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemRequest.getName());
        menuItem.setDescription(menuItemRequest.getDescription());
        menuItem.setPrice(menuItemRequest.getPrice());
        menuItem.setImageUrl(menuItemRequest.getImageUrl());
        menuItem.setIsAvailable(menuItemRequest.getIsAvailable() != null ? menuItemRequest.getIsAvailable() : true);
        menuItem.setPreparationTime(menuItemRequest.getPreparationTime() != null ? menuItemRequest.getPreparationTime() : 15);
        menuItem.setDisplayOrder(menuItemRequest.getDisplayOrder() != null ? menuItemRequest.getDisplayOrder() : 0);
        menuItem.setRestaurant(restaurant);
        
        // Set category if provided
        if (menuItemRequest.getCategoryId() != null) {
            MenuCategory category = menuCategoryRepository.findById(menuItemRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + menuItemRequest.getCategoryId()));
            
            // Verify the category belongs to the specified restaurant
            if (!category.getRestaurant().getId().equals(restaurantId)) {
                throw new RuntimeException("Category does not belong to the specified restaurant");
            }
            menuItem.setCategory(category);
            logger.info("Assigned menu item to category: {}", category.getName());
        } else {
            logger.warn("Menu item created without category assignment: {}", menuItemRequest.getName());
        }
        
        return menuItemRepository.save(menuItem);
    }

    public MenuItem createMenuItem(Long restaurantId, MenuItem menuItem) {
        logger.info("Creating menu item for restaurant {}: {}", restaurantId, menuItem.getName());
        
        Restaurant restaurant = getRestaurantById(restaurantId);
        menuItem.setId(null); // Ensure it's a new entity
        menuItem.setRestaurant(restaurant);
        
        // Handle category assignment - support both category object and categoryId field
        if (menuItem.getCategory() != null && menuItem.getCategory().getId() != null) {
            // Full category object provided
            MenuCategory category = menuCategoryRepository.findById(menuItem.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + menuItem.getCategory().getId()));
            
            // Verify the category belongs to the specified restaurant
            if (!category.getRestaurant().getId().equals(restaurantId)) {
                throw new RuntimeException("Category does not belong to the specified restaurant");
            }
            menuItem.setCategory(category);
        } else {
            // Check if we have a categoryId field (from frontend form data)
            // This requires adding a categoryId field to MenuItem or using a DTO
            // For now, let's log to debug
            logger.warn("Menu item created without category assignment: {}", menuItem.getName());
        }
        
        return menuItemRepository.save(menuItem);
    }

    public MenuItem updateMenuItem(Long restaurantId, Long menuItemId, MenuItem menuItem) {
        logger.info("Updating menu item {} for restaurant {}: {}", menuItemId, restaurantId, menuItem.getName());
        
        MenuItem existingItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + menuItemId));
        
        // Verify the item belongs to the specified restaurant
        if (!existingItem.getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Menu item does not belong to the specified restaurant");
        }
        
        // Update fields
        existingItem.setName(menuItem.getName());
        existingItem.setDescription(menuItem.getDescription());
        existingItem.setPrice(menuItem.getPrice());
        existingItem.setImageUrl(menuItem.getImageUrl());
        existingItem.setIsAvailable(menuItem.getIsAvailable());
        existingItem.setIsVegetarian(menuItem.getIsVegetarian());
        existingItem.setPreparationTime(menuItem.getPreparationTime());
        existingItem.setDisplayOrder(menuItem.getDisplayOrder());
        
        // Update category if provided
        if (menuItem.getCategory() != null && menuItem.getCategory().getId() != null) {
            MenuCategory category = menuCategoryRepository.findById(menuItem.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + menuItem.getCategory().getId()));
            
            // Verify the category belongs to the specified restaurant
            if (!category.getRestaurant().getId().equals(restaurantId)) {
                throw new RuntimeException("Category does not belong to the specified restaurant");
            }
            existingItem.setCategory(category);
        }
        
        return menuItemRepository.save(existingItem);
    }

    public void deleteMenuItem(Long restaurantId, Long menuItemId) {
        logger.info("Deleting menu item {} for restaurant {}", menuItemId, restaurantId);
        
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + menuItemId));
        
        // Verify the item belongs to the specified restaurant
        if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Menu item does not belong to the specified restaurant");
        }
        
        menuItemRepository.deleteById(menuItemId);
    }
}
