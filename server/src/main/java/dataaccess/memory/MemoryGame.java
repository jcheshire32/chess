package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemoryGame implements GameDAO {

    private List<GameData> games = new ArrayList<GameData>();

    private static final MemoryGame instance = new MemoryGame();

    public static MemoryGame getInstance() {
        return instance;
    }

    @Override
    public List<GameData> getGames() throws DataAccessException {//LIST ALL THE GAMES
        return games;
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        games.add(game);
    }

    @Override
    public GameData findGame(int gameID) throws DataAccessException { //GET A SPECIFIC GAME
        return games.get(gameID);
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        games.add(gameData);
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
