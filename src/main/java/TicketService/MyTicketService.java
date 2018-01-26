package TicketService;

import Venue.Seating.SeatState;
import Venue.Seating.Seats;
import Venue.Venue;
import Venue.Seating.Seat;

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
        this(v, DEFAULT_HOLD_EXPIRATION_TIME);
    }

    public MyTicketService(Venue v, int holdExpirationTime) throws TicketServiceException {
        if (null == v) throw new TicketServiceException("Venue was null.");
        this.v = v;
        this.holdExpirationTime = (holdExpirationTime >= 0) ? holdExpirationTime : DEFAULT_HOLD_EXPIRATION_TIME;

        seatHoldTracker = new HashMap<Integer, SeatHold>();
        confirmationTracker = new HashMap<String, SeatHold>();
        seatHoldIdGenerator = new SeatHoldIdGenerator();
        confirmCodeGenerator = new ConfirmationCodeGenerator();
    }

    /**
     * Finds next best open seat in venue seating by searching
     * seats from best (front-left) to worst (rear-right).
     * Very inefficient search, O(N*M) where N = rows and M = columns
     * @return next best seat
     */
    public Seat findNextBestSeat() {
        if (numSeatsAvailable() == 0) return null; // Insufficient seats
        final Seats seats = v.getSeater().getSeats();
        for (int i = 0; i < seats.getRowLength(); i++) {
            for (int j = 0; j < seats.getColumnLength(); j++) {
                final Seat s = seats.getSeat(i, j);
                if (SeatState.OPEN.equals(s.getSeatState())) {
                    try {
                        v.getSeater().processSeat(s, SeatState.SYS_HOLD);
                    } catch (Exception e) {
                        // Error marking seat as system hold. Not sure what case causes this to happen.
                        // Need to investigate.
                    }
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * Removes expired seat holds.
     * If ticket service run on a web server of a sort, this method would
     * run on a 'regular' basis (once every minute) as a batch process.
     */
    public void removeExpiredSeatHolds() {
        Date cur = new Date();
        for (Map.Entry<Integer, SeatHold> entry : seatHoldTracker.entrySet()) {
            SeatHold sh = entry.getValue();
            long duration = cur.getTime() - sh.getCreateDate().getTime();
            if (duration >= holdExpirationTime) {
                try { v.getSeater().processSeats(sh.getSeatsHold(), SeatState.OPEN); }
                catch (Exception e) { /* Error "releasing" held seats. */ }
                seatHoldTracker.remove(sh.getSeatHoldId(), sh);
            }
        }
    }

    /**
     * Marks all given seats open. Used by removeExpiredSeatHolds().
     * @param seats seats to be marked open
     */
    public void markSeatsOpen(List<Seat> seats) throws TicketServiceException {
        if (null == seats) throw new TicketServiceException("Cannot mark null seats!");
        if (seats != null) {
            try { v.getSeater().processSeats(seats, SeatState.OPEN);}
            catch (Exception e) { /* Error "releasing" held seats. */ }
        }
    }

    /**
     * Determines if email is valid email.
     * For purpose of exercise, borrowed from link:
     * https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
     * @param email email to validate
     * @return true if valid email, false otherwise
     */
    public boolean isValidEmail(final String email) { return Pattern.compile(EMAIL_PATTERN).matcher(email).matches(); }

    /**
     * Wrapper to actual findAndHoldSeats(). Caller should not handle seatHold
     * object directly; rather, it should receive and handle its ID.
     * @param numSeats      the number of seats to find and hold
     * @param customerEmail unique identifier for the customer related information
     * @return seatHold id
     */
    public int findAndHoldSeatsWrap(int numSeats, String customerEmail) {
        SeatHold sh = findAndHoldSeats(numSeats, customerEmail);
        if (sh != null) {
            seatHoldTracker.put(sh.getSeatHoldId(), sh);
            return sh.getSeatHoldId();
        } else return -1;
    }

    /////////////////////////////////////////////////////////////////////////////////

    /**
     * The number of seats in the venue that are neither held nor reserved.
     * Race condition in count may be acceptable in some cases.
     * @return the number of tickets/seats available in the venue
     */
    public int numSeatsAvailable() {
        return v.getSeater().getNumOpenSeats();
    }

    /**
     * Find and hold the best available seats for a customer.
     * Synchronizing leads to performance bottleneck such that only one thread can find seats and mark them as held at
     * a time. This is to avoid multiple threads retrieving and marking the same seats resulting in multiple seats being
     * held by multiple customers.
     * Use findAndHoldSeatsWrap() instead!
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
                try { v.getSeater().processSeats(seats, SeatState.OPEN); }
                catch (Exception e) { /* Error attempting to clean up corrupted seat hold. */ }
                return null;
            }
            seats.add(s);
        }

        try { v.getSeater().processSeats(seats, SeatState.HOLD); }
        catch (Exception e) {
            /* Error attempting to hold seats. Inform caller of holding error. */
            try { v.getSeater().processSeats(seats, SeatState.OPEN); }
            catch (Exception ex) { /* Error attempting to clean up corrupted seat hold. */ }
        }

        int seatHoldId = Integer.valueOf(seatHoldIdGenerator.generateId());
        SeatHold sh;
        try {
            sh = new SeatHold(seatHoldId, customerEmail, seats);
        } catch (TicketServiceException e) {
            // Issue creating seatHold (empty seats list already checked handled above, error should not occur)
            try { v.getSeater().processSeats(seats, SeatState.OPEN); }
            catch (Exception ex) { /* Error attempting to clean up corrupted seat hold. */ }
            return null;
        }

        return sh;
    }

    /**
     * Commit seats held for a specific customer
     * @pre   seatHoldId must be stored in seatHoldTracker
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

        try { v.getSeater().processSeats(sh.getSeatsHold(), SeatState.RESERVED); }
        catch (Exception e) {
            /* Error attempting to reserve seat hold. Inform caller of reservation error */
            try { v.getSeater().processSeats(sh.getSeatsHold(), SeatState.HOLD); }
            catch (Exception ex) { /* Error attempting to reverse corrupted seat hold. */ }
        }

        String newConfirmationCode = confirmCodeGenerator.generateId();
        confirmationTracker.put(newConfirmationCode, sh);
        seatHoldTracker.remove(seatHoldId, sh);
        return newConfirmationCode;
    }

}