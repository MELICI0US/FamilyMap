package Services;

import Common.Clear;
import DAOs.Database;
import Exceptions.DataAccessException;
import Requests.RegisterRequest;
import Responses.RegisterResponse;
import Server.LoadedData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterServiceTest {
  Connection conn;
  Database database = new Database();
  RegisterService service;


  @BeforeEach
  void setUp() throws DataAccessException, IOException {
    LoadedData loader = new LoadedData();
    loader.load();
    conn = database.openConnection();
    Clear clearer = new Clear();
    clearer.clear(conn);
  }

  void tearDown() throws DataAccessException {
    database.closeConnection(true);
    conn = null;
  }

  @Test
  void registerPass() throws DataAccessException {
    RegisterRequest request = new RegisterRequest("user", "pass", "user@user.com", "user", "name", "m");

    tearDown();

    service = new RegisterService();

    RegisterResponse response = service.register(request);

    assertTrue(response.getSuccess());
  }

  @Test
  void registerFail() throws DataAccessException {
    RegisterRequest request = new RegisterRequest("user", "pass", "user@user.com", "user", "name", "m");

    tearDown();

    service = new RegisterService();

    service.register(request);
    RegisterResponse response = service.register(request);

    assertFalse(response.getSuccess());
  }
}