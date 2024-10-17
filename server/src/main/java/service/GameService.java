package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuth;
import dataaccess.memory.MemoryGame;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GameService {
    public record ListGamesRequest(AuthData authToken){}
    public record ListGamesResult(List<GameData> games){}
    public record CreateGameRequest(AuthData authToken){}
    public record CreateGameResult(int gameID){}

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
    public CreateGameResult createGame(String authToken, String gameName) throws UnauthorizedException {
        AuthData authData;
        try {
            authData = MemoryAuth.getInstance().getAuth(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Get outta here");
        }
        //also if it's null, combine with above? or just delete?
        if (authData == null) {
            throw new UnauthorizedException("Invalid auth token");
        }
        MemoryGame gameStorage = MemoryGame.getInstance();
        int gameID;
        //IF GAME ID IS TAKEN
        do {
            gameID = ThreadLocalRandom.current().nextInt(1, 1000);
        } while(isDuplicateGameID(gameID, gameStorage));
        GameData game_val = new GameData(gameID, null, null, gameName, new ChessGame());
        try {
            MemoryGame.getInstance().createGame(game_val);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Couldn't create game");
        }
        return new CreateGameResult(gameID);
    }
    boolean isDuplicateGameID(int gameID, MemoryGame gameStorage) throws UnauthorizedException {
        List<GameData> games;
        try {
            games = gameStorage.getGames();
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Couldn't get any games");
        }
        for (GameData game : games) {
            if (game.gameID() == gameID) { //if there IS a dup
                return true;
            }
        }
        return false;
    }
    public AuthData joinGame(AuthData authToken, GameData colorandIDmaybeidkwhatgoeshere) {return null;}
}
