package dataaccess.SQL;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGame implements GameDAO {

    public SQLGame() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess_db");
            createDbStatement.execute();
            var createGameTable = """
            CREATE TABLE IF NOT EXISTS gameTable (
                gameID INT NOT NULL,
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

    void insertGame(
        Connection conn, int gameID, String whiteUserName, String blackUserName, String gameName, String game
        ) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("INSERT INTO gameTable (gameID, whiteUserName, blackUserName, gameName, game) VALUES(?, ?, ?, ?, ?)")){
            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, whiteUserName);
            preparedStatement.setString(3, blackUserName);
            preparedStatement.setString(4, gameName);
            preparedStatement.setString(5, game);
            preparedStatement.execute();
        }
    }

    void updateGame(Connection conn, int gameID, String whiteUserName, String blackUserName, String gameName, String game) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("UPDATE gameTable SET whiteUserName = ?, blackUserName = ?, gameName = ?, game = ?, WHERE gameID = ?")) {
            preparedStatement.setString(1, whiteUserName);
            preparedStatement.setString(2, blackUserName);
            preparedStatement.setString(3, gameName);
            preparedStatement.setString(4, game);
            preparedStatement.setInt(5, gameID);
            preparedStatement.execute();
        }
    }

    GameData queryGame(Connection conn, int gameID) throws SQLException {//gameID vs userName
        try (var preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameTable WHERE gameID = ?")) {
            preparedStatement.setInt(1, gameID);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var serializer = new Gson();
                    var gameIDxtra = resultSet.getInt("gameID");
                    var whiteUserName = resultSet.getString("whiteUsername");
                    var blackUserName = resultSet.getString("blackUsername");
                    var gameName = resultSet.getString("gameName");
                    var game = resultSet.getString("game");
                    return new GameData(gameIDxtra,whiteUserName,blackUserName,gameName,serializer.fromJson(game, ChessGame.class));
                } else {
                    return null;
                }
            }
        }
    }

    void clearGame(Connection conn) throws SQLException {
        try (var ps = conn.prepareStatement("TRUNCATE TABLE gameTable")) {
            ps.executeUpdate();
        }
    }

    List<GameData> listGames(Connection conn) throws SQLException {
        ArrayList<GameData> games = new ArrayList<>();
        try (var ps = conn.prepareStatement("SELECT gameID FROM gameTable")) {
            try (var resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    var gameIDxtra = resultSet.getInt("gameID");
                    games.add(queryGame(conn, gameIDxtra));
                }
            }
        }
        return games;
    }


    @Override
    public List<GameData> getGames() throws DataAccessException { //Maybe GameData instead?
        try (var conn = DatabaseManager.getConnection()){
            return listGames(conn);
        } catch (SQLException e) {
            throw new DataAccessException("Error: IDK");
        }
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        var serializer = new Gson();
        try (var conn = DatabaseManager.getConnection()){
            insertGame(conn, game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), serializer.toJson(game.game()));
        } catch (SQLException e) {
            throw new DataAccessException("Error: IDK");
        }
    }

    @Override
    public GameData findGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            return queryGame(conn, gameID);
        } catch (SQLException e) {
            throw new DataAccessException("Error: IDK");
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        var serializer = new Gson();
        try (var conn = DatabaseManager.getConnection()){
            updateGame(conn, gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), serializer.toJson(gameData.game()));
        } catch (SQLException e) {
            throw new DataAccessException("Error: IDK");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            clearGame(conn);
        } catch (SQLException e) {
            throw new DataAccessException("Error: IDK");
        }
    }
}
