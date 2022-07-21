package DAOs;

import Exceptions.DataAccessException;
import Models.Person;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data access class for person
 */
public class PersonDao {

  private final Connection conn;

  public PersonDao(Connection conn) {
    this.conn=conn;
  }

  /**
   * Adds a person to the database
   *
   * @param person the person you are adding
   */
  public void addPerson(Person person) throws DataAccessException {
    String sql="INSERT INTO persons ( personID, username, firstName, lastName, gender, fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
    try (PreparedStatement stmt=conn.prepareStatement(sql)) {

      stmt.setString(1, person.getPersonID());
      stmt.setString(2, person.getUsername());
      stmt.setString(3, person.getFirstName());
      stmt.setString(4, person.getLastName());
      stmt.setString(5, person.getGender());
      stmt.setString(6, person.getFatherID());
      stmt.setString(7, person.getMotherID());
      stmt.setString(8, person.getSpouseID());

      stmt.executeUpdate();
    } catch (SQLException e) {

      throw new DataAccessException(e.getMessage() + "Error encountered while inserting person into the database");
    }
  }

  /**
   * Queries the database for the person
   *
   * @return a person object with all of it's data
   */
  public Person getPerson(String personID) throws DataAccessException{
    Person person;
    ResultSet rs = null;
    String sql = "SELECT * FROM persons WHERE personID = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)){
      stmt.setString(1, personID);
      rs = stmt.executeQuery();

      if (rs.next()){
        person = new Person(rs.getString("personID"), rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
        return person;
      }
    } catch (SQLException e){
      e.printStackTrace();
      throw new DataAccessException("Error encountered while getting person from database");
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    return null;
  }

  /**
   * clears the person table
   */
  public void clear() throws DataAccessException{
    if (!getTable().isEmpty()) {
      try (Statement stmt=conn.createStatement()) {
        String sql="DELETE FROM persons";
        stmt.executeUpdate(sql);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new DataAccessException("Error encountered while clearing persons from database");
      }
    }
  }

  /**
   * Gets the person table from the database
   * @return an array of all the persons in the table
   * @throws DataAccessException
   */
  public ArrayList<Person> getTable() throws DataAccessException {
    Person person;
    ArrayList<Person> persons = new ArrayList<>();
    ResultSet rs = null;
    String sql = "SELECT * FROM persons;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)){
      rs = stmt.executeQuery();

      while (rs.next()){
          person = new Person(rs.getString("personID"), rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));

        persons.add(person);
      }
    } catch (SQLException e){
      e.printStackTrace();
      throw new DataAccessException("Error encountered while getting person table from database");
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    return persons;
  }

  /**
   * Gets all the persons associated with a username
   * @return an array of persons associated with a username
   * @throws DataAccessException
   */
  public ArrayList<Person> getPersonsForUser(String username) throws DataAccessException {
    Person person;
    ArrayList<Person> persons = new ArrayList<>();
    ResultSet rs = null;
    String sql = "SELECT * FROM persons WHERE username = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)){
      stmt.setString(1, username);
      rs = stmt.executeQuery();

      while (rs.next()){
        person = new Person(rs.getString("personID"), rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));

        persons.add(person);
      }
    } catch (SQLException e){
      e.printStackTrace();
      throw new DataAccessException("Error encountered while getting person table from database");
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    return persons;
  }

}
