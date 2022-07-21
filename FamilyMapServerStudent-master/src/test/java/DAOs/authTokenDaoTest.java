package DAOs;

import Exceptions.DataAccessException;
import Models.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class authTokenDaoTest {
  Connection conn;
  Database database=new Database();
  AuthTokenDao authDao;


  @BeforeEach
  void setUp() throws DataAccessException {
    conn=database.openConnection();
    database.clearTables();
    authDao=new AuthTokenDao(conn);
  }

  @AfterEach
  void tearDown() throws DataAccessException{
    database.closeConnection(false);
    conn=null;
  }

  @Test
  void addAuthPass() throws DataAccessException {
    AuthToken auth1=new AuthToken("aaaa");
    AuthToken auth2=new AuthToken("bbbb");
    AuthToken auth3=new AuthToken("cccc");

    authDao.addAuthtoken(auth1);
    authDao.addAuthtoken(auth2);
    authDao.addAuthtoken(auth3);

    assertTrue(authDao.validateToken(auth1));
    assertTrue(authDao.validateToken(auth2));
    assertTrue(authDao.validateToken(auth3));
  }

  @Test
  void addAuthFail() throws DataAccessException {
    AuthToken auth1=new AuthToken("aaaa");

    authDao.addAuthtoken(auth1);

    assertThrows(DataAccessException.class, ()-> authDao.addAuthtoken(auth1));
  }

  @Test
  void getAuthPass() throws DataAccessException {
    AuthToken auth1=new AuthToken("aaaa");
    AuthToken auth2=new AuthToken("bbbb");
    AuthToken auth3=new AuthToken("cccc");

    authDao.addAuthtoken(auth1);
    authDao.addAuthtoken(auth2);
    authDao.addAuthtoken(auth3);

    assertTrue(authDao.validateToken(auth1));
    assertTrue(authDao.validateToken(auth2));
    assertTrue(authDao.validateToken(auth3));
  }

  @Test
  void getAuthFail() throws DataAccessException {
    AuthToken auth1=new AuthToken("aaaa");
    //not adding token to the database so it should not be found
    assertFalse(authDao.validateToken(auth1));
  }

  @Test
  void clear() throws DataAccessException{
    AuthToken auth1=new AuthToken("aaaa");
    AuthToken auth2=new AuthToken("bbbb");
    AuthToken auth3=new AuthToken("cccc");

    authDao.addAuthtoken(auth1);
    authDao.addAuthtoken(auth2);
    authDao.addAuthtoken(auth3);

    authDao.clear();

    assertTrue(authDao.getTable().isEmpty());

  }

  @Test
  void getUsernamePass() throws DataAccessException{
    AuthToken auth1=new AuthToken("aaaa","user1");
    AuthToken auth2=new AuthToken("bbbb", "user1");
    AuthToken auth3=new AuthToken("cccc", "user2");

    authDao.addAuthtoken(auth1);
    authDao.addAuthtoken(auth2);
    authDao.addAuthtoken(auth3);

    assertEquals("user1", authDao.getUsername(auth1));
    assertEquals("user1", authDao.getUsername(auth2));
    assertEquals("user2", authDao.getUsername(auth3));
  }

  @Test
  void getUsernameFail() throws DataAccessException {
    AuthToken auth1=new AuthToken("fakeToken","user1");

    assertNull(authDao.getUsername(auth1));
  }
}