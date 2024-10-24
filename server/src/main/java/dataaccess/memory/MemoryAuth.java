package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuth implements AuthDAO {
    //authToken
    //username
    Map<String, String> authDataMap = new HashMap<>();
    private static final MemoryAuth instance = new MemoryAuth();

    public static MemoryAuth getInstance() {
        return instance;
    }

    public MemoryAuth() {
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        authDataMap.put(authData.authToken(), authData.username());
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException {
        return authDataMap.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authDataMap.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        authDataMap.clear();
    }
}
