package Services;

import DAOs.AuthTokenDao;
import DAOs.EventDao;
import Exceptions.DataAccessException;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Responses.EventResponse;
import Responses.PersonResponse;

import java.util.ArrayList;

/**
 * Service for event
 */
public class EventService extends Service{
  EventDao eventDao;


  public EventResponse getEvent(String eventID, AuthToken token) {
    EventResponse response;
    try {
      connect();

      eventDao = new EventDao(conn);
      Event event = eventDao.getEvent(eventID);

      String username = validateAuthToken(token);

      if (event == null || !event.getAssociatedUsername().equals(username)){
        disconnect();
        response = new EventResponse(false, "Error: Event does not belong to you");
        return response;
      }

      if (event == null){
        disconnect();
        response = new EventResponse(false, "Error: Event does not exist");
        return response;
      }

      response = new EventResponse(eventID, event.getAssociatedUsername(), event.getPersonID(), event.getLatitude(), event.getLongitude(), event.getCountry(), event.getCity(), event.getEventType(), event.getYear(), true);

      disconnect();
    } catch (DataAccessException ex) {
      response = new EventResponse(false, ex.getMessage());
    }
    return response;
  }

  public EventResponse getEventTable(AuthToken token) {
    EventResponse response;
    try {
      connect();

      String username = validateAuthToken(token);

      eventDao = new EventDao(conn);
      ArrayList<Event> events = eventDao.getEventsForUser(username);

      if (events == null || events.size() == 0){
        disconnect();
        response = new EventResponse(false, "Error: No events");
        return response;
      }

      response = new EventResponse(true, events);

      disconnect();
    } catch (DataAccessException ex) {
      response = new EventResponse(false, "Error: " + ex.getMessage());
      disconnect();
    }
    return response;
  }

  private String validateAuthToken(AuthToken token) throws DataAccessException{
    String username;
    try {
      AuthTokenDao authTokenDao = new AuthTokenDao(conn);
      username = authTokenDao.getUsername(token);
    } catch (DataAccessException ex) {
      throw new DataAccessException("Invalid auth token");
    }

    return username;
  }
}
