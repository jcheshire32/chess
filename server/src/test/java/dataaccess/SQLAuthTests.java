package dataaccess;

import dataaccess.SQL.SQLAuth;
import model.AuthData;
import org.junit.jupiter.api.*;

public class SQLAuthTests {

    private SQLAuth sqlAuth;

    @BeforeEach
    public void setUp() throws DataAccessException {
        sqlAuth = new SQLAuth();
        sqlAuth.clear();
    }

    @Test
    @DisplayName("Create Auth Success")
    public void createAuthTest() throws DataAccessException {
        var token = "pdofipoi";
        AuthData testAuth = new AuthData(token, "Jim");
        sqlAuth.createAuth(testAuth);
        var guy = sqlAuth.getAuth(token);
        Assertions.assertEquals(testAuth.username(), guy);
    }

    @Test
    @DisplayName("Create Auth Fail")
    public void createAuthFailTest(){
        AuthData testAuth = new AuthData(null, "Jim");
        Assertions.assertThrows(DataAccessException.class, () -> sqlAuth.createAuth(testAuth));
    }

    @Test
    @DisplayName("Get Auth Success")
    public void getAuthTest() throws DataAccessException {
        AuthData testAuth = new AuthData("asdfg", "Jim");
        sqlAuth.createAuth(testAuth);
        var guy = sqlAuth.getAuth("asdfg");
        Assertions.assertEquals(testAuth.username(), guy);
    }

    @Test
    @DisplayName("Get Auth Fail")
    public void getAuthFailTest() throws DataAccessException {
        AuthData testAuth = new AuthData("asdfgh", "Jim");
        sqlAuth.createAuth(testAuth);
        Assertions.assertNull(sqlAuth.getAuth("qwerty"));
    }

    @Test
    @DisplayName("Delete Auth Success")
    public void deleteAuthTest() throws DataAccessException {
        var token = "asodifu";
        AuthData testAuth = new AuthData(token, "Jim");
        sqlAuth.createAuth(testAuth);
        sqlAuth.deleteAuth(token);
        Assertions.assertNull(sqlAuth.getAuth(token));
    }

    @Test
    @DisplayName("Delete Auth Fail")
    public void deleteAuthFailTest() throws DataAccessException {
        var token = "weoiuiouoouur";
        AuthData testAuth = new AuthData(token, "Jim");
        sqlAuth.createAuth(testAuth);
        sqlAuth.deleteAuth("Jim");
        Assertions.assertNotNull(sqlAuth.getAuth(token));
    }

    @Test
    @DisplayName("Clear Auth Success")
    public void clearAuthTest() throws DataAccessException {
        var token = "xnvc";
        AuthData testAuth = new AuthData(token, "Jim");
        sqlAuth.createAuth(testAuth);
        sqlAuth.clear();
        Assertions.assertNull(sqlAuth.getAuth(token));
    }
}
