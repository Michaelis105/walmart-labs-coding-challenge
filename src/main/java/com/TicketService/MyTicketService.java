package main.java.com.TicketService;

import main.java.com.Venue.Seat;
import main.java.com.Venue.Venue;

import java.util.*;
import java.util.regex.Pattern;

/**
 * MyTicketService services a venue's seating arrangement.
 */
public class MyTicketService implements TicketService {

    private Venue v;

    private Map<Integer, SeatHold> seatHoldTracker;
    private Map<String, SeatHold> confirmationTracker;

    private static IdGenerator seatHoldIdGenerator;
    private static IdGenerator confirmCodeGenerator;

    private int holdExpirationTime;

    private static int DEFAULT_HOLD_EXPIRATION_TIME = 30;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[_A-Za-z]{2,})$";

    public MyTicketService(Venue v) throws TicketServiceException {
        this(v, 30); // default hold expiration time
    }

    public MyTicketService(Venue v, int holdExpirationTime) throws TicketServiceException {
        if (null == v) throw new TicketServiceException("Venue was null.");
        this.v = v;
        this.holdExpirationTime = (holdExpirationTime >= 0) ? holdExpirationTime : 30;

        seatHoldTracker = new HashMap<Integer, SeatHold>();
        confirmationTracker = new HashMap<String, SeatHold>();
        seatHoldIdGenerator = new SeatHoldIdGenerator();
        confirmCodeGenerator = new ConfirmationCodeGenerator();
    }

    /**
     * Finds next best seat in venue seating
     * @return next best seat
     */
    public Seat findNextBestSeat() {
        // TODO: Implement method
        throw new UnsupportedOperationException();
    }

    /**
     * Removes expired seat holds.
     * If ticket service run on a web server of a sort, this method would
     * run on a 'regular' basis (once every minute) as a batch process.
     */
    public void removeExpiredSeatHolds() {
        // TODO: Implement method
        // get current time
        // iterate through all seat holds
        // if different between current time and seat hold time is greater than DEFAULT_HOLD_EXPIRATION_TIME
        // then open all
        throw new UnsupportedOperationException();
    }

    /**
     * Marks all given seats open. Used by removeExpiredSeatHolds().
     * @param seats seats to be marked open
     */
    public void markSeatsOpen(List<Seat> seats) {
        if (seats != null) for (Seat s : seats) s.markOpen();
    }

    /**
     * Determines if email is valid email.
     * For purpose of exercise, borrowed from link:
     * https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
     * @param email email to validate
     * @return true if valid email, false otherwise
     */
    public boolean isValidEmail(final String email) { return Pattern.compile(EMAIL_PATTERN).matcher(email).matches(); }

    /////////////////////////////////////////////////////////////////////////////////

    /**
     * The number of seats in the venue that are neither held nor reserved.
     * @return the number of tickets/seats available in the venue
     */
    public int numSeatsAvailable() {
        return v.getSeats().getNumOpenSeats();
    }

    /**
     * Find and hold the best available seats for a customer.
     * Synchronizing leads to performance bottleneck such that only one thread can find seats and mark them as held at
     * a time. This is to avoid multiple threads retrieving and marking the same seats resulting in multiple seats being
     * held by multiple customers.
     * @param numSeats      the number of seats to find and hold
     * @param customerEmail unique identifier for the customer related information
     */
    public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        if (!isValidEmail(customerEmail)) return null; // Invalid customer email
        if (numSeatsAvailable() < numSeats) return null; // Insufficient seats

        // Hold numSeats seats
        List<Seat> seats = new LinkedList<Seat>();
        for (int i = 0; i < numSeats; i++) {
            Seat s = findNextBestSeat();
            if (null == s) {
                // Already checked sufficient seats but still ran out, possible race condition
                markSeatsOpen(seats);
                return null;
            }
            s.markHold();
            seats.add(s);
        }

        int seatHoldId = Integer.valueOf(seatHoldIdGenerator.generateId());
        SeatHold sh;
        try {
            sh = new SeatHold(seatHoldId, customerEmail, seats);
        } catch (TicketServiceException e) {
            // Issue creating seatHold (empty seats list already checked handled above, error should not occur)
            markSeatsOpen(seats);
            return null;
        }
        seatHoldTracker.put(seatHoldId, sh);
        return sh;

    }

    /**
     * Commit seats held for a specific customer
     * @param seatHoldId    the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold
     *                      is assigned
     * @return a reservation confirmation code
     */
    public String reserveSeats(int seatHoldId, String customerEmail) {
        if (!isValidEmail(customerEmail)) return null; // Invalid customer email
        SeatHold sh = seatHoldTracker.get(seatHoldId);
        if (null == sh) return null; // seatHold not found, either inadvertently removed from system or invalid seatHoldId
        if (!sh.getCustomerEmail().equals(customerEmail)) return null; // Incorrect email to seatHold

        for (Seat s : sh.getSeatsHold()) s.markReserved();

        String newConfirmationCode = confirmCodeGenerator.generateId();
        confirmationTracker.put(newConfirmationCode, sh);
        seatHoldTracker.remove(seatHoldId, sh);
        return newConfirmationCode;
    }

}