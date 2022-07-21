package Services;

import DAOs.AuthTokenDao;
import DAOs.PersonDao;
import Exceptions.DataAccessException;
import Models.AuthToken;
import Models.Person;
import Responses.PersonResponse;

import java.util.ArrayList;

/**
 * Service for person
 */
public class PersonService extends Service {
  PersonDao personDao;

  public PersonResponse getPerson(String personID, AuthToken token) {
    PersonResponse response;
    try {
      connect();

      personDao = new PersonDao(conn);
      Person person = personDao.getPerson(personID);

      String username = validateAuthToken(token);

      if (person == null){
        disconnect();
        response = new PersonResponse(false, "Error: Person does not exist");
        return response;
      }

      if (!person.getUsername().equals(username)){
        disconnect();
        response = new PersonResponse(false, "Error: Person does not belong to you");
        return response;
      }

      response = new PersonResponse(personID, person.getUsername(), person.getFirstName(), person.getLastName(), person.getGender(), person.getFatherID(), person.getMotherID(), person.getSpouseID(), true);

      disconnect();
    } catch (DataAccessException ex) {
      response = new PersonResponse(false, ex.getMessage());
      disconnect();
    }
    return response;
  }

  public PersonResponse getPersonTable(AuthToken token) {
    PersonResponse response;
    try {
      connect();

      String username = validateAuthToken(token);

      personDao = new PersonDao(conn);
      ArrayList<Person> persons = personDao.getPersonsForUser(username);

      if (persons == null || persons.size() == 0){
        disconnect();
        response = new PersonResponse(false, "Error: No persons");
        return response;
      }

      response = new PersonResponse(true, persons);

      disconnect();
    } catch (DataAccessException ex) {
      response = new PersonResponse(false, ex.getMessage());
    }
    return response;
  }

  private String validateAuthToken(AuthToken token) throws DataAccessException{
    String username;
    try {
      AuthTokenDao authTokenDao = new AuthTokenDao(conn);
      username = authTokenDao.getUsername(token);
    } catch (DataAccessException ex) {
      username = "";
      throw new DataAccessException("Invalid auth token");
    }

    return username;
  }

}
