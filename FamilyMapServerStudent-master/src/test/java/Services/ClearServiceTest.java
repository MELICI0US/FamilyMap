package Services;

import Common.Clear;
import DAOs.*;
import Exceptions.DataAccessException;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ClearServiceTest {
  Connection conn;
  Database database = new Database();
  ClearService service = new ClearService();


  @BeforeEach
  void setUp() throws DataAccessException {
    conn = database.openConnection();
    Clear clearer = new Clear();
    clearer.clear(conn);
  }


  void close() throws DataAccessException {
    database.closeConnection(true);
    conn = null;
  }

  @Test
  void clear() throws DataAccessException {
    AuthTokenDao authDao = new AuthTokenDao(conn);
    EventDao eventDao = new EventDao(conn);

    AuthToken auth1 = new AuthToken("aaaa");
    AuthToken auth2 = new AuthToken("bbbb");
    AuthToken auth3 = new AuthToken("cccc");

    authDao.addAuthtoken(auth1);
    authDao.addAuthtoken(auth2);
    authDao.addAuthtoken(auth3);

    Event bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Event bestEvent2 = new Event("Swimming_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Event bestEvent3 = new Event("Running_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);

    eventDao.addEvent(bestEvent);
    eventDao.addEvent(bestEvent2);
    eventDao.addEvent(bestEvent3);

    close();

    service.clear();

    setUp();
    authDao = new AuthTokenDao(conn);
    eventDao = new EventDao(conn);
    assertTrue(eventDao.getTable().isEmpty());
    assertTrue(authDao.getTable().isEmpty());
    close();
  }

  @Test
  void clear2() throws DataAccessException {
    UserDao userDao = new UserDao(conn);
    PersonDao personDao = new PersonDao(conn);

    User user = new User("user3", "pass123", "user3@user.com", "Joe", "Doey", "m", "xyz321");
    User user2 = new User("user4", "password123", "user4@user.com", "Julia", "Doey", "f", "321xyz");
    User user3 = new User("user5", "word123", "user5@user.com", "Jimothy", "Doey", "m", "4321wxyz");

    userDao.addUser(user);
    userDao.addUser(user2);
    userDao.addUser(user3);

    Person person1 = new Person("p121", "user", "Joe", "Doey", "m", "p221", "p321", "p421");
    Person person2 = new Person("p122", "user", "Julia", "Doey", "f", "p222", "p322", "p42");
    Person person3 = new Person("p123", "user", "Jimothy", "Doey", "m", "p223", "p323", "p42");

    personDao.addPerson(person1);
    personDao.addPerson(person2);
    personDao.addPerson(person3);

    close();

    service.clear();

    setUp();
    userDao = new UserDao(conn);
    personDao = new PersonDao(conn);
    assertTrue(userDao.getTable().isEmpty());
    assertTrue(personDao.getTable().isEmpty());
    close();
  }
}