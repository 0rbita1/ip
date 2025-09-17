package keeka.exceptions;

/**
 * Exception thrown when task marking operations fail.
 */
public class InvalidMarkingException extends Exception {
    public InvalidMarkingException(String message) {
        super(message);
    }
}
