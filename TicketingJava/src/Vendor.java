import utils.Logger;

import java.util.List;

public class Vendor implements Runnable{
    private final TicketPool ticketPool;
    private final String vendorId;
    private final List<Ticket> ticketsToProduce;
    private final int releaseInterval;     // Time interval between ticket releases (in milliseconds)

    public Vendor(String vendorId, TicketPool ticketPool, List<Ticket> ticketsToProduce, int releaseInterval){
        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.ticketsToProduce = ticketsToProduce;
        this.releaseInterval = releaseInterval;
    }
    @Override
    public void run() {
        for(Ticket ticket : ticketsToProduce){
            synchronized (ticketPool) {
                while (ticketPool.getPoolSize() >= ticketPool.getMaxCapacity()) {
                    Logger.log(vendorId + ": Pool is full. Waiting to add tickets...");
                    try {
                        ticketPool.wait(); // Waiting until the space become available in the pool
                    } catch (InterruptedException e) {
                        Logger.log(vendorId + " was interrupted while waiting to add tickets.");
                        return;
                    }
                }
                ticketPool.addTickets(List.of(ticket)); // Adding a single ticket
                Logger.log(vendorId + " added ticket. Current pool size is " + ticketPool.getPoolSize() + ": " + ticket);
                ticketPool.notifyAll(); // Notifying other threads waiting on the pool
            }
                // Simulating release interval
                try {
                    Thread.sleep(releaseInterval);
                } catch (InterruptedException e) {
                    Logger.log(vendorId + " was interrupted during sleep.");
                    return;
                }
        }
        Logger.log(vendorId + " has finished adding tickets.");
    }
}
