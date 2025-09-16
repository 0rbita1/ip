package keeka.exceptions;

/**
 * Exception thrown when task creation or manipulation fails.
 */
public class InvalidTaskException extends Exception {
    public InvalidTaskException(String message) {
        super(message);
    }
}