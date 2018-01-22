package main.java.com;

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

    public synchronized void markOpen() { ss = SeatState.OPEN; }
    public synchronized void markHold() { ss = SeatState.HOLD; }
    public synchronized void markReserved() { ss = SeatState.RESERVED; }

}