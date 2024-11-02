package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
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

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(String authToken) throws
            UnauthorizedException, BadRequestException, DataAccessException {
        List<GameData> games;
        try {
            if (authDAO.getAuth(authToken) == null){
                throw new UnauthorizedException("Error: unauthorized");
            }
            GameDAO gameStorage = gameDAO;
            games = gameStorage.getGames();
        } catch (DataAccessException e) {
            throw new BadRequestException("Error: Couldn't get any games"); // putting for 500
        }
        return new ListGamesResult(games);
    }
    public CreateGameResult createGame(String authToken, String gameName) throws
            UnauthorizedException, BadRequestException, ServiceException {
        try {
            if (authDAO.getAuth(authToken) == null) {
                throw new UnauthorizedException("Error: Unauthorized");
            }
            if (gameName == null){
                throw new BadRequestException("Error: bad request");
            }
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
        GameDAO gameStorage = gameDAO;
        int gameID;
        //IF GAME ID IS TAKEN
        do {
            gameID = ThreadLocalRandom.current().nextInt(1, 1000);
        } while(isDuplicateGameID(gameID, gameStorage));
        GameData gameVal = new GameData(gameID, null, null, gameName, new ChessGame());
        try {
            gameDAO.createGame(gameVal);
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
        return new CreateGameResult(gameID);
    }
    boolean isDuplicateGameID(int gameID, GameDAO gameStorage) throws ServiceException {
        List<GameData> games;
        try {
            games = gameStorage.getGames();
        } catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage());
        }
        for (GameData game : games) {
            if (game.gameID() == gameID) { //if there IS a dup
                return true;
            }
        }
        return false;
    }
    public JoinGameResult joinGame(String authToken, JoinGameRequest game) throws
            UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        String username;
        username = authDAO.getAuth(authToken);
        if (username == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        //FIND GAME
        GameDAO gameStorage = gameDAO;
        if (gameStorage == null){
            throw new BadRequestException("Error: bad request");
        }
        GameData game2join = gameStorage.findGame(game.gameID()); //from parameter
        if (game2join == null) {
            throw new BadRequestException("Error: bad request");
        }
        //UPDATE GAME
        //make sure the team they're trying to join is null or already their name
        if (game.playerColor == ChessGame.TeamColor.WHITE) {
            if (game2join.whiteUsername() == null) {
                gameStorage.updateGame(new GameData(game2join.gameID(), username, game2join.blackUsername(),
                        game2join.gameName(), game2join.game()));
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        } else if (game.playerColor == ChessGame.TeamColor.BLACK) {
            if (game2join.blackUsername() == null) {
                gameStorage.updateGame(new GameData(game2join.gameID(), game2join.whiteUsername(), username,
                        game2join.gameName(), game2join.game()));
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        } else { //Neither white nor black
            throw new BadRequestException("Error: bad request");
        }
        return new JoinGameResult();
    }
}
