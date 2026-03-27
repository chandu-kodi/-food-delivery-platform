package com.example.orderservice.service;

import com.example.orderservice.dto.OrderCreatedEvent;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderServiceImpl(OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository,
                           KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        logger.info("Creating order for user {} and restaurant {}", request.getUserId(), request.getRestaurantId());
        
        // Calculate total amount
        BigDecimal totalAmount = request.getOrderItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = Order.builder()
                .userId(request.getUserId())
                .restaurantId(request.getRestaurantId())
                .deliveryAddress(request.getDeliveryAddress())
                .phoneNumber(request.getPhoneNumber())
                .specialInstructions(request.getSpecialInstructions())
                .totalAmount(totalAmount)
                .status(Order.OrderStatus.PENDING)
                .estimatedDeliveryTime(LocalDateTime.now().plusMinutes(45)) // Default 45 minutes
                .build();

        Order savedOrder = orderRepository.save(order);

        // Create order items
        List<OrderItem> orderItems = request.getOrderItems().stream()
                .map(itemRequest -> {
                    BigDecimal itemTotal = itemRequest.getUnitPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
                    return OrderItem.builder()
                            .order(savedOrder)
                            .menuItemId(itemRequest.getMenuItemId())
                            .menuItemName(itemRequest.getMenuItemName())
                            .quantity(itemRequest.getQuantity())
                            .unitPrice(itemRequest.getUnitPrice())
                            .specialInstructions(itemRequest.getSpecialInstructions())
                            .build();
                })
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);

        logger.info("Order created successfully with ID: {}", savedOrder.getId());

        // Send Kafka event
        try {
            OrderCreatedEvent event = new OrderCreatedEvent(
                    savedOrder.getId(),
                    savedOrder.getOrderNumber(),
                    savedOrder.getUserId(),
                    savedOrder.getRestaurantId(),
                    savedOrder.getTotalAmount(),
                    savedOrder.getStatus().name(),
                    savedOrder.getCreatedAt()
            );
            kafkaTemplate.send("order-created", event);
            logger.debug("Order created event sent to Kafka");
        } catch (Exception e) {
            logger.error("Failed to send order created event to Kafka: {}", e.getMessage());
        }

        return convertToResponse(savedOrder);
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public OrderResponse getOrderById(Long id) {
        logger.debug("Fetching order by ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return convertToResponse(order);
    }

    @Override
    @Cacheable(value = "orders", key = "#orderNumber")
    public OrderResponse getOrderByNumber(String orderNumber) {
        logger.debug("Fetching order by number: {}", orderNumber);
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found with number: " + orderNumber));
        return convertToResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        logger.debug("Fetching orders for user: {}", userId);
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByRestaurantId(Long restaurantId) {
        logger.debug("Fetching orders for restaurant: {}", restaurantId);
        List<Order> orders = orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, Order.OrderStatus status) {
        logger.info("Updating order {} status to: {}", id, status);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        order.setStatus(status);
        
        // Update estimated delivery time based on status
        switch (status) {
            case CONFIRMED:
                order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(40));
                break;
            case PREPARING:
                order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(25));
                break;
            case OUT_FOR_DELIVERY:
                order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(15));
                break;
            case DELIVERED:
                order.setEstimatedDeliveryTime(LocalDateTime.now());
                break;
        }
        
        Order updatedOrder = orderRepository.save(order);
        return convertToResponse(updatedOrder);
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(Order.OrderStatus status) {
        logger.debug("Fetching orders with status: {}", status);
        List<Order> orders = orderRepository.findByStatusOrderByCreatedAtDesc(status);
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        logger.info("Cancelling order: {}", id);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel delivered order");
        }
        
        if (order.getStatus() == Order.OrderStatus.OUT_FOR_DELIVERY) {
            throw new RuntimeException("Cannot cancel order that is out for delivery");
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setUserId(order.getUserId());
        response.setRestaurantId(order.getRestaurantId());
        response.setRestaurantName("Restaurant " + order.getRestaurantId()); // Placeholder - can be enhanced later
        response.setStatus(order.getStatus().name());
        response.setTotalAmount(order.getTotalAmount());
        response.setDeliveryAddress(order.getDeliveryAddress());
        response.setPhoneNumber(order.getPhoneNumber());
        response.setSpecialInstructions(order.getSpecialInstructions());
        response.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        if (order.getOrderItems() != null) {
            response.setOrderItems(order.getOrderItems().stream()
                    .map(item -> {
                        OrderResponse.OrderItemResponse itemResponse = new OrderResponse.OrderItemResponse();
                        itemResponse.setId(item.getId());
                        itemResponse.setMenuItemId(item.getMenuItemId());
                        itemResponse.setMenuItemName(item.getMenuItemName());
                        itemResponse.setQuantity(item.getQuantity());
                        itemResponse.setUnitPrice(item.getUnitPrice());
                        itemResponse.setTotalPrice(item.getTotalPrice());
                        itemResponse.setSpecialInstructions(item.getSpecialInstructions());
                        return itemResponse;
                    })
                    .collect(Collectors.toList()));
        }

        return response;
    }
}
