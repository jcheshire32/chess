package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUser implements UserDAO {

    public SQLUser() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var createUserTable = """
            CREATE TABLE IF NOT EXISTS userTable (
                userName VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (userName)
            )""";
            try (var createTableStatement = conn.prepareStatement(createUserTable)) {
                createTableStatement.execute();
            }
        }
    }

    void insertUser(Connection conn, String userName, String password, String email) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("INSERT INTO userTable (userName, password, email) VALUES(?, ?, ?)")) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
        }
    }

    UserData queryUser(Connection conn, String userName) throws DataAccessException, SQLException {//not sure what params
        try (var preparedStatement = conn.prepareStatement("SELECT userName, password, email FROM userTable WHERE userName = ?")) {
            preparedStatement.setString(1, userName);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var username = resultSet.getString("username");
                    var password = resultSet.getString("password");
                    var email = resultSet.getString("email");

                    return new UserData(username, password, email);
                } else {
                    throw new DataAccessException("Error: User not found");
                }
            }
        }
    }

    void clearUser(Connection conn) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE userTable")) {
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            insertUser(conn, userData.username(), userData.password(), userData.email());
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            return queryUser(conn, username); // simplified from IJ
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    //Need clear to work to make the first one work
    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            clearUser(conn);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
