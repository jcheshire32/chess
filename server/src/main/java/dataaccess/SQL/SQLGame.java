package dataaccess.SQL;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGame implements GameDAO {

    public SQLGame() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess_db");
            createDbStatement.execute();
            //FIX LAST TABLE VAR
            var createGameTable = """
            CREATE TABLE IF NOT EXISTS gameTable (
                gameID INT NOT NULL AUTO_INCREMENT,
                whiteUsername VARCHAR(255) DEFAULT NULL,
                blackUsername VARCHAR(255) DEFAULT NULL,
                gameName VARCHAR(255) NOT NULL,
                game TEXT NOT NULL,
                PRIMARY KEY (gameID)
            )""";
            try (var createTableStatement = conn.prepareStatement(createGameTable)){
                createTableStatement.execute();
            }
        } catch (SQLException e) {
            // handle it...can i just use stacktrace?
        }
    }

    int insertGame(
        Connection conn, String whiteUserName, String blackUserName, String gameName, String game
        ) throws SQLException, DataAccessException { //is game a string or JSON?
        var statement = "INSERT INTO gameTable (whiteUserName, blackUserName, gameName, game) VALUES(?, ?, ?, ?)";
        return executeUpdate(statement, whiteUserName, blackUserName, gameName, game);//aka gameID
    }

    void updateGame(Connection conn, int gameID, String whiteUserName, String blackUserName, String gameName, String game) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("UPDATE gameTable SET gameID = ?, whiteUserName = ?, blackUserName = ?, gameName = ?, game = ?, WHERE id = ?")) {
            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, whiteUserName);
            preparedStatement.setString(3, blackUserName);
            preparedStatement.setString(4, gameName);
            preparedStatement.setString(5, game);
            preparedStatement.execute();
        } catch (SQLException e) {
            //handle
        }
    }

    void deleteGame(Connection conn, int gameID) throws SQLException { // I don't think this one should be userName
        try (var preparedStatement = conn.prepareStatement("DELETE FROM gameTable WHERE gameID = ?")) {
            preparedStatement.setInt(1, gameID);
            preparedStatement.execute();
        } catch (SQLException e) {
            //handle
        }
    }

    GameData queryGame(Connection conn, int gameID) throws SQLException {//gameID vs userName
        try (var preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameTable WHERE gameID = ?")) {
            preparedStatement.setInt(1, gameID);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var gameIDxtra = resultSet.getInt("gameID");
                    var whiteUserName = resultSet.getString("whiteUsername");
                    var blackUserName = resultSet.getString("blackUsername");
                    var gameName = resultSet.getString("gameName");
                    var game = resultSet.getObject("game", ChessGame.class);
                    return new GameData(gameIDxtra,whiteUserName,blackUserName,gameName,game);
                } else {
                    return null;
                }
            }
        }
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: IDK");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<GameData> getGames() throws DataAccessException { //Maybe GameData instead?
        return List.of();
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        var serializer = new Gson();
        try (var conn = DatabaseManager.getConnection()){
            insertGame(conn, game.whiteUsername(), game.blackUsername(), game.gameName(), serializer.toJson(game.game()));
        } catch (SQLException e) {
            //handle
        }
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
