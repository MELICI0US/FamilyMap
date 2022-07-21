package DAOs;

import Exceptions.DataAccessException;
import Models.Event;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data access class for event
 */
public class EventDao {
  private final Connection conn;

  public EventDao(Connection conn) {
    this.conn = conn;
  }

  /**
   * Adds an event to the database
   *
   * @param event the event to be added
   */
  public void addEvent(Event event) throws DataAccessException {
    //We can structure our string to be similar to a sql command, but if we insert question
    //marks we can change them later with help from the statement
    String sql = "INSERT INTO Events (eventID, username, personID, latitude, longitude, " +
            "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      //Using the statements built-in set(type) functions we can pick the question mark we want
      //to fill in and give it a proper value. The first argument corresponds to the first
      //question mark found in our sql String
      stmt.setString(1, event.getEventID());
      stmt.setString(2, event.getAssociatedUsername());
      stmt.setString(3, event.getPersonID());
      stmt.setFloat(4, event.getLatitude());
      stmt.setFloat(5, event.getLongitude());
      stmt.setString(6, event.getCountry());
      stmt.setString(7, event.getCity());
      stmt.setString(8, event.getEventType());
      stmt.setInt(9, event.getYear());

      stmt.executeUpdate();
    } catch (SQLException e) {
      //System.out.println(e.getMessage());
      throw new DataAccessException("Error encountered while inserting event into the database");
    }
  }

  /**
   * Queries the database for the event
   *
   * @return an event object with all of it's data
   */
  public Event getEvent(String eventID) throws DataAccessException {
    Event event;
    ResultSet rs = null;
    String sql = "SELECT * FROM Events WHERE EventID = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, eventID);
      rs = stmt.executeQuery();
      if (rs.next()) {
        event = new Event(rs.getString("eventID"), rs.getString("username"),
                rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                rs.getInt("year"));
        return event;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding event");
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
   * gets a list of events based on the person ID
   *
   * @param personID
   * @return the list of events associated with that id
   * @throws DataAccessException
   */
  public ArrayList<Event> getEventsByPersonID(String personID) throws DataAccessException {
    Event event;
    ArrayList<Event> events = new ArrayList<>();
    ResultSet rs = null;
    String sql = "SELECT * FROM Events WHERE personID = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, personID);
      rs = stmt.executeQuery();
      while (rs.next()) {
        event = new Event(rs.getString("eventID"), rs.getString("username"),
                rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                rs.getInt("year"));
        events.add(event);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding event");
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }

    }
    return events;
  }

  /**
   * clears the events table
   */
  public void clear() throws DataAccessException {
    if (!getTable().isEmpty()) {
      try (Statement stmt = conn.createStatement()) {
        String sql = "DELETE FROM events";
        stmt.executeUpdate(sql);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new DataAccessException("Error encountered while clearing events from database");
      }
    }
  }

  /**
   * Gets the event table from the database
   *
   * @return an array of all the events in the table
   * @throws DataAccessException
   */
  public ArrayList<Event> getTable() throws DataAccessException {
    Event event;
    ArrayList<Event> events = new ArrayList<>();
    ResultSet rs = null;
    String sql = "SELECT * FROM events;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      rs = stmt.executeQuery();

      while (rs.next()) {
        event = new Event(rs.getString("eventID"), rs.getString("username"),
                rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                rs.getInt("year"));
        events.add(event);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while getting events table from database");
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    return events;
  }

  /**
   * Gets all the events associated with a username
   *
   * @return an array of events associated with a username
   * @throws DataAccessException
   */
  public ArrayList<Event> getEventsForUser(String username) throws DataAccessException {
    Event event;
    ArrayList<Event> events = new ArrayList<>();
    ResultSet rs = null;
    String sql = "SELECT * FROM events WHERE username = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      rs = stmt.executeQuery();

      while (rs.next()) {
        event = new Event(rs.getString("eventID"), rs.getString("username"),
                rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                rs.getInt("year"));
        events.add(event);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while getting events table from database");
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    return events;
  }
}
