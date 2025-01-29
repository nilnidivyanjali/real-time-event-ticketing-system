package com.realtimeticketing.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory message broker to carry the message back to the client
        config.enableSimpleBroker("/topic");
        // Designates the prefix to filter destinations that the client will send messages to
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the "/chat" endpoint for WebSocket connections
        registry.addEndpoint("/chat")
                .setAllowedOrigins("http://127.0.0.1:4200", "http://localhost:4200" , "http://localhost:8081") // Allow connection from this origin
                .withSockJS();
    }
}