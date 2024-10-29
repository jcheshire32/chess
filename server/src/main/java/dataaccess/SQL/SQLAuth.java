package dataaccess.SQL;

import com.mysql.cj.xdevapi.Table;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLAuth implements AuthDAO {

    public SQLAuth() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess_db");
            createDbStatement.execute();
            var createAuthTable = """
            CREATE TABLE IF NOT EXISTS authTable (
                id INT NOT NULL AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                type VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            )""";
            try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e){
            //handle it
        }
    }
    //close the connection
    //try-with-resource

    //check the suggested imports with TA
    //what return type?
    int insertAuth(Connection conn, String userName, String authToken) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("INSERT INTO authTable (userName, authToken) VALUES(?, ?)", RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, authToken);

            preparedStatement.executeUpdate();

            var resultSet = preparedStatement.getGeneratedKeys();
            var ID = 0;
            if (resultSet.next()) {
                ID = resultSet.getInt(1); //keep this stuff?
            }

            return ID;
        }
    }

    void updateAuth(Connection conn, String userName, String authToken) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("UPDATE authTable SET userName = ?, authToken = ? WHERE id = ?")) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, authToken);

            preparedStatement.executeUpdate();
        }

    }

    void deleteAuth(Connection conn, String userName) throws SQLException {
        try (var preparedStatement = conn.prepareStatement("DELETE FROM authTable WHERE id = ?")) {
            preparedStatement.setString(1, userName);
            preparedStatement.executeUpdate();
        }
    }

    void queryAuth(Connection conn, String findType, String authToken) throws SQLException { //petshop example just says findtype but
        try (var preparedStatement = conn.prepareStatement("SELECT id, username, type FROM authTable WHERE id = ?")) { //not sure what to select
            preparedStatement.setString(1, findType);
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getInt("id");
                    var userName = rs.getString("username");
                    var type = rs.getString("type");

                    System.out.printf("id: %d, username: %s, type: %s%n", id, userName, type);
                }
            }
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public String getAuth(String authToken) throws DataAccessException {
        return "";
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
