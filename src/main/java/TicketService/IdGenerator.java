package TicketService;

/**
 * IdGenerator generates unique identifications for some object(s).
 */
public interface IdGenerator {

    /**
     * Generates unique id ideally to identify an object(s).
     * @return unique id
     */
    String generateId();
}
