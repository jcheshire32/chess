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

    public String listGames(String authToken) throws UnauthorizedException { //WHAT TO RETURN? STRING? LIST? doing string rn for JSON stuff
        AuthData authData;
        try {
            MemoryAuth.getInstance().getAuth(authToken);
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
        return new ListGamesResult(games).toString();
    }
    public GameData createGame(AuthData authToken, GameData gameName) {}
    public AuthData joinGame(AuthData authToken, GameData ?colorandID?) {}
}
