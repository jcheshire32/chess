package server;

import com.google.gson.Gson;
import model.AuthData;
import service.UnauthorizedException;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class UserHandler {
    //register,login,logout
    //return type? Json??
    public Object register(Request req, Response res){
        var serializer = new Gson();
        var registerRequest = serializer.fromJson(req.body(), UserService.RegisterRequest.class);
        UserService registerService = new UserService();
        try {
            var registerResult = registerService.register(registerRequest);
            return serializer.toJson(registerResult);
        } catch (UnauthorizedException e) {
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(401);
            res.body(serializer.toJson(temp));
            return res.body();
        }
    }
    public Object login(Request req, Response res){
        var serializer = new Gson();
        var loginRequest = serializer.fromJson(req.body(), UserService.LoginRequest.class);
        //stuff
        UserService loginService = new UserService();
        try {
            var loginResult = loginService.login(loginRequest);
            return serializer.toJson(loginResult);
        } catch (UnauthorizedException e) {
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(401);
            res.body(serializer.toJson(temp));
            return res.body();
        }
    }
    public Object logout(Request req, Response res){
        var serializer = new Gson();
        UserService logoutService = new UserService();
        String authToken = req.headers("Authorization");
        try {
            return serializer.toJson(logoutService.logout(authToken));
        } catch (UnauthorizedException e) {
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(401);
            res.body(serializer.toJson(temp));
            return res.body();
        }
    }
}
