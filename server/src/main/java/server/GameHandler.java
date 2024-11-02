package server;


import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import service.*;

import java.util.Arrays;
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
            return getString401(res,e,serializer);
        } catch (BadRequestException e) {
            return getString400(res,e,serializer);
        } catch (DataAccessException e) {
            return getString500(res,e,serializer);
        }
    }
    public Object createGame(Request req, Response res){ //takes a JSON object and a nonJSON object
        var serializer = new Gson();
        var createGameRequest = serializer.fromJson(req.body(), GameService.CreateGameRequest.class);
        GameService gameService = new GameService(authDAO, gameDAO);
        String authToken = req.headers("Authorization");
        try {
            var createGameResult = gameService.createGame(authToken, createGameRequest.gameName()); //Double check with TA michael this line works
            return serializer.toJson(createGameResult);
        } catch (BadRequestException e) {
            return getString400(res,e,serializer);
        } catch (UnauthorizedException e){
            return getString401(res,e,serializer);
        } catch (ServiceException e){
            return getString500serv(res,e,serializer);
        }
    }
    public Object joinGame(Request req, Response res){
        var serializer = new Gson();
        var joinGameRequest = serializer.fromJson(req.body(), GameService.JoinGameRequest.class);
        GameService gameService = new GameService(authDAO, gameDAO);
        String authToken = req.headers("Authorization");
        try {
            var joinGameResult = gameService.joinGame(authToken, joinGameRequest);
            return serializer.toJson(joinGameResult);
        } catch (BadRequestException e) {
            return getString400(res,e,serializer);
        } catch (UnauthorizedException e) {
            return getString401(res,e,serializer);
        } catch (AlreadyTakenException e){
            return getString403(res,e,serializer);
        } catch (DataAccessException e) {
            return getString500(res, e, serializer);
        }
    }
    private static String getString400(Response res, BadRequestException e, Gson serializer) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("message", e.getMessage());
        res.status(400);
        res.body(serializer.toJson(temp));
        return res.body();
    }

    private static String getString401(Response res, UnauthorizedException e, Gson serializer) {
        Map<String, String> temp = new HashMap<>();
        temp.put("message", e.getMessage());
        res.status(401);
        res.body(serializer.toJson(temp));
        return res.body();
    }

    private static String getString403(Response res, AlreadyTakenException e, Gson serializer) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("message", e.getMessage());
        res.status(403);
        res.body(serializer.toJson(temp));
        return res.body();
    }

    private static String getString500(Response res, DataAccessException e, Gson serializer) {
        Map<String, String> temp = new HashMap<>();
        temp.put("message", e.getMessage());
        res.status(500);
        res.body(serializer.toJson(temp));
        return res.body();
    }

    private static String getString500serv(Response res, ServiceException e, Gson serializer) {
        Map<String, String> temp = new HashMap<>();
        temp.put("message", e.getMessage());
        res.status(500);
        res.body(serializer.toJson(temp));
        return res.body();
    }
}
