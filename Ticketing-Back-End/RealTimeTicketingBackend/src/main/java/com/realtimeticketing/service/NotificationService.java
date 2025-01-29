package com.realtimeticketing.service;

import com.realtimeticketing.cli.*;
import com.realtimeticketing.cli.utils.Logger;
import com.realtimeticketing.dto.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    TicketPool ticketPool;
    List<Thread> vendorThreads = new ArrayList<>();
    List<Thread> customerThreads = new ArrayList<>();
    Thread main;
    private static final AtomicInteger ticketCounter = new AtomicInteger(1);
    int status = 0;
    public NotificationService(SimpMessagingTemplate template) {
        this.messagingTemplate = template;
    }
    @PostConstruct
    public void init() {
        /*new Thread(() -> {
            int count = 0;
            while (true) {
                try {
                    System.out.println("Running");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.setSender(count%3==0 ?"Vendor" : count%3==1 ? "Customer" : "Ticket");
                count++;
                message.setContent(new Random().nextInt(100) + " - Random Message" );
                messagingTemplate.convertAndSend("/topic/messages",message);
            }
        }).start();*/

    }

    public void configure(Configuration config) {
        ticketPool = new TicketPool(config.getMaxTicketCapacity());
        if (status == 1) {
            status = 0;
        }
        if (main==null){
            main = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        System.out.print("> ");
                        try {
                            Thread.sleep(1000);
                        }catch (Exception e){

                        }
                        String command = status == 0 ? "s" : status == 1 ? "e" : "p";
                        switch (command) {
                            case "s":
                                if (vendorThreads.isEmpty() && customerThreads.isEmpty()) {
                                    status = 0;
                                    Logger.log("Starting vendors and customers...");
                                    messagingTemplate.convertAndSend("/topic/messages", new Message("System", "Starting vendors and customers..."));
                                    // Creating Vendor Threads
                                    for (int i = 1; i <= 3; i++) { // Simulating 3 vendors
                                        Thread vendorThread = new Thread(new Vendor("Vendor" + i, ticketPool, generateUniqueTickets(config.getTotalTickets()), config.getTicketReleaseRate() * 1000,messagingTemplate));
                                        vendorThreads.add(vendorThread);
                                        vendorThread.start();
                                    }
                                    // Create Customer threads
                                    for (int i = 1; i <= 5; i++) { // Simulating 5 customers
                                        Thread customerThread = new Thread(new Customer("Customer" + i, ticketPool, config.getCustomerRetrievalRate() * 1000,messagingTemplate));
                                        customerThreads.add(customerThread);
                                        customerThread.start();
                                    }
                                    System.out.println("Vendors and Customers started.");
                                    messagingTemplate.convertAndSend("/topic/messages", new Message("System", "Vendors and Customers started."));
                                } else {
                                    System.out.println("Threads are already running.");
                                    //messagingTemplate.convertAndSend("/topic/messages", new Message("System", "Threads are already running."));
                                }
                                break;
                            case "p":
                                Logger.log("all threads paused...");
                                //messagingTemplate.convertAndSend("/topic/messages", new Message("System", "Pausing all threads..."));
                                stopThreads(vendorThreads);
                                stopThreads(customerThreads);
                                vendorThreads.clear();
                                customerThreads.clear();
                                System.out.println("all threads paused...");
                                messagingTemplate.convertAndSend("/topic/messages", new Message("System", "All threads stopped."));
                                break;

                            case "e":
                                Logger.log("Exiting the application...");
                                messagingTemplate.convertAndSend("/topic/messages", new Message("System", "Exiting the application..."));
                                stopThreads(vendorThreads);
                                stopThreads(customerThreads);
                                System.out.println("Application exited.");
                                messagingTemplate.convertAndSend("/topic/messages", new Message("System", "Application exited."));
                                main=null;
                                return;
                        }

                    }
                };
            };
        }else {
            status = 0;
        }
        messagingTemplate.convertAndSend("/topic/messages", new Message("System", "System Configured with "+config.getTotalTickets()+" tickets and "+config.getMaxTicketCapacity()+" capacity."));
        /*Logger.log("System started.");
        System.out.println("System started.");
        messagingTemplate.convertAndSend("/topic/messages", "System started");*/
    }

    public void exit() {
        status = 1;
        messagingTemplate.convertAndSend("/topic/messages", new Message("System", "Exiting the application..."));
    }

    public void stop() {
        status = 2;
        messagingTemplate.convertAndSend("/topic/messages", new Message("System", "Pausing all threads..."));
    }

    private  void stopThreads(List<Thread> threads) {
        //messagingTemplate.convertAndSend("/topic/messages", new Message("System", "Stopping threads..."));
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

    public String getStatus() {
        return status == 0 ? "Running" : status == 1 ? "Stopped" : "Paused";
    }

    public void start() {
        messagingTemplate.convertAndSend("/topic/messages", new Message("System", "Starting the application..."));
        status = 0;
        if ((main!=null) && !main.isAlive()) {
            main.start();
        }
    }
}
