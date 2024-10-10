package dataaccess;

import model.GameData;

public interface GameDAO {
    void getGames() throws DataAccessException;
    void createGame() throws DataAccessException;
    GameData findGame(int gameID) throws DataAccessException;
    void joinGame(GameData gameData) throws DataAccessException;
    void clear() throws DataAccessException;
}
