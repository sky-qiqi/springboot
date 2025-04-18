package org.example.system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class MapService {

    private final WebClient webClient;
    private final String apiKey;
    private static final String GAODE_DRIVING_URL = "https://restapi.amap.com/v3/direction/driving";

    // Constructor injection for WebClient.Builder and apiKey
    public MapService(WebClient.Builder webClientBuilder, @Value("${gaode.api.key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl(GAODE_DRIVING_URL).build();
        this.apiKey = apiKey;
    }

    /**
     * Gets driving directions between two points using Gaode Maps Web API.
     *
     * @param originLat      Origin latitude
     * @param originLon      Origin longitude
     * @param destinationLat Destination latitude
     * @param destinationLon Destination longitude
     * @return A Mono<String> containing the JSON response from the Gaode API, or an error Mono.
     */
    public Mono<String> getDrivingRoute(double originLat, double originLon, double destinationLat, double destinationLon) {
        String origin = originLon + "," + originLat;
        String destination = destinationLon + "," + destinationLat;

        System.out.println("Using Gaode API Key: " + apiKey); // Example usage
        System.out.println("Fetching route from (" + originLat + "," + originLon + ") to (" + destinationLat + "," + destinationLon + ") via Web API");

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", this.apiKey)
                        .queryParam("origin", origin)
                        .queryParam("destination", destination)
                        // Add other parameters as needed, e.g., output format
                        .queryParam("output", "json")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10)) // Add a timeout
                .doOnSuccess(response -> System.out.println("Successfully fetched route from Gaode Web API."))
                .doOnError(error -> System.err.println("Error calling Gaode Web API: " + error.getMessage()));
                // Further processing (e.g., parsing JSON) can be added here or in the calling service
    }

    // Add other map-related methods as needed (e.g., geocoding, static map generation using WebClient)
}