package Services;

import Common.Clear;
import DAOs.AuthTokenDao;
import DAOs.Database;
import DAOs.PersonDao;
import Exceptions.DataAccessException;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Responses.EventResponse;
import Responses.PersonResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonServiceTest {
  Connection conn;
  Database database = new Database();
  PersonService service;
  PersonDao personDao;


  @BeforeEach
  void setUp() throws DataAccessException {
    conn = database.openConnection();
    personDao = new PersonDao(conn);
    Clear clearer = new Clear();
    clearer.clear(conn);
  }

  void tearDown() throws DataAccessException {
    database.closeConnection(true);
    conn = null;
  }

  @Test
  void getPerson() throws DataAccessException{
    AuthTokenDao authDao = new AuthTokenDao(conn);

    AuthToken auth1 = new AuthToken("aaaa", "user");

    Person person1=new Person("p121", "user", "Joe", "Doey", "m", "p221", "p321", "p421");

    authDao.addAuthtoken(auth1);
    personDao.addPerson(person1);

    tearDown();
    service = new PersonService();
    PersonResponse response = service.getPerson("p121", auth1);

    assertTrue(response.getSuccess());
  }

  @Test
  void getPersonTable() throws DataAccessException{
    AuthTokenDao authDao = new AuthTokenDao(conn);

    AuthToken auth1 = new AuthToken("aaaa", "user");

    Person person1=new Person("p121", "user", "Joe", "Doey", "m", "p221", "p321", "p421");
    Person person2=new Person("p14421", "user", "Joe", "Doey", "m", "p221", "p321", "p421");

    authDao.addAuthtoken(auth1);
    personDao.addPerson(person1);
    personDao.addPerson(person2);


    tearDown();
    service = new PersonService();
    PersonResponse response = service.getPersonTable(auth1);

    assertTrue(response.getSuccess());
  }

  @Test
  void getPersonFail() throws DataAccessException{
    service = new PersonService();
    AuthToken auth1 = new AuthToken("aaaa", "user");
    PersonResponse response = service.getPerson("fakeName",auth1);
    tearDown();

    assertFalse(response.getSuccess());
  }

  @Test
  void getPersonTableFail() throws DataAccessException{
    service = new PersonService();
    AuthToken auth1 = new AuthToken("aaaa", "FakeUser");
    PersonResponse response = service.getPersonTable(auth1);
    tearDown();

    assertFalse(response.getSuccess());
  }
}