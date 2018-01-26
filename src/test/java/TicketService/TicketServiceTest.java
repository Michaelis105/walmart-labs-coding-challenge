package TicketService;

import Venue.Seating.Seat;
import Venue.Seating.SeatState;
import org.junit.Assert;
import org.junit.Test;

import Venue.Venue;

import static java.lang.Thread.sleep;

public class TicketServiceTest {

    @Test(expected = TicketServiceException.class)
    public void TicketServiceNull() throws TicketServiceException {
        new MyTicketService(null, 20);
    }

    @Test
    public void FindNextBestSeat() throws TicketServiceException {
        Venue v1 = new Venue(2,2);
        MyTicketService mts1 = new MyTicketService(v1, 20);
        Seat s1 = mts1.findNextBestSeat();
        Seat s2 = mts1.findNextBestSeat();
        Seat s3 = mts1.findNextBestSeat();
        Seat s4 = mts1.findNextBestSeat();

        // Ensure the same seat is not found.
        Assert.assertTrue(s1 != s2 && s1 != s3 && s1 != s4 &&
        s2 != s3 && s2 != s4 && s3 != s4);
    }

    @Test
    public void FindNextBestSeatOutOfSeats() throws TicketServiceException {
        Venue v1 = new Venue(1,1);
        MyTicketService mts1 = new MyTicketService(v1, 20);
        Seat s1 = mts1.findNextBestSeat();
        Seat s2 = mts1.findNextBestSeat();

        Assert.assertNotNull(s1);
        Assert.assertNull(s2); // out of seats
    }

    // NOTE: Running removeExpiredSeatHolds() should only be called by
    // a cron job, batch process, or equivalent function.
    @Test
    public void SimulateRemoveExpiredSeatHoldsBatchProcessing() throws TicketServiceException, InterruptedException {
        Venue v1 = new Venue(5,5);
        MyTicketService mts1 = new MyTicketService(v1, 4);
        String simpleEmail = "anne.bobby@gmail.com";
        int shid1 = mts1.findAndHoldSeatsWrap(4, simpleEmail);
        int shid2 = mts1.findAndHoldSeatsWrap(5, simpleEmail);
        int shid3 = mts1.findAndHoldSeatsWrap(6, simpleEmail);
        int shid4 = mts1.findAndHoldSeatsWrap(7, simpleEmail);
        mts1.removeExpiredSeatHolds();
        String conf1 = mts1.reserveSeats(shid1, simpleEmail);
        String conf2 = mts1.reserveSeats(shid2, simpleEmail);
        Assert.assertNotNull(conf1);
        Assert.assertNotNull(conf2);
        sleep(5000);
        mts1.removeExpiredSeatHolds();
        String conf3 = mts1.reserveSeats(shid3, simpleEmail);
        String conf4 = mts1.reserveSeats(shid4, simpleEmail);
        Assert.assertNull(conf3);
        Assert.assertNull(conf4);
        // TODO: Check it works, ConcurrentModificationException
    }

    @Test
    public void Emails() {
        // out-of-scope of exercise
    }

    @Test
    public void NumSeatsAvailableAndFindAndHoldSeats() throws TicketServiceException {
        Venue v1 = new Venue(5,5);
        MyTicketService mts1 = new MyTicketService(v1, 1000);
        Assert.assertEquals(25, mts1.numSeatsAvailable()); // All available

        String simpleEmail = "anne.bobby@gmail.com";
        mts1.findAndHoldSeatsWrap(1, simpleEmail);
        Assert.assertEquals(24, mts1.numSeatsAvailable()); // -1
        mts1.findAndHoldSeatsWrap(2, simpleEmail);
        Assert.assertEquals(22, mts1.numSeatsAvailable()); // -3
        mts1.findAndHoldSeatsWrap(10, simpleEmail);
        Assert.assertEquals(12, mts1.numSeatsAvailable()); // -13
        mts1.findAndHoldSeatsWrap(12, simpleEmail);
        Assert.assertEquals(0, mts1.numSeatsAvailable()); // -25

        // TODO: Avail not decrementing
    }

    @Test
    public void findAndHoldSeatsBadArgs() throws TicketServiceException {
        Venue v1 = new Venue(5,5);
        MyTicketService mts1 = new MyTicketService(v1, 1000);
        String simpleEmail = "anne.bobby@gmail.com";

        // Bad email
        Assert.assertTrue(mts1.findAndHoldSeatsWrap(2, "l@$)$@@@mfe.femiof.feiom") == -1);
        Assert.assertTrue(mts1.findAndHoldSeatsWrap(90000, simpleEmail) == -1);
    }

    @Test
    public void ReserveSeats() throws TicketServiceException {
        Venue v1 = new Venue(5,5);
        MyTicketService mts1 = new MyTicketService(v1, 1000);

        // Staging to hold
        String simpleEmail = "anne.bobby@gmail.com";
        int shid1 = mts1.findAndHoldSeatsWrap(4, simpleEmail);
        int shid2 = mts1.findAndHoldSeatsWrap(5, simpleEmail);
        int shid3 = mts1.findAndHoldSeatsWrap(6, simpleEmail);
        int shid4 = mts1.findAndHoldSeatsWrap(7, simpleEmail);
        int shid5 = mts1.findAndHoldSeatsWrap(3, simpleEmail);
        Assert.assertEquals(0, v1.getSeater().getNumOpenSeats());
        Assert.assertEquals(25, v1.getSeater().getNumHoldSeats());
        Assert.assertEquals(0, v1.getSeater().getNumReservedSeats());

        // Reserve
        Assert.assertNotNull(mts1.reserveSeats(shid1, simpleEmail));
        Assert.assertEquals(0, v1.getSeater().getNumOpenSeats());
        Assert.assertEquals(21, v1.getSeater().getNumHoldSeats());
        Assert.assertEquals(4, v1.getSeater().getNumReservedSeats());
        Assert.assertNotNull(mts1.reserveSeats(shid2, simpleEmail));
        Assert.assertNotNull(mts1.reserveSeats(shid3, simpleEmail));
        Assert.assertNotNull(mts1.reserveSeats(shid4, simpleEmail));
        Assert.assertNotNull(mts1.reserveSeats(shid5, simpleEmail));
        Assert.assertEquals(0, v1.getSeater().getNumOpenSeats());
        Assert.assertEquals(0, v1.getSeater().getNumHoldSeats());
        Assert.assertEquals(25, v1.getSeater().getNumReservedSeats());
    }

    @Test
    public void ReserveSeatsBadArgs() throws TicketServiceException {
        Venue v1 = new Venue(5,5);
        MyTicketService mts1 = new MyTicketService(v1, 1000);
        String simpleEmail = "anne.bobby@gmail.com";
        int shid1 = mts1.findAndHoldSeatsWrap(4, simpleEmail);

        // Bad id
        Assert.assertNull(mts1.reserveSeats(99999999, simpleEmail));

        // Bad email
        Assert.assertNull(mts1.reserveSeats(shid1, "asd=++@@f@.....lol"));

        // Wrong email
        Assert.assertNull(mts1.reserveSeats(shid1, "anne.charlie@gmail.com"));

        // Verify seats are still held.
        Assert.assertEquals(21, v1.getSeater().getNumOpenSeats());
        Assert.assertEquals(4, v1.getSeater().getNumHoldSeats());
        Assert.assertEquals(0, v1.getSeater().getNumReservedSeats());
        for (int i = 0; i < v1.getSeater().getSeats().getRowLength(); i++) {
            Assert.assertEquals(SeatState.HOLD, v1.getSeater().getSeats().getSeat(0, i));
        }
    }
}
