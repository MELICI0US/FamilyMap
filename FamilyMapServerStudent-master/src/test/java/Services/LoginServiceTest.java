package Services;

import Common.Clear;
import DAOs.Database;
import DAOs.UserDao;
import Exceptions.DataAccessException;
import Models.AuthToken;
import Models.User;
import Requests.LoginRequest;
import Responses.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginServiceTest {
  Connection conn;
  Database database = new Database();
  LoginService service;


  @BeforeEach
  void setUp() throws DataAccessException {
    conn = database.openConnection();
    Clear clearer = new Clear();
    clearer.clear(conn);
  }

  void tearDown() throws DataAccessException {
    database.closeConnection(true);
    conn = null;
  }

  @Test
  void loginPass() throws DataAccessException {
    User user=new User("user3", "pass123", "user3@user.com", "Joe", "Doey", "m", "xyz321");

    UserDao userDao = new UserDao(conn);
    userDao.addUser(user);

    tearDown();
    service = new LoginService();

    LoginRequest request = new LoginRequest("user3", "pass123");

    LoginResponse response = service.login(request, null);

    assertTrue(response.getSuccess());
  }

  @Test
  void loginFail() throws DataAccessException {
    tearDown();
    service = new LoginService();

    LoginRequest request = new LoginRequest("fakeuser", "fakepass");

    LoginResponse response = service.login(request, null);

    assertFalse(response.getSuccess());
  }
}