package Responses;

/**
 * Returns the result of the fill request
 *
 * Errors: Invalid username or generations parameter,
 * Internal server error
 */
public class FillResponse extends BaseResponse {
  public FillResponse(boolean success, String message) {
    super(success, message);
  }
}
