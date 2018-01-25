package Venue.Seating;

/**
 * Seat is a single seat as part of a seat arrangement and
 * has only one state of occupancy at a given time.
 */
public class Seat {

    private int id;
    private SeatState ss;

    /**
     * Create a seat with an ID and open state
     * @param id unique identifier for seat
     */
    public Seat(int id) {
        this.id = id;
        ss = SeatState.OPEN;
    }

    public int getId() { return id; }

    public SeatState getSeatState() { return ss; }

    public void markOpen() { ss = SeatState.OPEN; }
    public void markHold() { ss = SeatState.HOLD; }
    public void markReserved() { ss = SeatState.RESERVED; }

}