package Services;

import Common.Clear;
import DAOs.Database;
import DAOs.EventDao;
import DAOs.UserDao;
import Exceptions.DataAccessException;
import Models.Person;
import Models.User;
import Requests.FillRequest;
import Responses.FillResponse;
import Server.LoadedData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FillServiceTest {
  Connection conn;
  Database database = new Database();
  FillService service;
  EventDao eventDao;


  @BeforeEach
  void setUp() throws DataAccessException, IOException {
    LoadedData loader = new LoadedData();
    loader.load();
    conn = database.openConnection();
    eventDao = new EventDao(conn);
    Clear clearer = new Clear();
    clearer.clear(conn);
  }

  void tearDown() throws DataAccessException {
    database.closeConnection(true);
    conn = null;
  }

  @Test
  void fillPass() throws DataAccessException {
    service = new FillService();
    FillRequest request = new FillRequest("user", 4);
    UserDao userDao = new UserDao(conn);

    Person person = new Person("user", "John", "Doe", "m", null, null, null);

    User user = new User("user", "password", "user@user.com", "John", "Doe", "m",person.getPersonID());


    userDao.addUser(user);

    tearDown();
    FillResponse response = service.fill(request, null, person);

    System.out.println(response.toString());

    assertTrue(response.getSuccess());
  }

  @Test
  void fillFail() throws DataAccessException{
    service = new FillService();
    FillRequest request = new FillRequest("user", 4);
    UserDao userDao = new UserDao(conn);

    Person person = new Person("user", "John", "Doe", "m", null, null, null);

    User user = new User("user", "password", "user@user.com", "John", "Doe", "m",person.getPersonID());

    tearDown();
    FillResponse response = service.fill(request, null, person);

    System.out.println(response.toString());

    assertFalse(response.getSuccess());
  }
}