import utils.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a thread-safe pool of tickets with synchronized methods to add, remove,and manage tickets.
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
     * Adding a list of tickets to the pool.Waiting if the pool is full.
     * @param tickets This is the list of tickets to be added.
     */
    public synchronized void addTickets(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            while (ticketPool.size() >= maxCapacity) {
                try {
                    Logger.log("Pool is full. Waiting to add tickets...");
                    wait(); // Waiting until a space in the pool.
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
     * @return the removed ticket,or null if interrupted while waiting.
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
     * Getting the current size of the ticket pool.
     * @return the number of tickets currently in the pool.
     */
    public synchronized int getPoolSize() {
        return ticketPool.size();
    }

    /**
     * Getting the maximum capacity of the ticket pool.
     * @return the maximum capacity of the pool.
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Displaying all the  tickets currently in the pool.
     */
    public synchronized void displayTickets() {

        System.out.println("Current tickets in Pool: ");
        for (Ticket ticket : ticketPool) {
            System.out.println(ticket);
        }

    }
}
