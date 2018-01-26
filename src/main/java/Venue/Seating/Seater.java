package Venue.Seating;

import java.util.LinkedList;
import java.util.List;

/**
 * Seater facilitates seating of seats.
 */
public class Seater {

    private Seats seats;
    private int numOpenSeats;
    private int numHoldSeats;
    private int numReservedSeats;
    private int capacity;

    public Seater(int row, int col) {
        if (row <= 0) row = 1;
        if (col <= 0) col = 1;
        seats = new Seats(row, col);
        numOpenSeats = row * col;
        numHoldSeats = 0;
        numReservedSeats = 0;
        capacity = row * col;
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
     * Change the state of a given seat to some state and update counters.
     * @param s list of seats to change state
     * @param ss seat state to change to
     * @throws Exception
     */
    public synchronized void processSeat(Seat s, SeatState ss) throws Exception {
        List<Seat> sl = new LinkedList<Seat>();
        sl.add(s);
        processSeats(sl, ss);
    }

    /**
     * Change the state of given seats to some state and update counters.
     * @param s list of seats to change state
     * @param ss seat state to change to
     * @throws Exception
     */
    public synchronized void processSeats(List<Seat> s, SeatState ss) throws Exception {
        if (null == s || s.size() == 0) throw new IllegalArgumentException("Seats were null or empty.");
        if (null == ss) throw new IllegalArgumentException("Seat state to save is undefined.");
        for (Seat se : s) {
            SeatState curState = se.getSeatState();
            if (curState.equals(ss)) continue; // Possible race condition, avoid miscounting.

            // Decrement counter of corresponding from state.
            switch (curState) {
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
            switch (ss) {
                case OPEN:
                    se.markOpen();
                    numOpenSeats++;
                    break;
                case HOLD:
                    se.markHold();
                    numHoldSeats++;
                    break;
                case RESERVED:
                    se.markReserved();
                    numReservedSeats++;
                    break;
                case SYS_HOLD:
                    se.markSysHold();
                default: // Unrecognized current state.
            }

            if (numOpenSeats < 0  || numOpenSeats > capacity ||
                    numHoldSeats < 0 || numHoldSeats > capacity ||
                    numReservedSeats < 0 || numReservedSeats > capacity) {
                // Race condition
            }
        }
    }
}
