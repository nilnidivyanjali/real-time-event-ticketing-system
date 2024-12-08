import utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {
    private List<Ticket> ticketPool;      // Thread-safe list to store tickets
    private final int maxCapacity;        // Maximum capacity of the pool

    // Constructors
    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.ticketPool = Collections.synchronizedList(new ArrayList<>());
    }

    // Method to add tickets to the pool
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

    // Method to remove a ticket from the pool
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

    // Method to get the current pool size
    public synchronized int getPoolSize() {
        return ticketPool.size();
    }

    // Method to get the pool's maximum capacity
    public int getMaxCapacity() {
        return maxCapacity;
    }

    // Method to display all tickets in the pool
    public synchronized void displayTickets() {
        System.out.println("Current tickets in Pool: ");
        for (Ticket ticket : ticketPool) {
            System.out.println(ticket);
        }

    }
}
