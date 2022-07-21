package DAOs;

import Exceptions.DataAccessException;
import Models.Event;
import Models.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class personDaoTest {
  Connection conn;
  Database database=new Database();
  PersonDao personDAO;

  @BeforeEach
  void setUp() throws DataAccessException {
    conn=database.openConnection();
    database.clearTables();
    personDAO=new PersonDao(conn);
  }

  @AfterEach
  void tearDown() throws DataAccessException{
    database.closeConnection(false);
    conn=null;
  }

  @Test
  void addPersonPass() throws DataAccessException{
    Person person1=new Person("p101", "user", "John", "Doe", "m", "p201", "p301", "p401");
    Person person2=new Person("p102", "user1", "Jane", "Doe", "f", "p202", "p302", "p40");
    Person person3=new Person("p103", "user2", "Jim", "Doe", "m", "p203", "p303", "p40");

    personDAO.addPerson(person1);
    personDAO.addPerson(person2);
    personDAO.addPerson(person3);

    assertEquals(person1, personDAO.getPerson(person1.getPersonID()));
    assertEquals(person2, personDAO.getPerson(person2.getPersonID()));
    assertEquals(person3, personDAO.getPerson(person3.getPersonID()));
  }

  @Test
  void addPersonFail() throws DataAccessException{
    Person person = new Person("p101", "user", "John", "Doe", "m", "p201", "p301", "p401");

    personDAO.addPerson(person);

    assertThrows(DataAccessException.class, ()->personDAO.addPerson(person));
  }

  @Test
  void getPersonPass() throws DataAccessException{
    Person person1=new Person("p121", "user", "Joe", "Doey", "m", "p221", "p321", "p421");
    Person person2=new Person("p122", "user1", "Julia", "Doey", "f", "p222", "p322", "p42");
    Person person3=new Person("p123", "user2", "Jimothy", "Doey", "m", "p223", "p323", "p42");

    personDAO.addPerson(person1);
    personDAO.addPerson(person2);
    personDAO.addPerson(person3);

    assertEquals(person1, personDAO.getPerson(person1.getPersonID()));
    assertEquals(person2, personDAO.getPerson(person2.getPersonID()));
    assertEquals(person3, personDAO.getPerson(person3.getPersonID()));
  }

  @Test
  void getPersonFail() throws DataAccessException {
    assertNull(personDAO.getPerson("notRealID"));
  }

    @Test
  void clear() throws DataAccessException{
      Person person1=new Person("p121", "user", "Joe", "Doey", "m", "p221", "p321", "p421");
      Person person2=new Person("p122", "user1", "Julia", "Doey", "f", "p222", "p322", "p42");
      Person person3=new Person("p123", "user2", "Jimothy", "Doey", "m", "p223", "p323", "p42");

      personDAO.addPerson(person1);
      personDAO.addPerson(person2);
      personDAO.addPerson(person3);

      personDAO.clear();

      assertTrue(personDAO.getTable().isEmpty());
  }

  @Test
  void getPersonsForUserPass() throws DataAccessException{
    Person person1=new Person("p121", "user", "Joe", "Doey", "m", "p221", "p321", "p421");
    Person person2=new Person("p122", "user", "Julia", "Doey", "f", "p222", "p322", "p42");
    Person person3=new Person("p123", "user", "Jimothy", "Doey", "m", "p223", "p323", "p42");

    personDAO.addPerson(person1);
    personDAO.addPerson(person2);
    personDAO.addPerson(person3);

    ArrayList<Person> personsForUser = new ArrayList<>();
    personsForUser.add(person1);
    personsForUser.add(person2);
    personsForUser.add(person3);

    ArrayList<Person> actualPersons = personDAO.getPersonsForUser("user");

    assertEquals(personsForUser, actualPersons);
  }

  @Test
  void getPersonsForUserFail() throws DataAccessException{
    ArrayList<Person> actualPersons = personDAO.getPersonsForUser("FakeUsername");


    ArrayList<Person> personsForUser = new ArrayList<>();

    assertEquals(personsForUser, actualPersons);
  }
}