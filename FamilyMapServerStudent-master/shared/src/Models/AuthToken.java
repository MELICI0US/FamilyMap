package Models;

import static java.util.UUID.randomUUID;

public class AuthToken {
  String authtoken;
  String username;

  public AuthToken(String username) {
    this.username=username;
    generateAuthToken();
  }

  public AuthToken(String authtoken, String username) {
    this.username=username;
    this.authtoken = authtoken;
  }

  /**
   * Returns the value of the token
   *
   * @return the String value of the authorization token
   * */
  public String getToken() {
    return authtoken;
  }

  public String getUsername() {
    return username;
  }

  /**
   * Generates an authorization token
   */
  public void generateAuthToken() {
    String token = randomUUID().toString().substring(0, 7);

    this.authtoken = token;
  }
}
