package org.example.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // Enable Spring Cache
public class LogisticsTrackingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogisticsTrackingSystemApplication.class, args);
	}

}