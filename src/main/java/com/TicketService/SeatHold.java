package main.java.com.TicketService;

import main.java.com.Venue.Seating.Seat;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * SeatHold is a collection of seat(s) for which a customer has a hold or reservation.
 */
public class SeatHold {

    private final int seatHoldId;
    private final String customerEmail;
    private List<Seat> seatsHold;
    private Date createDate;

    public SeatHold(int seatHoldId, String customerEmail, List<Seat> seatsHold) throws TicketServiceException {
        this.seatHoldId = seatHoldId;
        this.customerEmail = customerEmail;
        this.seatsHold = new LinkedList<Seat>(seatsHold);
        createDate = new Date(); // Time of holding and seat hold creation date
                                 // will differ depending on how fast seatsHold is populated
    }

    /**
     * Gets id associated with seat hold.
     * @return seat hold ID
     */
    public int getSeatHoldId() { return seatHoldId; }

    /**
     * Gets group of seat(s).
     * @return group of seat(s)
     */
    public List<Seat> getSeatsHold() { return seatsHold; }

    /**
     * Gets customer email associated with seat hold.
     * @return customer email
     */
    public String getCustomerEmail() { return customerEmail; }

    /**
     * Gets creation date of seat hold.
     * @return create date
     */
    public Date getCreateDate() { return createDate; }

}
