package Services;

import Common.Clear;
import DAOs.AuthTokenDao;
import DAOs.Database;
import DAOs.EventDao;
import Exceptions.DataAccessException;
import Models.AuthToken;
import Models.Event;
import Responses.EventResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventServiceTest {
  Connection conn;
  Database database = new Database();
  EventService service;
  EventDao eventDao;


  @BeforeEach
  void setUp() throws DataAccessException {
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
  void getEventPass() throws DataAccessException {
    AuthTokenDao authDao = new AuthTokenDao(conn);

    AuthToken auth1 = new AuthToken("aaaa", "Gale");

    Event bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);

    authDao.addAuthtoken(auth1);
    eventDao.addEvent(bestEvent);

    tearDown();
    service = new EventService();
    EventResponse response = service.getEvent("Biking_123A", auth1);

    assertTrue(response.getSuccess());
  }

  @Test
  void getEventTablePass() throws DataAccessException {
    AuthTokenDao authDao = new AuthTokenDao(conn);

    AuthToken auth1 = new AuthToken("aaaa", "Gale");

    Event bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Event bestEvent2 = new Event("Swimming_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);
    Event bestEvent3 = new Event("Running_123A", "Gale", "Gale123A",
            35.9f, 140.1f, "Japan", "Ushiku",
            "Biking_Around", 2016);

    authDao.addAuthtoken(auth1);
    eventDao.addEvent(bestEvent);

    tearDown();
    service = new EventService();
    EventResponse response = service.getEventTable(auth1);

    assertTrue(response.getSuccess());
  }

  @Test
  void getEventFail() throws DataAccessException{
    service = new EventService();
    AuthToken auth1 = new AuthToken("aaaa", "Gale");
    tearDown();
    EventResponse response = service.getEvent("fakeID",auth1);

    System.out.println(response.toString());

    assertFalse(response.getSuccess());
  }

  @Test
  void getEventTableFail() throws DataAccessException{
    service = new EventService();
    AuthToken auth1 = new AuthToken("aaaa", "FakeUsername");
    tearDown();
    EventResponse response = service.getEventTable(auth1);

    System.out.println(response.toString());

    assertFalse(response.getSuccess());
  }
}