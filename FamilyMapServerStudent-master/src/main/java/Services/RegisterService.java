package Services;

import DAOs.PersonDao;
import DAOs.UserDao;
import Exceptions.DataAccessException;
import Models.Person;
import Models.User;
import Requests.FillRequest;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.FillResponse;
import Responses.LoginResponse;
import Responses.RegisterResponse;


/**
 * Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
 */
public class RegisterService extends Service {
  User user;
  Person person;
  UserDao userDao;
  String authToken;

  /**
   * Generates 4 generations of ancestor data for each new user
   */
  private void generateGenerations() throws DataAccessException {
    FillRequest fillRequest = new FillRequest(user.getUsername(), 4);
    FillService fillService = new FillService();

    FillResponse response = fillService.fill(fillRequest, database, person);
    if (!response.getSuccess()) {
      throw new DataAccessException("Fill failed");
    }
  }

  /**
   * Logs the user in after registering
   */
  private void login() throws DataAccessException {
    LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());
    LoginService loginService = new LoginService();

    LoginResponse response = loginService.login(loginRequest, database);
    authToken = response.getAuthtoken();
    //TODO add message if fails
  }

  /**
   * Creates a user and adds them to the database
   *
   * @param request holds all the data for the user to be made
   */
  private void createUser(RegisterRequest request) throws DataAccessException {
    userDao = new UserDao(conn);
    PersonDao personDao = new PersonDao(conn);

    if (userDao.getUserByUsername(request.getUsername()) != null){
      throw new DataAccessException("Username taken");
    }

    person = new Person(request.getUsername(), request.getFirstName(), request.getLastName(), request.getGender(), null, null, null);

    user = new User(request.getUsername(), request.getPassword(), request.getEmail(), request.getFirstName(), request.getLastName(), request.getGender(), person.getPersonID());


    userDao.addUser(user);

  }

  public RegisterResponse register(RegisterRequest request) {
    RegisterResponse response;
    try {
      connect();

      createUser(request);

      generateGenerations();

      login();

      response = new RegisterResponse(authToken, user.getUsername(), user.getPersonID(), true);

      disconnect();
    } catch (DataAccessException ex) {
      response = new RegisterResponse(false, "Error: " + ex.getMessage());
      disconnect();
    }

    return response;
  }
}
