package Venue.Seating;

import java.util.List;

/**
 * Seater facilitates seating of seats
 */
public class Seater {

    private Seats seats;
    private int numOpenSeats;
    private int numHoldSeats;
    private int numReservedSeats;

    public Seater(int row, int col) {
        if (row <= 0) row = 1;
        if (col <= 0) col = 1;
        seats = new Seats(row, col);
        numOpenSeats = row * col;
        numHoldSeats = 0;
        numReservedSeats = 0;
    }

    /**
     * Gets seats for accessing purposes only.
     * Important that callers do not modify seats directly.
     * @return seats
     */
    public Seats getSeats() { return seats; }

    public int getNumOpenSeats() { return numOpenSeats; }
    public int getNumHoldSeats() { return numHoldSeats; }
    public int getNumReservedSeats() { return numReservedSeats; }

    /**
     * Change the state of given seats to some state and update counters.
     * @param s list of seats to change state
     * @param ss seat state to change to
     * @throws Exception
     */
    public void processSeats(List<Seat> s, SeatState ss) throws Exception {
        if (null == s || s.size() == 0) throw new IllegalArgumentException("Seats were null or empty.");
        if (null == ss) throw new IllegalArgumentException("Seat state to save is undefined.");
        for (Seat se : s) {
            SeatState curState = se.getSeatState();
            if (curState.equals(ss)) continue; // Possible race condition, avoid miscounting.

            // Decrement counter of corresponding from state.
            switch(curState) {
                case OPEN:
                    numOpenSeats--;
                    break;
                case HOLD:
                    numHoldSeats--;
                    break;
                case RESERVED:
                    numReservedSeats--;
                    break;
                default: // Unrecognized current state.
            }

            // Increment counter of corresponding new state and update state.
            switch(se.getSeatState()) {
                case OPEN:
                    se.markOpen();
                    break;
                case HOLD:
                    se.markHold();
                    break;
                case RESERVED:
                    se.markReserved();
                    break;
                default: // Unrecognized current state.
            }
        }
    }
}
