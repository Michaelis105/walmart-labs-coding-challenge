package main.java.com.Venue;

/**
 * Seat's state
 * - OPEN: Available for occupancy/reservation or hold, can go to other states.
 * - RESERVED: Occupied, can go to open on cancel order only.
 * - HOLD: Temporary hold, can go to open on expire/cancel or reserved on reservation/purchase.
 */
public enum SeatState {
    OPEN, RESERVED, HOLD
}
