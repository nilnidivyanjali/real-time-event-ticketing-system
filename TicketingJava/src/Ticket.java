public class Ticket {
    private String ticketId;
    private String eventName;
    private double ticketPrice;


    // Constructors
    public Ticket(String ticketId, String eventName, double ticketPrice) {
        this.ticketId = ticketId;
        this.eventName = eventName;
        this.ticketPrice = ticketPrice;

    }

    // Getters
    public String getTicketId() {
        return ticketId;
    }

    public String getEventName() {
        return eventName;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }



    // Setters
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setTicketPrice(double ticketPrice) {
        if (ticketPrice < 0) {
            throw new IllegalArgumentException("Ticket price cannot be negative.");
        }
        this.ticketPrice = ticketPrice;
    }



    // Displaying ticket details
    @Override
    public String toString() {
        return "Ticket Details:\n"+
                "Ticket ID: " + ticketId + "\n" +
                "Event Name: " + eventName + "\n" +
                "Ticket Price: $" + ticketPrice + "\n";

    }

}

