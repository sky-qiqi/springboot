package org.example.system.service;

import org.example.system.domain.LogisticsTrace;
import org.example.system.repository.LogisticsTraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class LogisticsService {

    private final LogisticsTraceRepository logisticsTraceRepository;
    private final MapService mapService; // Inject MapService
    private final NotificationService notificationService; // Inject NotificationService

    @Autowired
    public LogisticsService(LogisticsTraceRepository logisticsTraceRepository, MapService mapService, NotificationService notificationService) {
        this.logisticsTraceRepository = logisticsTraceRepository;
        this.mapService = mapService; // Initialize MapService
        this.notificationService = notificationService; // Initialize NotificationService
    }

    /**
     * Adds a new logistics trace record.
     *
     * @param trace The LogisticsTrace object to add.
     * @return The saved LogisticsTrace object.
     */
    public LogisticsTrace addTrace(LogisticsTrace trace) {
        // Potentially add validation or enrichment here
        LogisticsTrace savedTrace = logisticsTraceRepository.save(trace);

        // Send notification after saving the trace
        String notificationMessage = String.format("Order %d updated: %s at %s",
                savedTrace.getOrderId(),
                savedTrace.getDescription(),
                savedTrace.getLocation());
        notificationService.sendSmsNotification(notificationMessage);

        return savedTrace;
    }

    /**
     * Retrieves all logistics traces for a specific order, sorted by timestamp.
     *
     * @param orderId The ID of the order.
     * @return A list of LogisticsTrace objects.
     */
    public List<LogisticsTrace> getTracesForOrder(Long orderId) {
        return logisticsTraceRepository.findByOrderIdOrderByTimestampAsc(orderId);
    }

    /**
     * Retrieves the driving route for a specific order based on its first and last trace points.
     *
     * @param orderId The ID of the order.
     * @return A Mono containing the JSON response string from the map service, or an empty/error Mono.
     */
    public Mono<String> getRouteForOrder(Long orderId) {
        List<LogisticsTrace> traces = getTracesForOrder(orderId);

        if (traces == null || traces.size() < 2) {
            // Need at least two points (start and end) to determine a route
            System.err.println("Not enough trace points to determine a route for order: " + orderId);
            return Mono.empty(); // Return empty Mono if not enough points
        }

        // Assuming the first trace is the origin and the last is the destination
        LogisticsTrace originTrace = traces.get(0);
        LogisticsTrace destinationTrace = traces.get(traces.size() - 1);

        // Get actual coordinates from trace objects
        Double originLat = originTrace.getLatitude();
        Double originLon = originTrace.getLongitude();
        Double destLat = destinationTrace.getLatitude();
        Double destLon = destinationTrace.getLongitude();

        // Check if coordinates are available
        if (originLat == null || originLon == null || destLat == null || destLon == null) {
            System.err.println("Coordinate data missing in traces for order: " + orderId);
            // Optionally, you could try geocoding the location string if coordinates are missing
            return Mono.error(new IllegalArgumentException("Coordinate data missing for order: " + orderId)); // Return error Mono
        }

        System.out.println("Requesting route for order " + orderId + " from (" + originLat + "," + originLon + ") to (" + destLat + "," + destLon + ")");

        return mapService.getDrivingRoute(originLat, originLon, destLat, destLon);
    }

    // Add more business logic related to logistics traces if needed
}