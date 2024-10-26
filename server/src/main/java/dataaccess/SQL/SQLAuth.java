package dataaccess.SQL;

import com.mysql.cj.xdevapi.Table;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLAuth implements AuthDAO {

    public SQLAuth() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT 1+1")) {//SQL code for making a table. BIG STRING. look at petshop
                var rs = preparedStatement.executeQuery(); //send command to SQL
                rs.next();
                System.out.println(rs.getInt(1));
            }
        } catch (DataAccessException | SQLException e){
            //handle it
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
