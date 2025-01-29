package com.realtimeticketing.cli;

import com.google.gson.Gson;

import java.io.*;

/**
 * Representing the system configuration for a ticketing application.
 * Providing methods to save and load configuration data in JSON and plain text formats.
 */
public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    //Constructors
    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    //Getters
    public int getTotalTickets() {
        return totalTickets;
    }
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }


    //Setters
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }
    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    /**
     * Saves the configuration to a JSON file through serialization.
     * @param filePath This is the path to the JSON file.
     * @throws IOException If an I/O error occurs.
     */
    public void exportToJson(String filePath) throws IOException {
        Gson gson = new Gson();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(gson.toJson(this));
        }
    }

    /**
     * Loads the configuration from a JSON file.
     * @param filePath This is the path to the JSON file.
     * @return Loaded configuration object.
     * @throws IOException If an I/O error occurs.
     */
    public static Configuration importFromJson(String filePath) throws IOException {
        Gson gson = new Gson();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return gson.fromJson(reader, Configuration.class);
        }
    }

    /**
     * Saving the configuration to a plain text file.
     * @param filePath This is the path to the plain text file.
     * @throws IOException If an I/O error occurs.
     */
    public void exportToText(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("Total Tickets: " + totalTickets + "\n");
            writer.write("Ticket Release Rate: " + ticketReleaseRate + "\n");
            writer.write("Customer Retrieval Rate: " + customerRetrievalRate + "\n");
            writer.write("Maximum Ticket Capacity: " + maxTicketCapacity + "\n");
        }
    }

    /**
     * Loading the configuration from a plain text file.
     * @param filePath This is the path to the plain text file.
     * @return Loaded configuration object.
     * @throws IOException If an I/O error occurs.
     */
    public static Configuration importFromText(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            int totalTickets = Integer.parseInt(reader.readLine().split(": ")[1]);
            int ticketReleaseRate = Integer.parseInt(reader.readLine().split(": ")[1]);
            int customerRetrievalRate = Integer.parseInt(reader.readLine().split(": ")[1]);
            int maxTicketCapacity = Integer.parseInt(reader.readLine().split(": ")[1]);
            return new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        }
    }
}


