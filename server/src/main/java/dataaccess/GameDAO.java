package dataaccess;

import model.GameData;

import java.util.List; //hope this is the right list class/interface?

public interface GameDAO {
    List<GameData> getGames() throws DataAccessException;
    void createGame(GameData game) throws DataAccessException;
    GameData findGame(int gameID) throws DataAccessException;
    void updateGame(GameData gameData) throws DataAccessException;
    void clear() throws DataAccessException;
}
