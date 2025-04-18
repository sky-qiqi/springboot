package org.example.system.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders") // Use a more standard table name
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sender Information
    private String senderName;
    private String senderPhone;
    private String senderAddress;

    // Receiver Information
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    // Item Information
    private String itemName;
    private Double weightKg; // Weight in kilograms
    // private Double distanceKm; // Distance might be calculated dynamically or stored elsewhere

    // Financials
    private BigDecimal shippingCost;

    // Status and Timestamps
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = OrderStatus.PENDING; // Default status
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Enum for Order Status
    public enum OrderStatus {
        PENDING,        //待处理（刚下单）
        PICKED_UP,      // 已揽件
        IN_TRANSIT,     // 运输中
        OUT_FOR_DELIVERY,// 派送中
        DELIVERED,      // 已签收
        CANCELLED       // 已取消
    }
}