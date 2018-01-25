package Venue;

import Venue.Seating.Seater;

/**
 * Venue represents any general event.
 */
public class Venue {

    private Seater str;

    /**
     * Create venue.
     * For purpose of exercise, all venues should have seats.
     */
    public Venue(int row, int col) { str = new Seater(row, col); }

    /**
     * Gets seats
     * @return seats
     */
    public Seater getSeater() { return str; }

    /**
     * Some other service/class would call methods pertaining to venue.
     * Not important for exercise purposes.
     */
    public void partyTime() { /* Woo hoo. */ }
}
