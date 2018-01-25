package java.com.Venue.Seating;

/**
 * Seat's state
 * - OPEN: Available for occupancy/reservation or hold, can go to other states.
 * - RESERVED: Occupied, can go to open on cancel order only.
 * - HOLD: Temporary hold, can go to open on expire/cancel or reserved on reservation/purchase.
 */
public enum SeatState {
    OPEN, RESERVED, HOLD

    // Need easier add easier way for other classes to validate a potentially unknown state to list of valid states.
}
