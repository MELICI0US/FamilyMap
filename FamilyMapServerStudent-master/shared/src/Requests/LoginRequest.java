package Requests;

import Responses.LoginResponse;

/**
 * Logs in the user and returns an auth token.
 */
public class LoginRequest {
  String username;
  String password;

  public LoginRequest(String username, String password){
    this.username = username;
    this.password = password;

  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  /**
   * Logs the user in
   * @return the result
   */
  public LoginResponse login(){
    return null;
  }

}
