package org.example.system.controller;

import org.example.system.domain.LogisticsTrace;
import org.example.system.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/logistics") // Base path for logistics related endpoints
public class LogisticsController {

    private final LogisticsService logisticsService;

    @Autowired
    public LogisticsController(LogisticsService logisticsService) {
        this.logisticsService = logisticsService;
    }

    // POST /api/logistics - Add a new logistics trace
    @PostMapping
    public ResponseEntity<LogisticsTrace> addTrace(@RequestBody LogisticsTrace trace) {
        // Basic validation: Ensure orderId is present
        if (trace.getOrderId() == null) {
            return ResponseEntity.badRequest().build(); // Or throw a specific exception
        }
        LogisticsTrace createdTrace = logisticsService.addTrace(trace);
        return new ResponseEntity<>(createdTrace, HttpStatus.CREATED);
    }

    // GET /api/logistics/order/{orderId} - Get all traces for a specific order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<LogisticsTrace>> getTracesForOrder(@PathVariable Long orderId) {
        List<LogisticsTrace> traces = logisticsService.getTracesForOrder(orderId);
        if (traces.isEmpty()) {
            // Consider returning 404 if the order itself doesn't exist, 
            // or an empty list if the order exists but has no traces.
            // For simplicity, returning OK with an empty list.
            return ResponseEntity.ok(traces);
        }
        return ResponseEntity.ok(traces);
    }

    // GET /api/logistics/order/{orderId}/route - Get the driving route for an order
    @GetMapping("/order/{orderId}/route")
    public Mono<ResponseEntity<String>> getOrderRoute(@PathVariable Long orderId) {
        return logisticsService.getRouteForOrder(orderId)
                .map(routeJson -> ResponseEntity.ok(routeJson)) // If successful, wrap JSON string in ResponseEntity
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not retrieve route for order: " + orderId + ". Not enough trace points.")) // Handle empty Mono (e.g., not enough points)
                .onErrorResume(IllegalArgumentException.class, e -> 
                    Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage())) // Handle specific errors like missing coordinates
                )
                .onErrorResume(e -> 
                    Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving route: " + e.getMessage())) // Handle other potential errors
                );
    }

    // Add other endpoints as needed, e.g., get a specific trace by its ID
}