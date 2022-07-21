package DAOs;

import Exceptions.DataAccessException;
import Models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class userDaoTest {
  Connection conn;
  Database database=new Database();
  UserDao userDao;


  @BeforeEach
  void setUp() throws DataAccessException {
      conn=database.openConnection();
      database.clearTables();
      userDao=new UserDao(conn);
  }

  @AfterEach
  void tearDown() throws DataAccessException{
      database.closeConnection(false);
      conn=null;
  }

  @Test
  void addUserPass() throws DataAccessException {
    User user=new User("user1", "pass123", "user1@user.com", "John", "Doe", "m", "abc123");
    User user2=new User("user2", "password123", "user2@user.com", "Jane", "Doe", "f", "123abc");
    User user3=new User("user3", "word123", "user3@user.com", "Jim", "Doe", "m", "1234abcd");

    userDao.addUser(user);
    userDao.addUser(user2);
    userDao.addUser(user3);

    assertEquals(user, userDao.getUser(user.getPersonID()));
    assertEquals(user2, userDao.getUser(user2.getPersonID()));
    assertEquals(user3, userDao.getUser(user3.getPersonID()));

  }

  @Test
  void addUserFail() throws DataAccessException {
    User user=new User("user1", "pass123", "user1@user.com", "John", "Doe", "m", "abc123");

    userDao.addUser(user);

    assertThrows(DataAccessException.class, ()->userDao.addUser(user));
  }

  @Test
  void getUserPass() throws DataAccessException {
    User user=new User("user3", "pass123", "user3@user.com", "Joe", "Doey", "m", "xyz321");
    User user2=new User("user4", "password123", "user4@user.com", "Julia", "Doey", "f", "321xyz");
    User user3=new User("user5", "word123", "user5@user.com", "Jimothy", "Doey", "m", "4321wxyz");

    userDao.addUser(user);
    userDao.addUser(user2);
    userDao.addUser(user3);

    assertEquals(user, userDao.getUser(user.getPersonID()));
    assertEquals(user2, userDao.getUser(user2.getPersonID()));
    assertEquals(user3, userDao.getUser(user3.getPersonID()));
  }

  @Test
  void getUserFail() throws DataAccessException {
    assertNull(userDao.getUser("notRealID"));
  }

  @Test
  void clear() throws DataAccessException{
    User user=new User("user1", "pass123", "user1@user.com", "John", "Doe", "m", "abc123");
    User user2=new User("user2", "password123", "user2@user.com", "Jane", "Doe", "f", "123abc");
    User user3=new User("user3", "word123", "user3@user.com", "Jim", "Doe", "m", "1234abcd");

    userDao.addUser(user);
    userDao.addUser(user2);
    userDao.addUser(user3);

    userDao.clear();

    assertTrue(userDao.getTable().isEmpty());
  }

  @Test
  void getUserByUsernamePass() throws DataAccessException{
    User user=new User("user3", "pass123", "user3@user.com", "Joe", "Doey", "m", "xyz321");
    User user2=new User("user4", "password123", "user4@user.com", "Julia", "Doey", "f", "321xyz");
    User user3=new User("user5", "word123", "user5@user.com", "Jimothy", "Doey", "m", "4321wxyz");

    userDao.addUser(user);
    userDao.addUser(user2);
    userDao.addUser(user3);

    assertEquals(user, userDao.getUserByUsername("user3"));
    assertEquals(user2, userDao.getUserByUsername("user4"));
    assertEquals(user3, userDao.getUserByUsername("user5"));
  }

  @Test
  void getUserByUsernameFail() throws DataAccessException{
    assertNull(userDao.getUserByUsername("FakeUsername"));
  }
}