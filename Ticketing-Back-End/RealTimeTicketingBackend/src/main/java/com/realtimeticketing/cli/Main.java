package com.realtimeticketing.cli;

import com.realtimeticketing.cli.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main class for the ticketing system application. Handles system initialisation, configuration,
 * thread management, and command processing for vendors and customers.
 */
public class Main {

    private static final AtomicInteger ticketCounter = new AtomicInteger(1); //Global ticket counter for unique IDs

    /**
     * Entry point for the application. Initializes the system, processes user commands, and manages threads.
     */
    public static void main(String[] args) {

       Scanner sc = new Scanner(System.in);
       Configuration config = null;
        List<Thread> vendorThreads = new ArrayList<>();
        List<Thread> customerThreads = new ArrayList<>();
        TicketPool ticketPool = null;


        Logger.log("Starting the ticketing system...");

       // Loading existing configuration
        try{

            config = Configuration.importFromJson("config.json");
            Logger.log("Loaded previous configuration from JSON");
            displayConfig(config);

        } catch (IOException e) {
            Logger.log("No previous configuration in JSON .Checking for plain text...");

            try {
                config = Configuration.importFromText("config.txt");
                Logger.log("Loaded previous configuration from plain text:");
                displayConfig(config);

            } catch (IOException ex) {
                Logger.log("No previous configuration found in any format. Create one.");
            }

        }
            // Prompting user for configuration
        if (config == null) {

            Logger.log("Prompting user for configuration.");
            System.out.println("Configure the system");
            int totalTickets = getValidatedInput(sc, "Enter Total Number of Tickets: ", 1, Integer.MAX_VALUE);
            int ticketReleaseRate = getValidatedInput(sc, "Enter Ticket Release Rate (Between 1 to 50): ", 1, 50);
            int customerRetrievalRate = getValidatedInput(sc, "Enter Customer Retrieval Rate (Between 1 to 100): ", 1, 100);
            int maxTicketCapacity = getValidatedInput(sc, "Enter Maximum Ticket Capacity: ", 1, Integer.MAX_VALUE);

            // Creating new configuration
            config = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

            // Saving the configuration in both formats
            try {

                config.exportToJson("config.json");
                Logger.log("Configuration saved to JSON");

            } catch (IOException e) {
                Logger.log("Error occurred when saving configuration to JSON." + e.getMessage());
            }

            try {
                config.exportToText("config.txt");
                Logger.log("Configuration saved to plain text");

            } catch (IOException e) {
                Logger.log("Error occurred when saving configuration to plain text." + e.getMessage());
            }


            // Displaying final configuration
            Logger.log("\nFinal Configuration");
            displayConfig(config);

            Logger.log("Ticketing system setup completed successfully.");

        }

        // Initializing Ticket Pool
        ticketPool = new TicketPool(config.getMaxTicketCapacity());

        //Command loop
        System.out.println("Enter commands: 's' to start, 'p' to pause, or 'e' to exit.");

        while(true) {
            System.out.print("> ");
            String command = sc.nextLine().trim().toLowerCase();

            switch (command) {
                case "s":
                    if (vendorThreads.isEmpty() && customerThreads.isEmpty()) {
                        Logger.log("Starting vendors and customers...");

                        // Creating Vendor Threads
                        for (int i = 1; i <= 3; i++) { // Simulating 3 vendors
                            Thread vendorThread = new Thread(new Vendor("Vendor" + i, ticketPool, generateUniqueTickets(config.getTotalTickets()), config.getTicketReleaseRate() * 1000));
                            vendorThreads.add(vendorThread);
                            vendorThread.start();
                        }
                        // Create Customer threads
                        for (int i = 1; i <= 5; i++) { // Simulating 5 customers
                            Thread customerThread = new Thread(new Customer("Customer" + i, ticketPool, config.getCustomerRetrievalRate() * 1000));
                            customerThreads.add(customerThread);
                            customerThread.start();
                        }
                        System.out.println("Vendors and Customers started.");

                    } else {
                        System.out.println("Threads are already running.");

                    }
                    break;

                case "p":
                    Logger.log("Stopping all threads...");
                    stopThreads(vendorThreads);
                    stopThreads(customerThreads);
                    vendorThreads.clear();
                    customerThreads.clear();
                    System.out.println("All threads stopped.");
                    break;

                case "e":
                    Logger.log("Exiting the application...");
                    stopThreads(vendorThreads);
                    stopThreads(customerThreads);
                    System.out.println("Application exited.");
                    return;

                default:
                    System.out.println("Invalid command. 's' to start, 'p' to pause, or 'e' to exit.");
            }

        }
    }

    /**
     * Displaying the current system configuration to the console and logging it.
     * @param config This is the configuration object.
     */
    private static void displayConfig(Configuration config) {

        System.out.println("Total Tickets: " + config.getTotalTickets());
        System.out.println("Ticket Release Rate: " + config.getTicketReleaseRate());
        System.out.println("Customer Retrieval Rate: " + config.getCustomerRetrievalRate());
        System.out.println("Maximum Ticket Capacity: " + config.getMaxTicketCapacity());

        Logger.log("Final Configuration Details: " +
                "Total Tickets: " + config.getTotalTickets() +
                ", Ticket Release Rate: " + config.getTicketReleaseRate() +
                ", Customer Retrieval Rate: " + config.getCustomerRetrievalRate() +
                ", Maximum Ticket Capacity: " + config.getMaxTicketCapacity());
    }


    /**
     * Prompting the user for input and validating it against specified minimum and maximum values.
     * @param scanner This is the Scanner object for user input.
     * @param prompt This is the prompt message to display.
     * @param min This is the minimum valid value.
     * @param max This he maximum valid value.
     * @return a valid integer input from the user.
     */
    private static int getValidatedInput(Scanner scanner, String prompt, int min, int max) {
        int input;
        while(true) {

            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) return input;
                System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                Logger.log("Invalid input. User entered a number outside the range " + min + " and " + max + ".");

            } catch (NumberFormatException e) {

                System.out.println("Invalid input. Enter a valid number");
                Logger.log("Invalid input. User entered a non-numeric value.");
            }
        }
    }

    /**
     * Interrupts and stops all threads in the list.
     * @param threads This is the list of threads to stop.
     */
    private static void stopThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    /**
     * Generates a specified number of unique tickets with random prices and event names.
     * @param count This is the number of tickets to generate.
     * @return a list of generated tickets.
     */

    private static List<Ticket> generateUniqueTickets(int count) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            int ticketId = ticketCounter.getAndIncrement(); // Ensure unique ticket ID
            String eventName = "Event_" + (1000 + ticketId); // Generate unique event names using an offset
            double ticketPrice = 10.0 + (Math.random() * 40.0); // Random price between $10 and $50
            tickets.add(new Ticket("TID" + ticketId, eventName, Math.round(ticketPrice * 100.0) / 100.0)); // Round price to 2 decimals
        }
        return tickets;
    }

}



