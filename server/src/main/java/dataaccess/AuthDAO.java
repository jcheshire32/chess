package dataaccess;

import model.AuthData;

public class AuthDAO {
    void createAuth(AuthData authToken) throws DataAccessException{}
    getAuth(AuthData authToken) throws DataAccessException{}
    void deleteAuth(AuthData authToken) throws DataAccessException{}
    void clear();
}
