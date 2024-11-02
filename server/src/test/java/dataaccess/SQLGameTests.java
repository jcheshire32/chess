package dataaccess;

import dataaccess.SQL.SQLGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SQLGameTests {

    private SQLGame sqlGame;

    @BeforeEach
    public void setUp() throws SQLException, DataAccessException {
        sqlGame = new SQLGame();
    }

    @Test
    @DisplayName("Get Games Success")
    public void testGetGames() {

    }

    @Test
    @DisplayName("Get Games Fail")
    public void testGetGamesFail() {

    }

    @Test
    @DisplayName("Create Game Success")
    public void testCreateGame() {

    }

    @Test
    @DisplayName("Create Game Fail")
    public void testCreateGameFail() {

    }

    @Test
    @DisplayName("Find Game Success")
    public void testFindGame() {

    }

    @Test
    @DisplayName("Find Game Fail")
    public void testFindGameFail() {

    }

    @Test
    @DisplayName("Clear Game Success")
    public void testClearGame() {

    }

}
