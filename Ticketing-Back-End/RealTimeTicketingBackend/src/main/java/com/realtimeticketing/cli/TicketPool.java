package com.realtimeticketing.cli;

import com.realtimeticketing.cli.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A thread safe pool for managing tickets with a maximum capacity.
 * Provides synchronized methods for adding, removing, and displaying tickets.
 */
public class TicketPool {
    private List<Ticket> ticketPool;      // Thread-safe list to store tickets
    private final int maxCapacity;        // Maximum capacity of the pool

    // Constructors
    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.ticketPool = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Adding tickets to the pool. Waiting if the pool is full.
     * @param tickets This is the list of tickets to add.
     */
    public synchronized void addTickets(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            while (ticketPool.size() >= maxCapacity) {
                try {
                    Logger.log("Pool is full. Waiting to add tickets...");
                    wait(); // wait until there is space in the pool.
                } catch (InterruptedException e) {
                    Logger.log("Interrupted while waiting to add tickets.");
                    return;
                }
            }
            ticketPool.add(ticket);
            Logger.log("Added ticket: " + ticket);
            notifyAll(); // Notifying other threads
        }
    }

    /**
     * Removing a ticket from the pool. Waiting if no tickets are available.
     * @return the removed ticket, or null if interrupted.
     */
    public synchronized Ticket removeTicket() {

        while (ticketPool.isEmpty()) {
            try {
                Logger.log("No tickets available. Waiting for tickets...");
                wait(); // Waiting until tickets are added.
            } catch (InterruptedException e) {
                Logger.log("Interrupted while waiting to buy tickets...");
                return null;
            }
        }
        Ticket ticket = ticketPool.remove(0);
        Logger.log("Removed ticket: " + ticket);
        notifyAll();
        return ticket;
    }

    /**
     * Retrieving the current size of the pool.
     * @return the number of tickets currently in the pool.
     */
    public synchronized int getPoolSize() {
        return ticketPool.size();
    }

    /**
     * Retrieving the maximum capacity of the pool.
     * @return the maximum number of tickets the pool can hold.
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Displaying all tickets currently in the pool.
     */
    public synchronized void displayTickets() {
        System.out.println("Current tickets in Pool: ");
        for (Ticket ticket : ticketPool) {
            System.out.println(ticket);
        }

    }
}
