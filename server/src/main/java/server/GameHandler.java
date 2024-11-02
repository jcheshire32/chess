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
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(401);
            res.body(serializer.toJson(temp));
            return res.body();
        } catch (BadRequestException e) { //putting this 500 as bad req exception
            Map<String, Object> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(400);
            res.body(serializer.toJson(temp));
            return res.body();
        } catch (DataAccessException e) { //putting this 500 as bad req exception
            Map<String, Object> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(500);
            res.body(serializer.toJson(temp));
            return res.body();
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
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(400);
            res.body(serializer.toJson(temp));
            return res.body();
        } catch (UnauthorizedException e){
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(401);
            res.body(serializer.toJson(temp));
            return res.body();
        } catch (ServiceException e){
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(500);
            res.body(serializer.toJson(temp));
            return res.body();
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
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(400);
            res.body(serializer.toJson(temp));
            return res.body();
        } catch (UnauthorizedException e) {
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(401);
            res.body(serializer.toJson(temp));
            return res.body();
        } catch (AlreadyTakenException e){
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(403);
            res.body(serializer.toJson(temp));
            return res.body();
        } catch (DataAccessException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(500);
            res.body(serializer.toJson(temp));
            return res.body();
        }
    }
    //500 = data base problem??
}
