package server;


import RecordClasses.CreateGameRequest;
import RecordClasses.JoinGameRequest;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import service.*;

import java.util.HashMap;
import java.util.Map;

import spark.Response;
import spark.Request;

public class GameHandler {

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameHandler(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Object listGames(Request req, Response res){
        var serializer = new Gson();
        GameService gameService = new GameService(authDAO,gameDAO);
        String authToken = req.headers("Authorization");
        try {
            var listGamesResult = gameService.listGames(authToken);
            return serializer.toJson(listGamesResult);
        } catch (UnauthorizedException e) {
            return UserHandler.getString401(res,e,serializer);
        } catch (BadRequestException e) {
            return UserHandler.getString400(res,e,serializer);
        } catch (DataAccessException e) {
            return UserHandler.getString500(res,e,serializer);
        }
    }
    public Object createGame(Request req, Response res){ //takes a JSON object and a nonJSON object
        var serializer = new Gson();
        var createGameRequest = serializer.fromJson(req.body(), CreateGameRequest.class);
        GameService gameService = new GameService(authDAO, gameDAO);
        String authToken = req.headers("Authorization");
        try {
            var createGameResult = gameService.createGame(authToken, createGameRequest.gameName()); //Double check with TA michael this line works
            return serializer.toJson(createGameResult);
        } catch (BadRequestException e) {
            return UserHandler.getString400(res,e,serializer);
        } catch (UnauthorizedException e){
            return UserHandler.getString401(res,e,serializer);
        } catch (ServiceException e){
            return getString500serv(res,e,serializer);
        }
    }
    public Object joinGame(Request req, Response res){
        var serializer = new Gson();
        var joinGameRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
        GameService gameService = new GameService(authDAO, gameDAO);
        String authToken = req.headers("Authorization");
        try {
            var joinGameResult = gameService.joinGame(authToken, joinGameRequest);
            return serializer.toJson(joinGameResult);
        } catch (BadRequestException e) {
            return UserHandler.getString400(res,e,serializer);
        } catch (UnauthorizedException e) {
            return UserHandler.getString401(res,e,serializer);
        } catch (AlreadyTakenException e){
            return UserHandler.getString403(res,e,serializer);
        } catch (DataAccessException e) {
            return UserHandler.getString500(res, e, serializer);
        }
    }

    private static String getString500serv(Response res, ServiceException e, Gson serializer) {
        Map<String, String> temp = new HashMap<>();
        temp.put("message", e.getMessage());
        res.status(500);
        res.body(serializer.toJson(temp));
        return res.body();
    }
}
