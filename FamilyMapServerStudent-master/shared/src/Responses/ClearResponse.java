package Responses;

/**
 * Returns the result of the clear request
 *
 * Errors: Internal server error
 */
public class ClearResponse extends BaseResponse{

  public ClearResponse(boolean success, String message) {
    super(success, message);
  }

}
