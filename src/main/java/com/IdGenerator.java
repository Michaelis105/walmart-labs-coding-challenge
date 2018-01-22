package main.java.com;

/**
 * IdGenerator generates unique identifications.
 */
public interface IdGenerator {

    /**
     * Generates unique id ideally to identify an object.
     * @return unique id
     */
    public String generateId();
}
