package com.example.restaurantservice.repository;

import com.example.restaurantservice.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    List<MenuCategory> findByRestaurantIdOrderByDisplayOrderAsc(Long restaurantId);
    List<MenuCategory> findByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name);
}
