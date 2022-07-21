package Responses;

/**
 * Returns the result of the register request
 *
 * Errors:  Request property missing or has invalid value,
 * Username already taken by another user,
 * Internal server error
 */
public class RegisterResponse extends BaseResponse {
  String authtoken;
  String username;
  String personID;

  public RegisterResponse(String authtoken, String username, String personID, boolean success) {
    super(success, null);
    this.authtoken=authtoken;
    this.username=username;
    this.personID=personID;
    this.success=success;
  }

  public RegisterResponse(boolean success, String message){
    super(success, message);
  }

  @Override
  public String toString() {
    return "RegisterResponse{" +
            "authtoken='" + authtoken + '\'' +
            ", username='" + username + '\'' +
            ", personID='" + personID + '\'' +
            ", message='" + message + '\'' +
            ", success=" + success +
            '}';
  }

  public String getAuthtoken() {
    return authtoken;
  }

  public String getUsername() {
    return username;
  }

  public String getPersonID() {
    return personID;
  }

}
