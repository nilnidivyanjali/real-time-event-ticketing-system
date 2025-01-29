package com.realtimeticketing.service;

import com.realtimeticketing.cli.Configuration;
import com.realtimeticketing.cli.utils.Logger;
import com.realtimeticketing.dto.ConfigurationRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class APIService {
    private final SimpMessagingTemplate template;
    private Configuration configuration;

    public APIService(SimpMessagingTemplate template) {
        this.template = template;
    }


    public void configure(ConfigurationRequest configurationRequest) throws Exception{
        Configuration configuration = new Configuration(
                configurationRequest.getTotalTickets(),
                configurationRequest.getTicketReleaseRate(),
                configurationRequest.getCustomerRetrievalRate(),
                configurationRequest.getMaxTicketCapacity());
        this.configuration = configuration;
        Logger.log("Prompting user for configuration.");
        System.out.println("Configure the system");

        // Creating new configuration


        // Saving the configuration in both formats
        try {
            configuration.exportToJson("config.json");
            Logger.log("Configuration saved to JSON");
            template.convertAndSend("/topic/messages", "Configuration saved to JSON");
        } catch (IOException e) {
            Logger.log("Error occurred when saving configuration to JSON." + e.getMessage());
            throw new Exception("Error occurred when saving configuration to JSON." + e.getMessage());
        }

        try {
            configuration.exportToText("config.txt");
            Logger.log("Configuration saved to plain text");
            template.convertAndSend("/topic/messages", "Configuration saved to plain text");
        } catch (IOException e) {
            Logger.log("Error occurred when saving configuration to plain text." + e.getMessage());
            throw new Exception("Error occurred when saving configuration to plain text." + e.getMessage());
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }



}
