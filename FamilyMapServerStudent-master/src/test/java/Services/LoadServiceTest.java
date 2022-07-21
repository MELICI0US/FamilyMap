package Services;

import Common.Clear;
import DAOs.Database;
import Exceptions.DataAccessException;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.LoadRequest;
import Responses.LoadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoadServiceTest {
  Connection conn;
  Database database = new Database();
  LoadService service;


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
  void loadPass() throws DataAccessException{
    Event bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Event bestEvent2 = new Event("Swimming_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Event bestEvent3 = new Event("Running_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);

    ArrayList<Event> events = new ArrayList<>();
    events.add(bestEvent);
    events.add(bestEvent2);
    events.add(bestEvent3);

    Person person1=new Person("p121", "user", "Joe", "Doey", "m", "p221", "p321", "p421");
    Person person2=new Person("p122", "user", "Julia", "Doey", "f", "p222", "p322", "p42");
    Person person3=new Person("p123", "user", "Jimothy", "Doey", "m", "p223", "p323", "p42");

    ArrayList<Person> persons = new ArrayList<>();
    persons.add(person1);
    persons.add(person2);
    persons.add(person3);

    User user=new User("user3", "pass123", "user3@user.com", "Joe", "Doey", "m", "xyz321");
    User user2=new User("user4", "password123", "user4@user.com", "Julia", "Doey", "f", "321xyz");
    User user3=new User("user5", "word123", "user5@user.com", "Jimothy", "Doey", "m", "4321wxyz");

    ArrayList<User> users = new ArrayList<>();
    users.add(user);
    users.add(user2);
    users.add(user3);

    LoadRequest request = new LoadRequest(users, persons, events);

    tearDown();
    service = new LoadService();
    LoadResponse response = service.load(request);

    assertTrue(response.getSuccess());
  }

  @Test
  void loadFail() throws DataAccessException{
    Event bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Event bestEvent2 = new Event("Biking_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Event bestEvent3 = new Event("Biking_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);

    ArrayList<Event> events = new ArrayList<>();
    events.add(bestEvent);
    events.add(bestEvent2);
    events.add(bestEvent3);

    Person person1=new Person("p121", "user", "Joe", "Doey", "m", "p221", "p321", "p421");
    Person person2=new Person("p121", "user", "Julia", "Doey", "f", "p222", "p322", "p42");
    Person person3=new Person("p121", "user", "Jimothy", "Doey", "m", "p223", "p323", "p42");

    ArrayList<Person> persons = new ArrayList<>();
    persons.add(person1);
    persons.add(person2);
    persons.add(person3);

    User user=new User("user3", "pass123", "user3@user.com", "Joe", "Doey", "m", "xyz321");
    User user2=new User("user3", "password123", "user4@user.com", "Julia", "Doey", "f", "321xyz");
    User user3=new User("user3", "word123", "user5@user.com", "Jimothy", "Doey", "m", "4321wxyz");

    ArrayList<User> users = new ArrayList<>();
    users.add(user);
    users.add(user2);
    users.add(user3);

    LoadRequest request = new LoadRequest(users, persons, events);

    tearDown();
    service = new LoadService();
    LoadResponse response = service.load(request);

    assertFalse(response.getSuccess());
  }
}