package main.java.com;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SeatHoldIdGenerator generates unique id
 */
public class SeatHoldIdGenerator implements IdGenerator {

    private static AtomicLong idCounter = new AtomicLong();

    /**
     * Generates unique id for a seat hold
     * @return unique id
     */
    public synchronized String generateId() {
        return String.valueOf(idCounter.getAndIncrement());
    }

}
