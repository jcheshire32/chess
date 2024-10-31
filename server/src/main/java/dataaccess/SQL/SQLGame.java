package dataaccess.SQL;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGame implements GameDAO {

    public SQLGame() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess_db");
            createDbStatement.execute();
            var createGameTable = """
            CREATE TABLE IF NOT EXISTS gameTable (
                id INT NOT NULL AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                type VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            )""";
            try (var createTableStatement = conn.prepareStatement(createGameTable)){
                createTableStatement.execute();
            }
        } catch (SQLException e) {
            // handle it...can i just use stacktrace?
        }
    }

    int insertGame(
            Connection conn, int gameID, String whiteUserName, String blackUserName, String gameName, String game
            ) throws SQLException { //is game a string or JSON?
        try (var preparedStatement = conn.prepareStatement("INSERT INTO gameTable (gameID, whiteUserName, blackUserName, gameName, game) VALUES(?, ?, ?, ?, ?)", RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, whiteUserName);
            preparedStatement.setString(3, blackUserName);
            preparedStatement.setString(4, gameName);
            preparedStatement.setString(5, game);
            preparedStatement.execute();

            var resultSet = preparedStatement.getGeneratedKeys();
            var ID = 0;
            if (resultSet.next()) {
                ID = resultSet.getInt(1); //keep this stuff?
            }

            return ID;
        }
    }

    void updateGame(Connection conn, int gameID, String whiteUserName, String blackUserName, String gameName, String game) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("UPDATE gameTable SET gameID = ?, whiteUserName = ?, blackUserName = ?, gameName = ?, game = ?, WHERE id = ?")) {
            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, whiteUserName);
            preparedStatement.setString(3, blackUserName);
            preparedStatement.setString(4, gameName);
            preparedStatement.setString(5, game);
            preparedStatement.execute();
        }
    }

    void deleteGame(Connection conn, int gameID) throws SQLException { // I don't think this one should be userName
        try (var preparedStatement = conn.prepareStatement("DELETE FROM gameTable WHERE id = ?")) {
            preparedStatement.setInt(1, gameID);
            preparedStatement.execute();
        }
    }

    void queryGame(Connection conn, String findType, int gameID) throws SQLException {//gameID vs userName
        try (var preparedStatement = conn.prepareStatement("SELECT idkwhattoputhereanymore FROM gameTable WHERE gameID = ?")) {//fix
            preparedStatement.setInt(1, gameID);
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    //idk here either i feel like it can't be the same as before

                    //system.out.printf(asdfasdf);
                }
            }
        }
    }

    @Override
    public List<GameData> getGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {

    }

    @Override
    public GameData findGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
