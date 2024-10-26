package dataaccess.SQL;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

public class SQLUser implements UserDAO {
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
