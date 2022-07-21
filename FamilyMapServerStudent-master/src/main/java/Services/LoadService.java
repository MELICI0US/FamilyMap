package Services;

import Common.Clear;
import DAOs.EventDao;
import DAOs.PersonDao;
import DAOs.UserDao;
import Exceptions.DataAccessException;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.LoadRequest;
import Responses.LoadResponse;

import java.util.ArrayList;

/**
 * Service for load
 */
public class LoadService extends Service {
  int usersAdded = 0;
  int personsAdded = 0;
  int eventsAdded = 0;

  /**
   * Loads all of the data into the database
   *
   * @return the result of loading the database
   */
  public LoadResponse load(LoadRequest request) {
    LoadResponse response;
    try {
      connect();

      Clear clearer = new Clear();
      clearer.clear(conn);

      loadUsers(request.getUsers());
      loadPersons(request.getPersons());
      loadEvents(request.getEvents());

      response = new LoadResponse(true, "Successfully added " + usersAdded + " users, " + personsAdded + " persons, and " + eventsAdded + " events to the database");

      disconnect();
    } catch (DataAccessException ex) {
      response = new LoadResponse(false, "Error: " + ex.getMessage());
      disconnect();
    }

    return response;
  }

  /**
   * takes the users array and loads it to the database
   */
  private void loadUsers(ArrayList<User> users) throws DataAccessException {
    UserDao userDao = new UserDao(conn);
    for (User user : users) {
      userDao.addUser(user);
      usersAdded++;
    }
  }

  /**
   * takes the persons array and loads it to the database
   */
  private void loadPersons(ArrayList<Person> persons) throws DataAccessException {
    PersonDao personDao = new PersonDao(conn);
    for (Person person: persons){
      personDao.addPerson(person);
      personsAdded++;
    }
  }

  /**
   * takes the events array and loads it to the database
   */
  private void loadEvents(ArrayList<Event> events) throws DataAccessException {
    EventDao eventDao = new EventDao(conn);
    for (Event event: events){
      eventDao.addEvent(event);
      eventsAdded++;
    }
  }
}
