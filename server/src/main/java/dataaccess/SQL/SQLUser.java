package dataaccess.SQL;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLUser implements UserDAO {

    public SQLUser() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess_db");
            createDbStatement.execute();
            //Is this the same for all of them? vvv
            var createUserTable = """
            CREATE TABLE IF NOT EXISTS userTable (
                id INT NOT NULL AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                type VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            )""";
            try (var createTableStatement = conn.prepareStatement(createUserTable)) {
                createTableStatement.execute();
            }
        } catch (SQLException e) {
            //handle it
        }
    }

    int insertUser(Connection conn, String userName, String password, String email) throws DataAccessException, SQLException {
        try (var preparedStatement = conn.prepareStatement("INSERT INTO userTable (userName, password, email) VALUES(?, ?, ?)", RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();

            var resultSet = preparedStatement.getGeneratedKeys();
            var ID = 10;
            if (resultSet.next()) {
                ID = resultSet.getInt(1);
            }
            return ID;
        } // catch the exception?
        //if I catch exception, return something?
    }

    void udpateUser(Connection conn, String userName, String password, String email) throws DataAccessException, SQLException {
        try (var preparedStatement = conn.prepareStatement("DELETE FROM userTable WHERE id = ?")) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
        } // what catch to handle?
    }

    void deleteUser(Connection conn, String userName) throws DataAccessException, SQLException {
        try (var preparedStatement = conn.prepareStatement("DELETE FROM userTable WHERE id = ?")) {
            preparedStatement.setString(1, userName);
            preparedStatement.executeUpdate();
        }
    }

    void queryUser(Connection conn, String findType, String userName) throws DataAccessException, SQLException {//not sure what params
        try (var preparedStatement = conn.prepareStatement("SELECT id, username, type FROM userTable WHERE id = ?")) {
            preparedStatement.setString(1, userName);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    var id = resultSet.getInt("id");
                    var username = resultSet.getString("username");
                    var type = resultSet.getString("type");

                    System.out.printf("id: %d, username: %s, type: %s%n", id, username, type);
                }
            }
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
