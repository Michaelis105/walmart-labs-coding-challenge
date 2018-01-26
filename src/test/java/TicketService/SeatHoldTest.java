package TicketService;

import Venue.Seating.Seat;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SeatHoldTest {

    @Test
    public void Basic() throws TicketServiceException {
        List<Seat> s = new LinkedList<Seat>(Arrays.asList(new Seat(1), new Seat(2), new Seat(3)));
        SeatHold s1 = new SeatHold(0, "abc.efg@hijk.com", s);
        Date d = new Date();
        Assert.assertTrue(s1.getCreateDate().getTime()-d.getTime() <= 1);
        Assert.assertEquals("abc.efg@hijk.com", s1.getCustomerEmail());
        Assert.assertEquals(0, s1.getSeatHoldId());
        Assert.assertTrue(s.containsAll(s1.getSeatsHold()));
    }
}
