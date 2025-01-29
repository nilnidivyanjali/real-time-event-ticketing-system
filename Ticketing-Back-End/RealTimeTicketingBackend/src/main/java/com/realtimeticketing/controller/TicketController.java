package com.realtimeticketing.controller;

import com.realtimeticketing.cli.Configuration;
import com.realtimeticketing.dto.ConfigurationRequest;
import com.realtimeticketing.service.APIService;
import com.realtimeticketing.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.Notification;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for handling ticket system operations.
 * Providing endpoints for configuring, starting,stopping,and monitoring the system.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"}) // Specify allowed origins
public class TicketController {

    APIService apiService; //Service for API operations
    NotificationService notificationService; //Service for notification operations

    public TicketController(APIService apiService, NotificationService notificationService) {
        this.apiService = apiService;
        this.notificationService = notificationService;
    }

    private List<String> logs = new ArrayList<>();
    private boolean systemRunning = false;

    // POST endpoint to configure the system
    @PostMapping("/configure")
    public ResponseEntity<String> configureSystem(@RequestBody ConfigurationRequest configRequest) {
        logs.add("System configured with: " + configRequest.toString());
        try {
            apiService.configure(configRequest);
            Configuration configuration = apiService.getConfiguration();
            notificationService.configure(configuration);
            return ResponseEntity.ok("Configuration received successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error configuring the system: " + e.getMessage());
        }

    }

    // GET endpoint to fetch ticket logs
    @GetMapping("/logs")
    public ResponseEntity<List<String>> getLogs() {
        return ResponseEntity.ok(logs);
    }

    // GET endpoint for real-time ticket status
    @GetMapping("/ticket-status")
    public ResponseEntity<String> getTicketStatus() {
        return ResponseEntity.ok(this.notificationService.getStatus());
    }

    // POST endpoint to start the system
    @PostMapping("/start")
    public ResponseEntity<String> startSystem() {
        //systemRunning = true;
        logs.add("System started.");
        System.out.println("System started.");
        notificationService.start();
        return ResponseEntity.ok("System started successfully!");
    }

    // POST endpoint to stop the system
    @PostMapping("/stop")
    public ResponseEntity<String> stopSystem() {
        systemRunning = false;
        logs.add("System stopped.");
        notificationService.stop();
        System.out.println("System stopped.");
        return ResponseEntity.ok("System stopped successfully!");
    }

    // POST endpoint to exit the system
    @PostMapping("/exit")
    public ResponseEntity<String> exitSystem() {
        systemRunning = false;
        logs.add("System exited.");
        notificationService.exit();
        System.out.println("System exited.");
        return ResponseEntity.ok("System exited successfully!");
    }
}




