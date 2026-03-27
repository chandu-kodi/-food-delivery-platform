package com.example.paymentservice.service;

import com.example.paymentservice.dto.OrderCreatedEvent;
import com.example.paymentservice.dto.PaymentCompletedEvent;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.repository.PaymentRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository repository;
    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;
    private final String topic;
    private final org.modelmapper.ModelMapper mapper;

    public PaymentServiceImpl(PaymentRepository repository,
                              KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate,
                              org.springframework.core.env.Environment env,
                              org.modelmapper.ModelMapper mapper) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.topic = env.getProperty("spring.kafka.topic.payment-completed");
        this.mapper = mapper;
    }

    @Override
    public PaymentCompletedEvent processPayment(OrderCreatedEvent orderEvent) {
        // simplistic processing logic
        Double amount = orderEvent.getTotalAmount().doubleValue();
        Payment payment = Payment.builder()
                .orderId(orderEvent.getOrderId())
                .userId(orderEvent.getUserId())
                .amount(BigDecimal.valueOf(amount))
                .status(Payment.PaymentStatus.COMPLETED)
                .build();
        Payment saved = repository.save(payment);
        PaymentCompletedEvent event = new PaymentCompletedEvent(saved.getId(), saved.getOrderId(), saved.getStatus().name(), saved.getAmount().doubleValue());
        kafkaTemplate.send(topic, event);
        return event;
    }

    @Override
    public com.example.paymentservice.dto.PaymentResponse createPayment(com.example.paymentservice.dto.PaymentRequest request) {
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .restaurantId(request.getRestaurantId())
                .orderNumber(request.getOrderNumber())
                .paymentMethod(request.getPaymentMethod())
                .amount(request.getAmount())
                .deliveryAddress(request.getDeliveryAddress())
                .phoneNumber(request.getPhoneNumber())
                .specialInstructions(request.getSpecialInstructions())
                .status(Payment.PaymentStatus.PENDING)
                .build();
        Payment saved = repository.save(payment);
        return mapper.map(saved, com.example.paymentservice.dto.PaymentResponse.class);
    }

    @Override
    public com.example.paymentservice.dto.PaymentResponse getPaymentById(Long id) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        return mapper.map(payment, com.example.paymentservice.dto.PaymentResponse.class);
    }
}
