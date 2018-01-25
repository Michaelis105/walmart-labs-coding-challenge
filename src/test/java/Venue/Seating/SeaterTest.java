package Venue.Seating;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class SeaterTest {

    @Test
    public void Basic() {
        Seater s1 = new Seater(-1, -1);
        Seater s2 = new Seater(0, 0);
        Seater s3 = new Seater(10, 10);

        // Invalid rows or columns will initialize seater with one row and column of seats.
        Assert.assertEquals(1, s1.getNumOpenSeats());
        Assert.assertEquals(0, s1.getNumHoldSeats());
        Assert.assertEquals(0, s1.getNumReservedSeats());
        Assert.assertEquals(1, s2.getNumOpenSeats());
        Assert.assertEquals(0, s2.getNumHoldSeats());
        Assert.assertEquals(0, s2.getNumReservedSeats());
        Assert.assertEquals(100, s3.getNumOpenSeats());
        Assert.assertEquals(0, s3.getNumHoldSeats());
        Assert.assertEquals(0, s3.getNumReservedSeats());

        Assert.assertNotNull(s1.getSeats());
        Assert.assertNotNull(s2.getSeats());
        Assert.assertNotNull(s3.getSeats());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ProcessSeatsNull1() throws Exception {
        Seater s1 = new Seater(50, 50);
        s1.processSeats(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ProcessSeatsNull2() throws Exception {
        Seater s1 = new Seater(50, 50);
        s1.processSeats(new LinkedList<Seat>(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ProcessSeatsNoSeats() throws Exception {
        Seater s1 = new Seater(50, 50);
        s1.processSeats(new LinkedList<Seat>(), SeatState.HOLD);
    }

    @Test
    public void ProcessSeats1() throws Exception {
        Seater s1 = new Seater(4, 4);
        List<Seat> seats = new LinkedList<Seat>();

        /**
         * Hold first few seats.
         *
         * H H O O
         * H H O O
         * O O O O
         * O O O O
         */
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                seats.add(s1.getSeats().getSeat(i, j));
            }
        }
        s1.processSeats(seats, SeatState.HOLD);

        // Check same seats are marked hold.
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Assert.assertEquals(SeatState.HOLD, s1.getSeats().getSeat(i, j).getSeatState());
            }
        }

        // Check remaining seats remain original state.
        for (int i = 2; i <= 3; i++) {
            for (int j = 2; j <= 3; j++) {
                Assert.assertEquals(SeatState.OPEN, s1.getSeats().getSeat(i, j).getSeatState());
            }
        }

        // Check counts
        Assert.assertEquals(12, s1.getNumOpenSeats());
        Assert.assertEquals(4, s1.getNumHoldSeats());
        Assert.assertEquals(0, s1.getNumReservedSeats());
    }

    @Test
    public void ProcessSeats2() throws Exception {
        Seater s1 = new Seater(4, 4);

        // Get all seats
        List<Seat> seats = new LinkedList<Seat>();
        for (int i = 0; i < s1.getSeats().getRowLength(); i++) {
            for (int j = 0; j < s1.getSeats().getColumnLength(); j++) {
                seats.add(s1.getSeats().getSeat(i, j));
            }
        }

        // Attempt to overwrite state with same status.
        s1.processSeats(seats, SeatState.HOLD);
        s1.processSeats(seats, SeatState.HOLD);

        // Check same seats are marked hold.
        Assert.assertTrue(checkAllSeatsIsState(s1, SeatState.HOLD));

        s1.processSeats(seats, SeatState.RESERVED);

        // Check same seats are marked reserved.
        Assert.assertTrue(checkAllSeatsIsState(s1, SeatState.RESERVED));

        // Check counts
        Assert.assertEquals(0, s1.getNumOpenSeats());
        Assert.assertEquals(0, s1.getNumHoldSeats());
        Assert.assertEquals(16, s1.getNumReservedSeats());
    }

    @Test
    public void ProcessSeatsMultithread() {
        Seater multi = new Seater(4, 4);

        List<Seat> seats = new LinkedList<Seat>();
        for (int i = 0; i < multi.getSeats().getRowLength(); i++) {
            for (int j = 0; j < multi.getSeats().getColumnLength(); j++) {
                seats.add(multi.getSeats().getSeat(i, j));
            }
        }

        ProcessSeatsThread r1 = new ProcessSeatsThread(multi, seats, SeatState.HOLD);
        ProcessSeatsThread r2 = new ProcessSeatsThread(multi, seats, SeatState.RESERVED);
        ProcessSeatsThread r3 = new ProcessSeatsThread(multi, seats, SeatState.OPEN);
        ProcessSeatsThread r4 = new ProcessSeatsThread(multi, seats, SeatState.HOLD);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);
        Thread t4 = new Thread(r4);
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // TODO: Seater reference is incorrect
        // Note: If at least one count is true, then no two threads were modifying counters concurrently.
        // This is okay. Simulate different customer calls at different times. First come, first serve.
        Assert.assertTrue(multi.getNumOpenSeats() == 16 || multi.getNumHoldSeats() == 16 || multi.getNumReservedSeats() == 16);
    }

    public boolean checkAllSeatsIsState(Seater s, SeatState ss) {
        for (int i = 0; i < s.getSeats().getRowLength(); i++) {
            for (int j = 0; j < s.getSeats().getColumnLength(); j++) {
                if (!s.getSeats().getSeat(i, j).getSeatState().equals(ss)) return false;
            }
        }
        return true;
    }

    class ProcessSeatsThread implements Runnable {
        private Seater s;
        private List<Seat> ls;
        private SeatState ss;

        ProcessSeatsThread(Seater s, List<Seat> ls, SeatState ss) {
            this.s = s;
            this.ls = ls;
            this.ss = ss;
        }

        @Override
        public void run() {
            try {
                s.processSeats(ls, ss);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
