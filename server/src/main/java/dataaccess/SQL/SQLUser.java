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
                userName VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (userName)
            )""";
            try (var createTableStatement = conn.prepareStatement(createUserTable)) {
                createTableStatement.execute();
            }
        } catch (SQLException e) {
            //handle it
        }
    }

    void insertUser(Connection conn, String userName, String password, String email) throws DataAccessException, SQLException {
        try (var preparedStatement = conn.prepareStatement("INSERT INTO userTable (userName, password, email) VALUES(?, ?, ?)")) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            //handle
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
                    return null;
                }
            }//catch?
        }
    }

    void deleteUser(Connection conn, String userName) throws DataAccessException, SQLException {
        try (var preparedStatement = conn.prepareStatement("DELETE FROM userTable WHERE userName = ?")) {
            preparedStatement.setString(1, userName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            //handle
        }
    }

    //clear and delete are different though,

    void clearUser(Connection conn, String userName) throws DataAccessException, SQLException {
        try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE userTable")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            //handle
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            insertUser(conn, userData.username(), userData.password(), userData.email());
        } catch (SQLException e) {
            //handle
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            return queryUser(conn, username); // simplified from IJ
        } catch (SQLException e) {
            //handle
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
