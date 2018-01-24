package main.java.com.Venue.Seating;

import java.util.ArrayList;
import java.util.List;

/**
 * Seats is seating arrangement
 */
public class Seats {

    private List<List<Seat>> seats;

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
                aRow.add(new Seat(i + j)); // Seat ID is row + col (00, 01, 02, ..., NM)
            }
            seats.add(aRow);
        }
    }

    public int getRowLength() { return seats.size(); }
    public int getColumnLength() { return seats.get(0).size(); }
    public Seat getSeat(int row, int col){
        Seat s = null;
        if (row >= 0 && row < seats.size() && col >= 0 && col < seats.get(0).size()) {
            s = seats.get(row).get(col);
        }
        return s;
    }
}
