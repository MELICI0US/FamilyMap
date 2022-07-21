package Common;

import DAOs.AuthTokenDao;
import DAOs.EventDao;
import DAOs.PersonDao;
import DAOs.UserDao;
import Exceptions.DataAccessException;

import java.sql.Connection;

public class Clear {
  public void clear(Connection conn) throws DataAccessException {
    PersonDao personDao = new PersonDao(conn);
    UserDao userDao = new UserDao(conn);
    EventDao eventDao = new EventDao(conn);
    AuthTokenDao authTokenDao = new AuthTokenDao(conn);

    personDao.clear();
    userDao.clear();
    eventDao.clear();
    authTokenDao.clear();
  }
}
