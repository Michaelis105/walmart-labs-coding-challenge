package main.java.com.Venue;

/**
 * Venue represents any general event.
 */
public class Venue {

    private Seats seats;

    /**
     * Create venue without seats.
     * For purpose of exercise, all venues should have seats.
     */
    public Venue() { this(null); }

    /**
     * Create venue with seats.
     * @param seats
     */
    public Venue(Seats seats) { this.seats = seats; }

    /**
     * Gets seats
     * @return seats
     */
    public Seats getSeats() { return seats; }

    /**
     * Some other service/class would call methods pertaining to venue.
     * Not important for exercise purposes.
     */
    public void partyTime() { /* Woo hoo. */ }
}
