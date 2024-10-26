package dataaccess.SQL;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.List;

public class SQLGame implements GameDAO {
    @Override
    public List<GameData> getGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {

    }

    @Override
    public GameData findGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
