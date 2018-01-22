package main.java.com;

import java.util.ArrayList;
import java.util.List;

public class Seats {
    List<List<Seat>> seats;

    public Seats(int row, int col) {
        if (row <= 0) row = 1;
        if (col <= 0) col = 1;
        for (int i = 0; i < row; i++) {
            List<Seat> aRow = new ArrayList<Seat>();
            for (int j = 0; j < col; j++) {
                aRow.add(new Seat(i+j));
            }
            seats.add(aRow);
        }
    }

}
