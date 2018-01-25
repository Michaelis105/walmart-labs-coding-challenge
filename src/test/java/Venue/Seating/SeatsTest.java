package Venue.Seating;

import org.junit.Assert;
import org.junit.Test;

public class SeatsTest {

    @Test
    public void Basic() {
        Seats s1 = new Seats(-1, -1);
        Seats s2 = new Seats(0, 0);
        Seats s3 = new Seats(10, 10);
        Seats s4 = new Seats(100, 300);
        Seats s5 = new Seats(900, 1000);

        // Invalid rows or columns will initialize seats with one row and column.
        Assert.assertEquals(1, s1.getRowLength());
        Assert.assertEquals(1, s1.getColumnLength());
        Assert.assertEquals(1, s2.getRowLength());
        Assert.assertEquals(1, s2.getColumnLength());

        // Valid rows and columns should have correct lengths.
        Assert.assertEquals(10, s3.getRowLength());
        Assert.assertEquals(10, s3.getColumnLength());
        Assert.assertEquals(100, s4.getRowLength());
        Assert.assertEquals(300, s4.getColumnLength());
        Assert.assertEquals(900, s5.getRowLength());
        Assert.assertEquals(1000, s5.getColumnLength());

        // Out-of-bounds seat indices should return null seat.
        Assert.assertNull(s5.getSeat(-1,-1));
        Assert.assertNull(s5.getSeat(5,-1));
        Assert.assertNull(s5.getSeat(-1,5));
        Assert.assertNull(s5.getSeat(1000,900));

        // Valid seat indices should return a seat.
        Assert.assertNotNull(s5.getSeat(0,0));
        Assert.assertNotNull(s5.getSeat(0,999));
        Assert.assertNotNull(s5.getSeat(899,0));
        Assert.assertNotNull(s5.getSeat(899,999));
    }

}
