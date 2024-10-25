package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUser implements UserDAO {
    Map<String, UserData> users = new HashMap<>();
    private static final MemoryUser Instance = new MemoryUser();

    public static MemoryUser getInstance() {
        return Instance;
    }

    public MemoryUser() {
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (users.containsKey(username)){
            return users.get(username);
        }
        throw new DataAccessException("User not found");
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if (users.containsKey(userData.username())){
            throw new DataAccessException("Username taken");
        }
        users.put(userData.username(), userData);
    }

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }
}
