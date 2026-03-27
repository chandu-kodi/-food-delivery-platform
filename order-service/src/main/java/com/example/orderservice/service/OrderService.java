package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    OrderResponse getOrderByNumber(String orderNumber);
    List<OrderResponse> getOrdersByUserId(Long userId);
    List<OrderResponse> getOrdersByRestaurantId(Long restaurantId);
    OrderResponse updateOrderStatus(Long id, Order.OrderStatus status);
    List<OrderResponse> getOrdersByStatus(Order.OrderStatus status);
    void cancelOrder(Long id);
}
