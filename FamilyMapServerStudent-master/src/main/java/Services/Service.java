package Services;

import Exceptions.DataAccessException;
import DAOs.Database;

import java.sql.Connection;

public class Service {
  protected Database database;
  protected Connection conn;

  protected void connect() {
    try {
      database = new Database();
      conn = database.openConnection();
    } catch (DataAccessException e) {
      e.printStackTrace();
    }
  }

  protected void disconnect() {
    try {
      database.closeConnection(true);
      conn = null;
    } catch (DataAccessException e) {
      e.printStackTrace();
    }
  }
}
