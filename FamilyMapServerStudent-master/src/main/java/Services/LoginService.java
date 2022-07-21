package Services;

import DAOs.AuthTokenDao;
import DAOs.Database;
import DAOs.UserDao;
import Exceptions.DataAccessException;
import Models.AuthToken;
import Models.User;
import Requests.LoginRequest;
import Responses.LoginResponse;

/**
 * Logs in the user and returns an auth token.
 */
public class LoginService extends Service {

  public LoginResponse login(LoginRequest request, Database database) {
    boolean createdDatabase = false;
    LoginResponse response;
    try {
      this.database = database;
      if (database == null) {
        createdDatabase = true;
        connect();
      } else {
        conn = database.getConnection();
      }

      String username = request.getUsername();
      String password = request.getPassword();
      User user = validateUser(username, password);
      if (user != null) {
        AuthToken authToken = getAuthtoken(username);
        response = new LoginResponse(true, authToken.getToken(), username, user.getPersonID());
      } else {
        response = new LoginResponse(false, "Error: User not found");
      }

      if (createdDatabase) {
        disconnect();
      }
    } catch (DataAccessException ex) {
      response = new LoginResponse(false, "Error: " + ex.getMessage());
      disconnect();
    }

    return response;
  }

  /**
   * generates an auth token and stores it in the database
   *
   * @return the generated token object
   */
  private AuthToken getAuthtoken(String username) throws DataAccessException {
    AuthToken token = new AuthToken(username);
    AuthTokenDao authTokenDao = new AuthTokenDao(conn);

    while (authTokenDao.validateToken(token)) {
      token = new AuthToken(username);
    }

    authTokenDao.addAuthtoken(token);

    return token;
  }

  /**
   * Checks the database to see if the username and password match
   *
   * @param username
   * @param password
   * @return true if they match, false otherwise
   */
  private User validateUser(String username, String password) throws DataAccessException {
    UserDao userDao = new UserDao(conn);

    User possibleUser = userDao.getUserByUsername(username);
    if (possibleUser != null && possibleUser.getPassword().equals(password)) {
      return possibleUser;
    }

    return null;
  }

}
