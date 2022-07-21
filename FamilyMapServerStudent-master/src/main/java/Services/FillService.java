package Services;

import DAOs.*;
import Data.Location;
import Exceptions.DataAccessException;
import Exceptions.DataGenerationException;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.FillRequest;
import Responses.FillResponse;
import Server.LoadedData;

import java.util.ArrayList;
import java.util.Random;

/**
 * Service for fill
 */
public class FillService extends Service {
  EventDao eventDao;
  UserDao userDao;
  PersonDao personDao;
  String username;
  int totalGen;
  int personsGenerated = 1;
  int eventsGenerated = 0;

  /**
   * generates specified number of generations for the user
   */
  public FillResponse fill(FillRequest request, Database database, Person person) {
    FillResponse response;
    boolean createdDatabase = false;
    try {
      this.database = database;
      if (database == null) {
        createdDatabase = true;
        connect();
      } else {
        conn = database.getConnection();
      }

      username = request.getUsername();
      int generations = request.getGenerations();

      eventDao = new EventDao(conn);
      personDao = new PersonDao(conn);
      userDao = new UserDao(conn);
      User user = userDao.getUserByUsername(username);
      if (person == null){
        person = personDao.getPerson(user.getPersonID());
      }
      try {
        getBirthYear(person);
      } catch (DataGenerationException ex){
        birth(person, 2021);
      }

      if (user != null) {
        totalGen = generations;
        generate(person, generations);
        response = new FillResponse(true, "Successfully added " + personsGenerated + " persons and " + eventsGenerated + " events to the database");
      } else {
        response = new FillResponse(false, "User not found");
      }

      if (createdDatabase) {
        disconnect();
      }
    } catch (Exception e) {
      response = new FillResponse(false, "Error: " + e.getMessage());
      if (createdDatabase) {
        disconnect();
      }
    }

    return response;
  }


  /**
   * Each person, excluding the user, must have at least three events (birth, marriage, and death) and the user needs to at least have a birth event.
   * <p>
   * Parents should be born a reasonable number of years before their children (at least 13 years),
   * <p>
   * get married at a reasonable age,
   * <p>
   * and not die before their child is born.
   * <p>
   * Also, females should not give birth at over 50 years old.
   * <p>
   * Birth events need be first, and death events need to be last.
   * <p>
   * No one should die at over 120 years old.
   * <p>
   * Each person in a couple has their own marriage event, but their two marriage events need to have the same year and location.
   *
   * @param person      the child of the generation
   * @param generations the number of generations that come after this one
   * @throws DataAccessException
   */
  private void generate(Person person, int generations) throws Exception {
    if (generations > 0) {
      Person mom = createMom();
      Person dad = createDad();

      int maxYear = calcMaxParentBirthYear(person);

      if (maxYear == 2022) {
        throw new DataGenerationException("Error encountered while calculating parents max birth year");
      }

      birth(mom, maxYear);
      birth(dad, maxYear);
      marry(mom, dad);
      death(mom);
      death(dad);

      generate(mom, generations - 1);
      generate(dad, generations - 1);

      mom.setSpouseID(dad.getPersonID());
      dad.setSpouseID(mom.getPersonID());

      person.setMotherID(mom.getPersonID());
      person.setFatherID(dad.getPersonID());

      personDao.addPerson(mom);
      personDao.addPerson(dad);
    }
    if (generations == totalGen){
      if (personDao.getPerson(person.getPersonID()) == null){
        personDao.addPerson(person);
      }
    }
  }

  private Person createMom() throws DataAccessException {
    String fname = LoadedData.fnames.getRandomName();
    String sname = LoadedData.snames.getRandomName();

    Person person = new Person(username, fname, sname, "f", null, null, null);

    personsGenerated++;

    return person;
  }

  private Person createDad() throws DataAccessException {
    String fname = LoadedData.mnames.getRandomName();
    String sname = LoadedData.snames.getRandomName();

    Person person = new Person(username, fname, sname, "m", null, null, null);

    personsGenerated++;

    return person;
  }

  private void birth(Person person, int maxYear) throws DataAccessException {

    Random rand = new Random();
    int random = rand.nextInt(50-13);

    int birthYear = maxYear - random;
    Location loc = LoadedData.locations.getRandomLocation();

    Event birth = new Event(username, person.getPersonID(), loc.getLatitude(), loc.getLongitude(), loc.getCountry(), loc.getCity(), "birth", birthYear);

    eventDao.addEvent(birth);
    eventsGenerated++;
  }

  private void marry(Person wife, Person husband) throws Exception {

    int wifeBirthYear = getBirthYear(wife);
    int husbandBirthYear = getBirthYear(husband);
    int maxYear;
    int minYear;

    if (wifeBirthYear < husbandBirthYear) {
      maxYear = wifeBirthYear + 120;
      minYear = husbandBirthYear + 13;
    } else {
      maxYear = husbandBirthYear + 120;
      minYear = wifeBirthYear + 13;
    }

    Random rand = new Random();
    int bound = maxYear - minYear;
    if (bound < 1) {
      bound = 1;
    }
    int random = rand.nextInt(bound);

    int marriageYear = maxYear - random;
    Location loc = LoadedData.locations.getRandomLocation();

    Event wifeMarriage = new Event(username, wife.getPersonID(), loc.getLatitude(), loc.getLongitude(), loc.getCountry(), loc.getCity(), "marriage", marriageYear);
    Event husbandMarriage = new Event(username, husband.getPersonID(), loc.getLatitude(), loc.getLongitude(), loc.getCountry(), loc.getCity(), "marriage", marriageYear);

    eventDao.addEvent(wifeMarriage);
    eventDao.addEvent(husbandMarriage);

    eventsGenerated += 2;
  }

  private void death(Person person) throws Exception {
    int birthYear = getBirthYear(person);
    int maxYear = birthYear + 120;

    ArrayList<Event> events = eventDao.getEventsByPersonID(person.getPersonID());

    Event lastEvent = null;
    for (Event event : events) {
      if (lastEvent == null || event.getYear() > lastEvent.getYear()) {
        lastEvent = event;
      }
    }

    int minYear = lastEvent.getYear();

    Random rand = new Random();
    int bound = maxYear - minYear;
    if (bound < 1) {
      bound = 1;
    }
    int random = rand.nextInt(bound);

    int deathYear = maxYear - random;
    Location loc = LoadedData.locations.getRandomLocation();

    Event death = new Event(username, person.getPersonID(), loc.getLatitude(), loc.getLongitude(), loc.getCountry(), loc.getCity(), "death", deathYear);

    eventDao.addEvent(death);

    eventsGenerated++;
  }

  private int calcMaxParentBirthYear(Person person) throws Exception {

    int birthYear = getBirthYear(person);

    int maxYear = birthYear - 13;

    return maxYear;
  }

  private int getBirthYear(Person person) throws Exception {
    ArrayList<Event> events = eventDao.getEventsByPersonID(person.getPersonID());

    Event birth = null;
    for (Event event : events) {
      if (event.getEventType().equals("birth")) {
        birth = event;
      }
    }
    int birthYear = 2022;

    if (birth != null) {
      birthYear = birth.getYear();
    } else {
      throw new DataGenerationException("Birth nonexistent");
    }

    return birthYear;
  }

}
