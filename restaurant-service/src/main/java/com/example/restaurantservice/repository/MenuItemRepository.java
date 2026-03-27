package com.example.restaurantservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurantservice.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurantIdAndIsAvailableTrue(Long restaurantId);
    List<MenuItem> findByRestaurantIdAndCategoryIdAndIsAvailableTrue(Long restaurantId, Long categoryId);
    List<MenuItem> findByRestaurantIdOrderByDisplayOrderAsc(Long restaurantId);
    List<MenuItem> findByRestaurantIdAndCategoryIdOrderByDisplayOrderAsc(Long restaurantId, Long categoryId);
    List<MenuItem> findByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name);
    List<MenuItem> findByRestaurantIdAndIsAvailableTrueOrderByDisplayOrderAsc(Long restaurantId);
    List<MenuItem> findByRestaurantIdAndCategoryIdAndIsAvailableTrueOrderByDisplayOrderAsc(Long restaurantId, Long categoryId);
}
