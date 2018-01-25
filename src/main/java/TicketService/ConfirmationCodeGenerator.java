package TicketService;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ConfirmationCodeGenerator generates unique confirmation code
 */
public class ConfirmationCodeGenerator implements IdGenerator {

    private static AtomicInteger idCounter = new AtomicInteger(0);

    /**
     * Generates unique id for a confirmation.
     * @return unique id
     */
    public synchronized String generateId() {
        return String.valueOf(idCounter.getAndIncrement()); // No two concurrent threads can obtain same id
    }

}
