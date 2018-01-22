package main.java.com;

/**
 * Venue represents any general event with some seat arrangement.
 */
public class Venue {

    private Seats seats;

    public Venue(Seats seats) {
        this.seats = seats;
    }

    public Seats getSeats() {
        return seats;
    }
}
