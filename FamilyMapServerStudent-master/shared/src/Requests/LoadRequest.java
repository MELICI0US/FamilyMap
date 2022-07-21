package Requests;

import Models.Event;
import Models.Person;
import Models.User;
import Responses.LoadResponse;

import java.util.ArrayList;

/**
 * Clears all data from the database (just like the /clear API), and then loads the posted user, person, and event data into the database.
 */
public class LoadRequest {
  ArrayList<User> users;
  ArrayList<Person> persons;
  ArrayList<Event> events;

  public LoadRequest(ArrayList<User> users, ArrayList<Person> persons, ArrayList<Event> events) {
    this.users=users;
    this.persons=persons;
    this.events=events;
  }

  public ArrayList<User> getUsers() {
    return users;
  }

  public ArrayList<Person> getPersons() {
    return persons;
  }

  public ArrayList<Event> getEvents() {
    return events;
  }
}
