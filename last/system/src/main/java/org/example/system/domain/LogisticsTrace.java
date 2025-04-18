package org.example.system.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "logistics_traces") // Specify MongoDB collection name
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsTrace {

    @Id
    private String id; // MongoDB uses String IDs by default

    private Long orderId; // Link to the Order entity (using its primary key)

    private LocalDateTime timestamp;
    private String location;
    private String description; // e.g., "Package arrived at sorting center", "Out for delivery"
    private String operator; // Optional: Name or ID of the person/system updating the status

    // Add latitude and longitude fields
    private Double latitude;  // 纬度
    private Double longitude; // 经度

    // Add a constructor for easier creation
    public LogisticsTrace(Long orderId, String location, String description, String operator) {
        this.orderId = orderId;
        this.timestamp = LocalDateTime.now();
        this.location = location;
        this.description = description;
        this.operator = operator;
    }

    // Consider adding a constructor that includes lat/lon
    public LogisticsTrace(Long orderId, String location, String description, String operator, Double latitude, Double longitude) {
        this.orderId = orderId;
        this.timestamp = LocalDateTime.now();
        this.location = location;
        this.description = description;
        this.operator = operator;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}