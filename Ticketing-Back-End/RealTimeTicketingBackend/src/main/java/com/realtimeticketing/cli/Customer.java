package com.realtimeticketing.cli;

import com.realtimeticketing.cli.utils.Logger;
import com.realtimeticketing.dto.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Represents a customer that attempts to purchase tickets from the ticket pool.
 * Implements Runnable to support multithreaded operation.
 */
public class Customer implements Runnable {

    private final TicketPool ticketPool;
    private final String customerId;
    private final int retrievalInterval; // Time interval between ticket purchases
    private SimpMessagingTemplate simpMessagingTemplate;


    public Customer(String customerId, TicketPool ticketPool, int retrievalInterval){

        this.customerId = customerId;
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
    }

    public Customer(String customerId, TicketPool ticketPool, int retrievalInterval, SimpMessagingTemplate simpMessagingTemplate){

        this(customerId, ticketPool, retrievalInterval);
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /**
     * Continuously attempts to purchase tickets from the ticket pool.
     * Waits if the pool is empty and sends updates through WebSocket.
     */
    @Override
    public void run(){
        while (true){ // Simulating continuous ticket purchase attempts
            synchronized (ticketPool){

                while (ticketPool.getPoolSize() == 0) {

                    Logger.log( customerId + " No tickets available. Waiting...");
                    simpMessagingTemplate.convertAndSend("/topic/messages", new Message("System","No tickets available. Waiting..."));
                    try {

                        ticketPool.wait(); // Waiting for tickets to become available
                    } catch (InterruptedException e) {

                        Logger.log(customerId + " was interrupted while waiting to buy tickets.");
                        simpMessagingTemplate.convertAndSend("/topic/messages", new Message("System",customerId + " was interrupted while waiting to buy tickets."));
                        return;
                    }
                }
                // Buying  a ticket
                Ticket ticket = ticketPool.removeTicket();

                if (ticket != null) {

                    Logger.log("Customer " + customerId + " bought ticket. Current pool size is " + ticketPool.getPoolSize() + ": " + ticket);
                    simpMessagingTemplate.convertAndSend("/topic/messages", new Message("System","Customer " + customerId + " bought ticket. Current pool size is " + ticketPool.getPoolSize() + ": " + ticket));
                }
                ticketPool.notifyAll(); //Notifying other threads waiting in the pool
            }

            // Simulating retrieval interval
            try{
                Thread.sleep(retrievalInterval);
            }
            catch (InterruptedException e){
                Logger.log( customerId + " was interrupted during sleep.");
                simpMessagingTemplate.convertAndSend("/topic/messages", new Message("System",customerId + " was interrupted during sleep."));
                return;
            }

        }
    }
}
