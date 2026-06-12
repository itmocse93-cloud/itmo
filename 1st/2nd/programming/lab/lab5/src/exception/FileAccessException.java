package exception;

/**
 * Exception for file access errors
 */
public class FileAccessException extends Exception {
    /**
     * Constructor for FileAccessException
     * @param message error message
     */
    public FileAccessException(String message) {
        super(message);
    }
}