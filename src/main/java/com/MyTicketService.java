package main.java.com;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MyTicketService services a venue's seating arrangement.
 */
public class MyTicketService implements TicketService {

    private Venue v;

    private Map<Integer, SeatHold> seatHoldTracker;
    private Map<String, Confirmation> confirmationTracker;

    private IdGenerator seatHoldIdGenerator;
    private IdGenerator confirmCodeGenerator;

    private int holdExpirationTime;

    private static int DEFAUL_HOLD_EXPIRATION_TIME = 30;

    public MyTicketService(Venue v) throws TicketServiceException {
        this(v, 30); // default hold expiration time
    }

    public MyTicketService(Venue v, int holdExpirationTime) throws TicketServiceException {
        if (null == v) throw new TicketServiceException("Venue was null.");
        this.v = v;
        this.holdExpirationTime = (holdExpirationTime >= 0) ? holdExpirationTime : 30;

        seatHoldTracker = new HashMap<Integer, SeatHold>();
        confirmationTracker = new HashMap<String, Confirmation>();
        seatHoldIdGenerator = new SeatHoldIdGenerator();
        confirmCodeGenerator = new ConfirmationCodeGenerator();
    }

    public List<Seat> findOpenSeats() {
        throw new UnsupportedOperationException();
    }

    ///////////////////////////////////////////////////////////////////

    /**
     * The number of seats in the venue that are neither held nor reserved
     * @return the number of tickets available in the venue
     */
    public int numSeatsAvailable() {
        throw new UnsupportedOperationException();
    }
    /**
     * Find and hold the best available seats for a customer
     * @param numSeats the number of seats to find and hold
     * @param customerEmail unique identifier for the customer related information
     */
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        if (null == customerEmail || customerEmail.length() == 0) return null;
        if (numSeatsAvailable() < numSeats) return null;
        throw new UnsupportedOperationException();
    }
    /**
     * Commit seats held for a specific customer
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold
     *                     is assigned
     * @return a reservation confirmation code
     */
    public String reserveSeats(int seatHoldId, String customerEmail) {
        if (null == customerEmail || customerEmail.length() == 0) return null;
        throw new UnsupportedOperationException();
    }

}