package org.example.system.repository;

import org.example.system.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // You can add custom query methods here if needed
    // For example: List<Order> findByStatus(Order.OrderStatus status);
}