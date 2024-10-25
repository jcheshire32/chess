package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGame implements GameDAO {

    private Map<Integer, GameData> games = new HashMap<Integer, GameData>();

    private static final MemoryGame INSTANCE = new MemoryGame();

    public static MemoryGame getInstance() {
        return INSTANCE;
    }

    @Override
    public List<GameData> getGames() throws DataAccessException {//LIST ALL THE GAMES
        return new ArrayList<>(games.values()); //series of IJ suggestions turned my for loop to this one line
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        games.put(game.gameID(),game);
    }

    @Override
    public GameData findGame(int gameID) throws DataAccessException { //GET A SPECIFIC GAME
        return games.get(gameID);
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
