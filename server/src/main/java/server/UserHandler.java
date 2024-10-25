package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.UnauthorizedException;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class UserHandler {
    private AuthDAO authDAO;
    private UserDAO userDAO;

    public UserHandler(AuthDAO authDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public Object register(Request req, Response res){
        var serializer = new Gson();
        var registerRequest = serializer.fromJson(req.body(), UserService.RegisterRequest.class);
        UserService registerService = new UserService(authDAO,userDAO);
        try {
            var registerResult = registerService.register(registerRequest);
            return serializer.toJson(registerResult);
        } catch (BadRequestException e) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(400);
            res.body(serializer.toJson(temp));
            return res.body();
        } catch (AlreadyTakenException e){
            Map<String, Object> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(403);
            res.body(serializer.toJson(temp));
            return res.body();
        } catch (UnauthorizedException e) { //how to do the 500 ones. catch "other" exception?
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(500);
            res.body(serializer.toJson(temp));
            return res.body();
        }
    }
    public Object login(Request req, Response res){
        var serializer = new Gson();
        var loginRequest = serializer.fromJson(req.body(), UserService.LoginRequest.class);
        //stuff
        UserService loginService = new UserService(authDAO,userDAO);
        try {
            var loginResult = loginService.login(loginRequest);
            return serializer.toJson(loginResult);
        } catch (UnauthorizedException e) {
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(401);
            res.body(serializer.toJson(temp));
            return res.body();
        } catch (BadRequestException e) { //putting this 500 as bad req exception
            Map<String, Object> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(500);
            res.body(serializer.toJson(temp));
            return res.body();
        }
    }
    public Object logout(Request req, Response res){
        var serializer = new Gson();
        UserService logoutService = new UserService(authDAO,userDAO);
        String authToken = req.headers("Authorization");
        try {
            return serializer.toJson(logoutService.logout(authToken));
        } catch (UnauthorizedException e) {
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(401);
            res.body(serializer.toJson(temp));
            return res.body();
        } catch (BadRequestException e) { //putting this 500 as bad req exception
            Map<String, String> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(500);
            res.body(serializer.toJson(temp));
            return res.body();
        }
    }
}
