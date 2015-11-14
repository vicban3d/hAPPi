package Exceptions;

/**
 * Created by victor on 11/14/2015.
 */
public class DatabaseConnectionErrorException extends Exception {
    public DatabaseConnectionErrorException(String message) {
        super(message);
    }
}
