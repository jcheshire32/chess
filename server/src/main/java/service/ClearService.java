package service;


import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.memory.MemoryAuth;
import dataaccess.memory.MemoryGame;
import dataaccess.memory.MemoryUser;

public class ClearService {
    //get dao instance and clear
    public void clear() throws OtherException{
        AuthDAO authDAO = MemoryAuth.getInstance();
        UserDAO userDAO = MemoryUser.getInstance();
        GameDAO gameDAO = MemoryGame.getInstance();
        try {
            authDAO.clear();
            userDAO.clear();
            gameDAO.clear();
        } catch (DataAccessException e) {
            throw new OtherException("Error: Issue with clearing data");
        }
    }
}
