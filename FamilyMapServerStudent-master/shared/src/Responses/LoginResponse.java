package Responses;

import Models.AuthToken;

/**
 * Returns the result of the login request
 *
 * Errors: Request property missing or has invalid value,
 * Internal server error
 */
public class LoginResponse extends BaseResponse {
  String authtoken;
  String username;
  String personID;

  public LoginResponse(boolean success, String message) {
    super(success, message);
  }

  public LoginResponse(boolean success, String authtoken, String username, String personID) {
    super(success, null);
    this.authtoken=authtoken;
    this.username=username;
    this.personID=personID;
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
