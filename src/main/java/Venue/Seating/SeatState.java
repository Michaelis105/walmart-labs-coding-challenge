package Venue.Seating;

/**
 * Seat's state
 * - OPEN: Available for occupancy/reservation or hold, can go to other states.
 * - RESERVED: Occupied, can go to open on cancel order only.
 * - HOLD: Temporary hold, can go to open on expire/cancel or reserved on reservation/purchase.
 * - SYS_HOLD: Temporary hold, system is currently holding seat to transition to other valid state.
 */
public enum SeatState {
    OPEN, RESERVED, HOLD, SYS_HOLD

    // Need easier add easier way for other classes to validate a potentially unknown state to list of valid states.
}
