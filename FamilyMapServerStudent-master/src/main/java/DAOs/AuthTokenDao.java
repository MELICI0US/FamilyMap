package DAOs;

import Exceptions.DataAccessException;
import Models.AuthToken;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data access class for authorization token
 */
public class AuthTokenDao {
  private final Connection conn;

  public AuthTokenDao(Connection conn) {
    this.conn = conn;
  }


  /**
   * Adds and authtoken to the database
   *
   * @param authtoken the token to be added
   */
  public void addAuthtoken(AuthToken authtoken) throws DataAccessException {

    String sql = "INSERT INTO authTokens (authToken, username) VALUES(?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, authtoken.getToken());
      stmt.setString(2, authtoken.getUsername());

      stmt.executeUpdate();
    } catch (SQLException e) {

      throw new DataAccessException(e.getMessage() + "Error encountered while inserting authtoken into the database");
    }
  }

  /**
   * Checks the database for a valid token person pair
   *
   * @param authtoken the authorization token
   * @return true if it is a valid token
   */
  public boolean validateToken(AuthToken authtoken)
          throws DataAccessException {
    ResultSet rs = null;
    String sql = "SELECT * FROM authTokens WHERE authToken = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, authtoken.getToken());
      rs = stmt.executeQuery();

      if (rs.next()) {
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while validating token");
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    return false;
  }

  public String getUsername(AuthToken authtoken) throws DataAccessException {
    ResultSet rs = null;
    String sql = "SELECT * FROM authTokens WHERE authToken = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, authtoken.getToken());
      rs = stmt.executeQuery();

      if (rs.next()) {
        return rs.getString("username");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while getting username from database");
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

  public void clear() throws DataAccessException {
    if (!getTable().isEmpty()) {
      try (Statement stmt = conn.createStatement()) {
        String sql = "DELETE FROM authTokens";
        stmt.executeUpdate(sql);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new DataAccessException("Error encountered while clearing authTokens from database");
      }
    }
  }

  /**
   * Gets the authtoken table from the database
   *
   * @return an array of all the authtokens in the table
   * @throws DataAccessException
   */
  public ArrayList<AuthToken> getTable() throws DataAccessException {
    AuthToken authToken;
    ArrayList<AuthToken> users = new ArrayList<>();
    ResultSet rs = null;
    String sql = "SELECT * FROM authTokens;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      rs = stmt.executeQuery();

      while (rs.next()) {
        authToken = new AuthToken(rs.getString("username"));

        users.add(authToken);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while getting authToken table from database");
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
