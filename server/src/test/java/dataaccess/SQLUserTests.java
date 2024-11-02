package dataaccess;

import dataaccess.SQL.SQLUser;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SQLUserTests {

    private SQLUser sqlUser;

    @BeforeEach
    public void setUp() throws SQLException, DataAccessException {
        sqlUser = new SQLUser();
    }

    @Test
    @DisplayName("Create User Success")
    public void createUserSuccess() throws DataAccessException {
        UserData testUser = new UserData("jim","jim","jim");
        sqlUser.createUser(testUser);
        var guy = sqlUser.getUser("jim");
        Assertions.assertEquals(testUser.password(), guy.password());
    }

    @Test
    @DisplayName("Create User Fail")
    public void createUserFail() {
        UserData testUser = new UserData("Jim","jim","jim");
        Assertions.assertThrows(DataAccessException.class, () -> sqlUser.createUser(testUser));
    }

    @Test
    @DisplayName("Get User Success")
    public void getUserSuccess() throws DataAccessException {
        UserData testUser = new UserData("jim","jim","jim");
        sqlUser.createUser(testUser);
        var guy = sqlUser.getUser("jim");
        Assertions.assertEquals(testUser, guy);
    }


    @Test
    @DisplayName("Get User Fail")
    public void getUserFail() {
        Assertions.assertThrows(DataAccessException.class, () -> sqlUser.getUser(null));
    }


    @Test
    @DisplayName("Clear Success")
    public void clearSuccess() throws DataAccessException {
        UserData testUser = new UserData("jim","jim","jim");
        sqlUser.createUser(testUser);
        sqlUser.clear();
        Assertions.assertNull(sqlUser.getUser("jim"));
    }
}
