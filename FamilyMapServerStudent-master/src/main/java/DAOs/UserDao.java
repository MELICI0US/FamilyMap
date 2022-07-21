package DAOs;

import Exceptions.DataAccessException;
import Models.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data access class for user
 */
public class UserDao {
  private final Connection conn;

  public UserDao(Connection conn)
  {
    this.conn = conn;
  }
  /**
   * Adds a user to the database
   * @param user the user you are adding
   */
  public void addUser(User user) throws DataAccessException {
    String sql = "INSERT INTO users (username, password, email, firstName, " +
            "lastName, gender, personID) VALUES(?,?,?,?,?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, user.getUsername());
      stmt.setString(2, user.getPassword());
      stmt.setString(3, user.getEmail());
      stmt.setString(4, user.getFirstName());
      stmt.setString(5, user.getLastName());
      stmt.setString(6, user.getGender());
      stmt.setString(7, user.getPersonID());

      stmt.executeUpdate();
    } catch (SQLException e) {

      throw new DataAccessException(e.getMessage() + "Error encountered while inserting user into the database");
    }
  }

  /**
   * Queries the database for the user
   * @return a user object with all of it's data
   */
  public User getUser(String personID)throws DataAccessException{
    User user;
    ResultSet rs = null;
    String sql = "SELECT * FROM users WHERE personID = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)){
      stmt.setString(1, personID);
      rs = stmt.executeQuery();

      if (rs.next()){
        user = new User(rs.getString("username"), rs.getString("password"),  rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("personID"));
        return user;
      }
    } catch (SQLException e){
      e.printStackTrace();
      throw new DataAccessException("Error encountered while getting user from database");
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
   * Queries the database for the user by username
   * @return a user object with all of it's data
   */
  public User getUserByUsername(String username) throws DataAccessException{
    User user;
    ResultSet rs = null;
    String sql = "SELECT * FROM users WHERE username = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)){
      stmt.setString(1, username);
      rs = stmt.executeQuery();

      if (rs.next()){
        user = new User(rs.getString("username"), rs.getString("password"),  rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("personID"));
        return user;
      }
    } catch (Exception e){
      e.printStackTrace();
      throw new DataAccessException("Error encountered while getting user from database");
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
   * clears the user table
   */
  public void clear() throws DataAccessException{
    if (!getTable().isEmpty()) {
      try (Statement stmt=conn.createStatement()) {
        String sql="DELETE FROM users";
        stmt.executeUpdate(sql);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new DataAccessException("Error encountered while clearing users from database");
      }
    }
  }

  /**
   * Gets the user table from the database
   * @return an array of all the users in the table
   * @throws DataAccessException
   */
  public ArrayList<User> getTable() throws DataAccessException {
    User user;
    ArrayList<User> users = new ArrayList<>();
    ResultSet rs = null;
    String sql = "SELECT * FROM users;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)){
      rs = stmt.executeQuery();

      while (rs.next()){
        user = new User(rs.getString("username"), rs.getString("password"),  rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("personID"));

        users.add(user);
      }
    } catch (SQLException e){
      e.printStackTrace();
      throw new DataAccessException("Error encountered while getting user table from database");
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    return users;
  }
}
