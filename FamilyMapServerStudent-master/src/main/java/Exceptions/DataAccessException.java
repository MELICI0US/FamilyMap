package Exceptions;

/**
 * Thrown when there is an error encountered while trying to do something with the database
 */
public class DataAccessException extends Exception {
    public DataAccessException(String message)
    {
        super(message);
    }

    DataAccessException()
    {
        super();
    }
}
