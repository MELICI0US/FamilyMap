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

class eventDaoTest {
  private Database db;
  private Event bestEvent;
  private EventDao eDao;

  @BeforeEach
  public void setUp() throws DataAccessException
  {
    db = new Database();
    bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Connection conn = db.getConnection();
    db.clearTables();
    eDao = new EventDao(conn);
  }

  @AfterEach
  public void tearDown() throws DataAccessException {

    db.closeConnection(false);
  }

  @Test
  public void insertPass() throws DataAccessException {

    eDao.addEvent(bestEvent);

    Event compareTest = eDao.getEvent(bestEvent.getEventID());

    assertNotNull(compareTest);

    assertEquals(bestEvent, compareTest);
  }

  @Test
  public void insertFail() throws DataAccessException {

    eDao.addEvent(bestEvent);

    assertThrows(DataAccessException.class, ()-> eDao.addEvent(bestEvent));
  }

  @Test
  void getEventPass() throws DataAccessException{
    Event event = new Event("Biking_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);    Person person2=new Person("p122", "user1", "Julia", "Doey", "f", "p222", "p322", "p42");

    eDao.addEvent(event);

    assertEquals(event, eDao.getEvent(event.getEventID()));
  }

  @Test
  void getEventFail() throws DataAccessException {
    assertNull(eDao.getEvent("notRealID"));
  }

  @Test
  void clear() throws DataAccessException{
    Event event = new Event("Biking_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);

    eDao.addEvent(event);

    eDao.clear();

    assertTrue(eDao.getTable().isEmpty());
  }

  @Test
  void getEventByPersonPass() throws DataAccessException{
    bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Event bestEvent2 = new Event("Swimming_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Event bestEvent3 = new Event("Running_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);

    eDao.addEvent(bestEvent);
    eDao.addEvent(bestEvent2);
    eDao.addEvent(bestEvent3);

    ArrayList<Event> eventsForPerson = new ArrayList<>();
    eventsForPerson.add(bestEvent);
    eventsForPerson.add(bestEvent2);
    eventsForPerson.add(bestEvent3);

    ArrayList<Event> actualEvents = eDao.getEventsByPersonID("Gale123A");

    assertEquals(eventsForPerson, actualEvents);
  }

  @Test
  void getEventByPersonFail()throws DataAccessException{
    ArrayList<Event> actualEvents = eDao.getEventsByPersonID("FakeId");

    ArrayList<Event> eventsForPerson = new ArrayList<>();

    assertEquals(eventsForPerson, actualEvents);
  }

  @Test
  void getEventsForUserPass() throws DataAccessException{
    bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Event bestEvent2 = new Event("Swimming_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Event bestEvent3 = new Event("Running_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);

    eDao.addEvent(bestEvent);
    eDao.addEvent(bestEvent2);
    eDao.addEvent(bestEvent3);

    ArrayList<Event> eventsForPerson = new ArrayList<>();
    eventsForPerson.add(bestEvent);
    eventsForPerson.add(bestEvent2);
    eventsForPerson.add(bestEvent3);

    ArrayList<Event> actualEvents = eDao.getEventsForUser("Gale");

    assertEquals(eventsForPerson, actualEvents);
  }

  @Test
  void getEventsForUserFail() throws DataAccessException{
    ArrayList<Event> actualEvents = eDao.getEventsByPersonID("FakeUsername");

    ArrayList<Event> eventsForPerson = new ArrayList<>();

    assertEquals(eventsForPerson, actualEvents);
  }
}