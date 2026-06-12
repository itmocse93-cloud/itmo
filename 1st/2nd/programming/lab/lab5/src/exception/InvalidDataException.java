package exception;

/**
 * Exception for invalid data errors
 */
public class InvalidDataException extends Exception {
    /**
     * Constructor for InvalidDataException
     * @param message error message
     */
    public InvalidDataException(String message) {
        super(message);
    }
}