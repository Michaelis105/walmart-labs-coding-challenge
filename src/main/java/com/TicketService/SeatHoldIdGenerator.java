package main.java.com.TicketService;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * SeatHoldIdGenerator generates unique id
 */
public class SeatHoldIdGenerator implements IdGenerator {

    private static AtomicInteger idCounter = new AtomicInteger(0);

    /**
     * Generates unique id for a seat hold.
     * @return unique id
     */
    public synchronized String generateId() {
        return String.valueOf(idCounter.getAndIncrement()); // No two concurrent threads can obtain same id
    }

}
