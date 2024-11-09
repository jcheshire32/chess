package web;

import RecordClasses.*;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    //PRE LOGIN

    public RegisterResult register(RegisterRequest req) {
        var path = "/user";
        return this.makeRequest("POST", path, req, RegisterResult.class);
    }

    public LoginResult login(LoginRequest req) {
        var path = "/session"; //same as server right?
        return this.makeRequest("POST", path, req, LoginResult.class);
    }

    //POST LOGIN

    public LogoutResult logout(UserData user) {
        var path = "/session";
        return this.makeRequest("DELETE", path, user, LogoutResult.class);
    }

    public CreateGameResult createGame(GameData game) {
        var path = "/game";
        return this.makeRequest("POST", path, game, CreateGameResult.class);
    }

    public GameData[] listGames() { //could not make it like petshop and instead like gameservice
        var path = "/game";
        record listGames(GameData[] games) {}
        var response = this.makeRequest("GET", path, null, listGames.class);
        return response.games();
    }

    public JoinGameResult playGame(UserData user, GameData game) { //auth data needed for joining game?
        var path = "/game";
        return this.makeRequest("PUT", path, game, JoinGameResult.class);
    }

    //observe game

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {
        try{
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
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
