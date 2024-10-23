package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import service.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.Response;
import spark.Request;

public class GameHandler {

    public Object listGames(Request req, Response res){
        var serializer = new Gson();
        GameService gameService = new GameService();
        String authToken = req.headers("Authorization");
        try {
            var ListGamesResult = gameService.listGames(authToken);
            return serializer.toJson(ListGamesResult);
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
        var CreateGameRequest = serializer.fromJson(req.body(), GameService.CreateGameRequest.class);
        GameService gameService = new GameService();
        String authToken = req.headers("Authorization");
        try {
            var CreateGameResult = gameService.createGame(authToken, CreateGameRequest.gameName()); //Double check with TA michael this line works
            return serializer.toJson(CreateGameResult);
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
        var JoinGameRequest = serializer.fromJson(req.body(), GameService.JoinGameRequest.class);
        GameService gameService = new GameService();
        String authToken = req.headers("Authorization");
        try {
            var JoinGameResult = gameService.joinGame(authToken, JoinGameRequest);
            return serializer.toJson(JoinGameResult);
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
        } catch (OtherException e) {
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(500);
            res.body(serializer.toJson(temp));
            return res.body();
        }
    }
    //500 = data base problem??
}
