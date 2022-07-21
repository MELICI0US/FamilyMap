package Exceptions;

/**
 * Thrown when there is an error encountered while trying to generate family data
 */
public class DataGenerationException  extends Exception {
  public DataGenerationException(String message)
  {
    super(message);
  }

  DataGenerationException()
  {
    super();
  }
}
