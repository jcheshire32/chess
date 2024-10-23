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
    //delete the ones I don't use, make the other top level
    public record ListGamesRequest(AuthData authToken){}
    public record ListGamesResult(List<GameData> games){}
    public record CreateGameRequest(String gameName){}
    public record CreateGameResult(int gameID){}
    public record JoinGameRequest(ChessGame.TeamColor playerColor, int gameID){}
    public record JoinGameResult(){}

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
            if (MemoryAuth.getInstance().getAuth(authToken) == null){
                throw new UnauthorizedException("Authorization failed");
            }
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Authorization failed");
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
    public JoinGameResult joinGame(String authToken, JoinGameRequest game) throws UnauthorizedException {
        AuthData authData;
        try {
            authData = MemoryAuth.getInstance().getAuth(authToken);
            if (authData == null){
                throw new UnauthorizedException("Authorization failed");
            }
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Authorization failed");
        }
        //FIND GAME
        MemoryGame gameStorage = MemoryGame.getInstance();
        GameData game2join;
        try {
            game2join = gameStorage.findGame(game.gameID()); //from parameter
            if (game2join == null) {
                throw new UnauthorizedException("Couldn't find game");
            }
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Couldn't join game");
        }
        //UPDATE GAME
        //make sure the team they're trying to join is null or already their name...not sure how to implement this...
        if (game.playerColor == ChessGame.TeamColor.WHITE) {
            if (game2join.whiteUsername() == null) {
                game2join = new GameData(game2join.gameID(), authData.username(), null, game2join.gameName(), game2join.game());
            } else {
                throw new UnauthorizedException("Looks like you have already joined this game bruv");
            }
        } else if (game.playerColor == ChessGame.TeamColor.BLACK){
            if (game2join.blackUsername() == null) {
                game2join = new GameData(game2join.gameID(), null, authData.username(), game2join.gameName(), game2join.game());
            } else {
                throw new UnauthorizedException("Looks like you have already joined this game bruv");
            }
        }
        else {
            throw new UnauthorizedException("Bad game request");
        }
        return new JoinGameResult(); //record at top look okay?
        //Do I add game2join as a parameter for the record of JoinGameResult?
    }
}
