package service;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuth;
import dataaccess.memory.MemoryGame;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    public record ListGamesRequest(AuthData authToken){}
    public record ListGamesResult(List<GameData> games){}

    public ListGamesResult listGames(String authToken) throws UnauthorizedException {
        try {
            if (MemoryAuth.getInstance().getAuth(authToken) == null){
                throw new UnauthorizedException("Authorization failed");
            }
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Authorization failed");
        }
        List<GameData> games;
        try {
            MemoryGame gameStorage = MemoryGame.getInstance();
            games = gameStorage.getGames();
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Couldn't get any games"); // right exception??
        }
        return new ListGamesResult(games);
    }
    public GameData createGame(AuthData authToken, GameData gameName) {return null;}
    public AuthData joinGame(AuthData authToken, GameData colorandID) {return null;}
}
