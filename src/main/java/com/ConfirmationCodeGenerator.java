package main.java.com;

import java.util.UUID;

/**
 * ConfirmationCodeGenerator generates unique confirmation code
 */
public class ConfirmationCodeGenerator implements IdGenerator {

    /**
     * Generates unique code for a confirmation
     * @return unique code
     */
    public synchronized String generateId() {
        return UUID.randomUUID().toString();
    }
}
