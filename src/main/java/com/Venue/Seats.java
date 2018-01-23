package main.java.com.Venue;

import java.util.ArrayList;
import java.util.List;

public class Seats {

    private List<List<Seat>> seats;
    private int numOpenSeats;
    private int numHoldSeats;
    private int numReservedSeats;

    /**
     * Setup seating arrangement in row by column box.
     * @param row number of seating rows
     * @param col number of seating columns
     */
    public Seats(int row, int col) {
        if (row <= 0) row = 1;
        if (col <= 0) col = 1;
        for (int i = 0; i < row; i++) {
            List<Seat> aRow = new ArrayList<Seat>();
            for (int j = 0; j < col; j++) {
                aRow.add(new Seat(i + j));
            }
            seats.add(aRow);
        }
        numOpenSeats = row * col;
        numHoldSeats = 0;
        numReservedSeats = 0;
    }

    public int getNumOpenSeats() { return numOpenSeats; }
    public int getNumHoldSeats() { return numHoldSeats; }
    public int getNumReservedSeats() { return numReservedSeats; }

    // TODO: Fix counter handling for seats. Currently only TicketService knows count.

}
