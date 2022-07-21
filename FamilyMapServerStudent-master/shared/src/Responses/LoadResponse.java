package Responses;

/**
 * Returns the result of the load request
 *
 * Errors: Invalid request data (missing values, invalid values, etc.),
 * Internal server error
 */
public class LoadResponse extends BaseResponse {

  public LoadResponse(boolean success, String message) {
    super(success, message);
  }
}
