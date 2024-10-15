package server;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import service.GameService;
import service.UnauthorizedException;

import java.util.ArrayList;
import java.util.List;

public class GameHandler {

    public String listGames(String authToken){ //what return type? String of games? LIST of games?
        var serializer = new Gson();
        List<GameData> games = new ArrayList<>();
        GameService gameService = new GameService();
        try {
            var ListGamesResult = gameService.listGames(authToken);
            return serializer.toJson(ListGamesResult); //supposed to have all this: [200] { "games": [{"gameID": 1234, "whiteUsername":"", "blackUsername":"", "gameName:""} ]}
        } catch (UnauthorizedException e) {
            return "[401] { \"message\": \"Error: unauthorized\" }";
        }
    }
}
