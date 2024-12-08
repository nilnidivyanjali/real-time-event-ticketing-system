import utils.Logger;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final AtomicInteger ticketCounter = new AtomicInteger(1); //Global ticket counter for unique IDs

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
            // The ranges for TicketReleaseRate (1 to 50 tickets/second) and CustomerRetrievalRate (1 to 100 tickets/second)
            // were chosen to ensure realistic system performance and prevent excessive load or invalid configurations during simulation
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
        System.out.println("Enter commands: 'start', 'stop', or 'exit'.");

        while(true) {
            System.out.print("> ");
            String command = sc.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
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
                case "stop":
                    Logger.log("Stopping all threads...");
                    stopThreads(vendorThreads);
                    stopThreads(customerThreads);
                    vendorThreads.clear();
                    customerThreads.clear();
                    System.out.println("All threads stopped.");
                    break;

                case "exit":
                    Logger.log("Exiting the application...");
                    stopThreads(vendorThreads);
                    stopThreads(customerThreads);
                    System.out.println("Application exited.");
                    return;

                default:
                    System.out.println("Invalid command. Please enter 'start', 'stop', or 'exit'.");
            }

        }
    }

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

    private static void stopThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }



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



