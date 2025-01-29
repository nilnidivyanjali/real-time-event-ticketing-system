package com.realtimeticketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the real time ticketing backend application.
 * Configures and starts the Spring Boot application.
 */
@SpringBootApplication
public class RealTimeTicketingBackendApplication {

    /**
     * Main method to launch the Spring Boot application.
     */
    public static void main(String[] args) {
        SpringApplication.run(RealTimeTicketingBackendApplication.class, args);
    }

}
