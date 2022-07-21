package Responses;

/**
 * A basic response that will be the superclass for all others
 */
public class BaseResponse {
  String message;
  boolean success;

  public BaseResponse(boolean success, String message) {
    this.success=success;
    this.message=message;
  }

  public boolean getSuccess(){
    return success;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return 

            "{" +
            "\"message\": \"" + message + '"' +"\"success\":" + success +

            '}';
  }
}
