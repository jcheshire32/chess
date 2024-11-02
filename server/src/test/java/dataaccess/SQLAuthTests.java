package dataaccess;

import dataaccess.SQL.SQLAuth;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SQLAuthTests {

    private SQLAuth sqlAuth;

    @BeforeEach
    public void setUp() throws DataAccessException {
        sqlAuth = new SQLAuth();
    }

    @Test
    @DisplayName("Create Auth Success")
    public void createAuthTest() throws DataAccessException {
        AuthData testAuth = new AuthData("asdf", "Jim");
        sqlAuth.createAuth(testAuth);
        var guy = sqlAuth.getAuth("asdf");
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
        AuthData testAuth = new AuthData("asdf", "Jim");
        sqlAuth.createAuth(testAuth);
        var guy = sqlAuth.getAuth("asdf");
        Assertions.assertEquals(testAuth.username(), guy);
    }

    @Test
    @DisplayName("Get Auth Fail")
    public void getAuthFailTest() throws DataAccessException {
        AuthData testAuth = new AuthData("asdf", "Jim");
        sqlAuth.createAuth(testAuth);
        Assertions.assertNull(sqlAuth.getAuth("qwerty"));
    }

    @Test
    @DisplayName("Delete Auth Success")
    public void deleteAuthTest() throws DataAccessException {
        AuthData testAuth = new AuthData("asdf", "Jim");
        sqlAuth.createAuth(testAuth);
        sqlAuth.deleteAuth("asdf");
        Assertions.assertNull(sqlAuth.getAuth("asdf"));
    }

    @Test
    @DisplayName("Delete Auth Fail")
    public void deleteAuthFailTest() throws DataAccessException {
        AuthData testAuth = new AuthData("asdf", "Jim");
        sqlAuth.createAuth(testAuth);
        Assertions.assertThrows(DataAccessException.class, () -> sqlAuth.deleteAuth(null));
    }

    @Test
    @DisplayName("Clear Auth Success")
    public void clearAuthTest() throws DataAccessException {
        AuthData testAuth = new AuthData("asdf", "Jim");
        sqlAuth.createAuth(testAuth);
        sqlAuth.clear();
        Assertions.assertNull(sqlAuth.getAuth("asdf"));
    }
}
