package web;

import RecordClasses.*;
import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.List;

public class ServerFacade {

    private final String serverUrl;
    private String authToken = null;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    //PRE LOGIN

    public RegisterResult register(RegisterRequest req) {
        var path = "/user";
        RegisterResult result = this.makeRequest("POST", path, req, RegisterResult.class);
        authToken = result.authToken();
        return result;
    }

    public LoginResult login(LoginRequest req) {
        var path = "/session"; //same as server right?
        LoginResult result = this.makeRequest("POST", path, req, LoginResult.class);
        authToken = result.authToken();
        return result;
    }

    //POST LOGIN

    public LogoutResult logout(String authToken) {
        var path = "/session";
        LogoutResult result = this.makeRequest("DELETE", path, authToken, LogoutResult.class);
        authToken = null;
        return result;
    }

    public CreateGameResult createGame(String name) {
        var path = "/game";
        return this.makeRequest("POST", path, name, CreateGameResult.class);
    }

    public List<GameData> listGames() {
        var path = "/game";
        record listGames(List<GameData> games) {}
        var response = this.makeRequest("GET", path, null, listGames.class);
        return response.games();
    }

    public JoinGameResult joinGame(int ID, ChessGame.TeamColor color) {
        var path = "/game";
        JoinGameRequest game = new JoinGameRequest(color, ID);
        return this.makeRequest("PUT", path, game, JoinGameResult.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {
        try{
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null){
                http.addRequestProperty("Authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) { //throw an exception, handle in the pre/post login
            throw new RuntimeException(e); //NOT ACTUALLY THIS EXCEPTION
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
        var status = http.getResponseCode();
        if (status != 200) {
            //throw a good fail message
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength()<0) {
            try (InputStream resBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(resBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
