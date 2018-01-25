package Venue.Seating;

import org.junit.Assert;
import org.junit.Test;

public class SeatTest {

    @Test
    public void Basic() {
        Seat s1 = new Seat(Integer.MIN_VALUE);
        Seat s2 = new Seat(-1);
        Seat s3 = new Seat(0);
        Seat s4 = new Seat(1);
        Seat s5 = new Seat(11);
        Seat s6 = new Seat(123);
        Seat s7 = new Seat(Integer.MAX_VALUE);

        // Initialized seats have ID as assigned to them.
        Assert.assertEquals(s1.getId(), Integer.MIN_VALUE);
        Assert.assertEquals(s2.getId(), -1);
        Assert.assertEquals(s3.getId(), 0);
        Assert.assertEquals(s4.getId(), 1);
        Assert.assertEquals(s5.getId(), 11);
        Assert.assertEquals(s6.getId(), 123);
        Assert.assertEquals(s7.getId(), Integer.MAX_VALUE);

        // Initialized seats start with open seat state.
        Assert.assertEquals(s1.getSeatState(), SeatState.OPEN);
        Assert.assertEquals(s3.getSeatState(), SeatState.OPEN);
        Assert.assertEquals(s7.getSeatState(), SeatState.OPEN);
    }

    @Test
    public void MarkSeats() {
        Seat s1 = new Seat(1);
        Seat s2 = new Seat(2);
        Seat s3 = new Seat(3);

        s1.markHold();
        s1.markOpen();
        s2.markHold();
        s3.markReserved();

        // Marked seats should have updated seat state
        Assert.assertEquals(s1.getSeatState(), SeatState.OPEN);
        Assert.assertEquals(s2.getSeatState(), SeatState.HOLD);
        Assert.assertEquals(s3.getSeatState(), SeatState.RESERVED);

    }

}
