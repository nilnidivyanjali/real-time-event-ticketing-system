package com.realtimeticketing.cli;


/**
 * Represents a vendor that produces tickets and adds them to the shared ticket pool.
 * Supports real time messaging using WebSocket through SimpMessagingTemplate.
 */
import com.realtimeticketing.cli.utils.Logger;
import com.realtimeticketing.dto.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class Vendor implements Runnable{

    private final TicketPool ticketPool;
    private final String vendorId;
    private final List<Ticket> ticketsToProduce;
    private final int releaseInterval;     // Time interval between ticket releases (in milliseconds)
    private SimpMessagingTemplate template;


    public Vendor(String vendorId, TicketPool ticketPool, List<Ticket> ticketsToProduce, int releaseInterval){

        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.ticketsToProduce = ticketsToProduce;
        this.releaseInterval = releaseInterval;
    }

    public Vendor(String vendorId, TicketPool ticketPool, List<Ticket> ticketsToProduce, int releaseInterval, SimpMessagingTemplate template){

        this(vendorId, ticketPool, ticketsToProduce, releaseInterval);
        this.template = template;
    }

    /**
     * Produces tickets and adds them to the ticket pool, ensuring thread safety and handling pool capacity.
     * Sends real time updates through WebSocket to notify the system of the vendor's actions.
     */
    @Override
    public void run() {

        for(Ticket ticket : ticketsToProduce){

            synchronized (ticketPool) {

                while (ticketPool.getPoolSize() >= ticketPool.getMaxCapacity()) {

                    Logger.log(vendorId + ": Pool is full. Waiting to add tickets...");
                    template.convertAndSend("/topic/messages", new Message("System", vendorId + ": Pool is full. Waiting to add tickets..."));

                    try {
                        ticketPool.wait(); // Waiting until the space become available in the pool
                    } catch (InterruptedException e) {

                        Logger.log(vendorId + " was interrupted while waiting to add tickets.");
                        template.convertAndSend("/topic/messages", new Message("System", vendorId + " was interrupted while waiting to add tickets."));
                        return;
                    }
                }

                ticketPool.addTickets(List.of(ticket)); // Adding a single ticket
                Logger.log(vendorId + " added ticket. Current pool size is " + ticketPool.getPoolSize() + ": " + ticket);
                template.convertAndSend("/topic/messages", new Message("System", vendorId + " added ticket. Current pool size is " + ticketPool.getPoolSize() + ": " + ticket));
                ticketPool.notifyAll(); // Notifying other threads waiting on the pool
            }
                // Simulating release interval
                try {
                    Thread.sleep(releaseInterval);
                } catch (InterruptedException e) {
                    Logger.log(vendorId + " was interrupted during sleep.");
                    template.convertAndSend("/topic/messages", new Message("System", vendorId + " was interrupted during sleep."));
                    return;
                }
        }

        Logger.log(vendorId + " has finished adding tickets.");
        template.convertAndSend("/topic/messages", new Message("System", vendorId + " has finished adding tickets."));
    }
}
