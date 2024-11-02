package dataaccess.SQL;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;


public class SQLAuth implements AuthDAO {

    public SQLAuth() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess_db");
            createDbStatement.execute();
            var createAuthTable = """
            CREATE TABLE IF NOT EXISTS authTable (
                username VARCHAR(255) NOT NULL,
                authToken VARCHAR(255) NOT NULL,
                PRIMARY KEY (authToken)
            )""";
            try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: IDK");
        }
    }

    void insertAuth(Connection conn, String userName, String authToken) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("INSERT INTO authTable (userName, authToken) VALUES(?, ?)")) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, authToken);

            preparedStatement.executeUpdate();
        }
    }

    void deleteAuth(Connection conn, String userName) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("DELETE FROM authTable WHERE authToken = ?")) {
            preparedStatement.setString(1, userName);
            preparedStatement.executeUpdate();
        }
    }

    AuthData queryAuth(Connection conn, String authToken) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("SELECT username, authToken FROM authTable WHERE authToken = ?")) {
            preparedStatement.setString(1, authToken);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var userName = resultSet.getString("username");
                    var authToken1 = resultSet.getString("authToken");

                    return new AuthData(authToken1, userName);
                } else {
                    return null;
                }
            }
        }
    }

    void clearAuth(Connection conn) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE authTable")) {
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            insertAuth(conn, authData.username(), authData.authToken());
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var auth = queryAuth(conn, authToken);
            if (auth != null){
                return auth.username();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            deleteAuth(conn, authToken);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            clearAuth(conn);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
