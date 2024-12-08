import utils.Logger;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final String customerId;
    private final int retrievalInterval; // Time interval between ticket purchases

    public Customer(String customerId, TicketPool ticketPool, int retrievalInterval){
        this.customerId = customerId;
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
    }

    @Override
    public void run(){
        while (true){ // Simulating continuous ticket purchase attempts
            synchronized (ticketPool){
                while (ticketPool.getPoolSize() == 0) {
                    Logger.log( customerId + " No tickets available. Waiting...");
                    try {
                        ticketPool.wait(); // Waiting for tickets to become available
                    } catch (InterruptedException e) {
                        Logger.log(customerId + " was interrupted while waiting to buy tickets.");
                        return;
                    }
                }
                // Buying  a ticket
                Ticket ticket = ticketPool.removeTicket();
                if (ticket != null) {
                    Logger.log("Customer " + customerId + " bought ticket. Current pool size is " + ticketPool.getPoolSize() + ": " + ticket);
                }
                ticketPool.notifyAll(); //Notifying other threads waiting in the pool
            }

            // Simulating retrieval interval
            try{
                Thread.sleep(retrievalInterval);
            }
            catch (InterruptedException e){
                Logger.log( customerId + " was interrupted during sleep.");
                return;
            }

        }
    }
}
