package server;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import service.GameService;
import service.UnauthorizedException;

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
        }
    }
}
