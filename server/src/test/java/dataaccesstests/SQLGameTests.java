package dataaccesstests;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SQL.SQLGame;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SQLGameTests {

    private SQLGame sqlGame;

    @BeforeEach
    public void setUp() throws SQLException, DataAccessException {
        sqlGame = new SQLGame();
        sqlGame.clear();
    }

    @Test
    @DisplayName("Get Games Success")
    public void testGetGames() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(3, "Jim", "John", "dagame", chessGame);
        sqlGame.createGame(gameData);
        var games = sqlGame.getGames();
        Assertions.assertNotNull(games);
        Assertions.assertTrue(!games.isEmpty());
    }

    @Test
    @DisplayName("Get Games Fail")
    public void testGetGamesFail() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(3, "steve", "John", "idk", chessGame);
        sqlGame.createGame(gameData);
        var games = sqlGame.getGames();
        Assertions.assertNotNull(games);
    }

    @Test
    @DisplayName("Create Game Success")
    public void testCreateGame() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(3, "steve", "John", "idk", chessGame);
        sqlGame.createGame(gameData);
        Assertions.assertNotNull(sqlGame.getGames());
    }

    @Test
    @DisplayName("Create Game Fail")
    public void testCreateGameFail() throws dataaccess.DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(2, "Jim", "John", "dagame", chessGame);
        GameData gameData2 = new GameData(2, "Jim", "John", "dagame", chessGame);
        sqlGame.createGame(gameData);
        Assertions.assertThrows(DataAccessException.class, () -> sqlGame.createGame(gameData2));
    }

    @Test
    @DisplayName("Find Game Success")
    public void testFindGame() throws dataaccess.DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(2, "steve", "Phil", "idk", chessGame);
        sqlGame.createGame(gameData);
        GameData thisGame = sqlGame.findGame(2);
        Assertions.assertNotNull(thisGame);

    }

    @Test
    @DisplayName("Find Game Fail")
    public void testFindGameFail() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(3, "Pete", "John", "bruh", chessGame);
        sqlGame.createGame(gameData);
        GameData thisGame = sqlGame.findGame(3);
        Assertions.assertNotNull(thisGame);
    }

    @Test
    @DisplayName("Clear Game Success")
    public void testClearGame() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(3, "steve", "John", "idk", chessGame);
        sqlGame.createGame(gameData);
        sqlGame.clear();
        var games = sqlGame.getGames();
        Assertions.assertTrue(games.isEmpty());
    }

}
