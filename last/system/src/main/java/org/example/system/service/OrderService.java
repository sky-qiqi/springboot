package org.example.system.service;

import org.example.system.domain.Order;
import org.example.system.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Order order) {
        // Basic validation or default setting
        if (order.getStatus() == null) {
            order.setStatus(Order.OrderStatus.PENDING);
        }
        // Simple shipping cost calculation (example: fixed cost + weight based)
        BigDecimal baseCost = BigDecimal.valueOf(5.0);
        BigDecimal weightCost = BigDecimal.valueOf(order.getWeightKg() != null ? order.getWeightKg() : 0.0).multiply(BigDecimal.valueOf(1.5));
        order.setShippingCost(baseCost.add(weightCost));

        // Set timestamps via @PrePersist
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Optional<Order> updateOrderStatus(Long id, Order.OrderStatus newStatus) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(newStatus);
            // Timestamps updated via @PreUpdate
            return Optional.of(orderRepository.save(order));
        }
        return Optional.empty();
    }

    // Add more business logic methods as needed
    // e.g., calculateShippingCost based on distance, etc.
}