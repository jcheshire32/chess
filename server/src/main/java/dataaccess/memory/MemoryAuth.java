package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuth implements AuthDAO {
    //authToken
    //username
    Map<String, AuthData> authDataMap = new HashMap<>();
    private static final MemoryAuth instance = new MemoryAuth();

    public static MemoryAuth getInstance() {
        return instance;
    }

    private MemoryAuth() {
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        authDataMap.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
