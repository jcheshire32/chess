package server;

import RecordClasses.LoginRequest;
import RecordClasses.RegisterRequest;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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
        var registerRequest = serializer.fromJson(req.body(), RegisterRequest.class);
        UserService registerService = new UserService(authDAO,userDAO);
        try {
            var registerResult = registerService.register(registerRequest);
            return serializer.toJson(registerResult);
        } catch (BadRequestException e) {
            return getString400(res, e, serializer);
        } catch (AlreadyTakenException e){
            return getString403(res, e, serializer);
        } catch (UnauthorizedException e) {
            return getString500unauth(res, e, serializer);
        }
    }

    static String getString500unauth(Response res, UnauthorizedException e, Gson serializer) {
        Map<String, String> temp = new HashMap<>();
        temp.put("message", e.getMessage());
        res.status(500);
        res.body(serializer.toJson(temp));
        return res.body();
    }

    static String getString400(Response res, BadRequestException e, Gson serializer) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("message", e.getMessage());
        res.status(400);
        res.body(serializer.toJson(temp));
        return res.body();
    }

    static String getString401(Response res, UnauthorizedException e, Gson serializer) {
        Map<String, String> temp = new HashMap<>();
        temp.put("message", e.getMessage());
        res.status(401);
        res.body(serializer.toJson(temp));
        return res.body();
    }

    static String getString403(Response res, AlreadyTakenException e, Gson serializer) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("message", e.getMessage());
        res.status(403);
        res.body(serializer.toJson(temp));
        return res.body();
    }

    static String getString500(Response res, DataAccessException e, Gson serializer) {
        Map<String, String> temp = new HashMap<>();
        temp.put("message", e.getMessage());
        res.status(500);
        res.body(serializer.toJson(temp));
        return res.body();
    }

    public Object login(Request req, Response res){
        var serializer = new Gson();
        var loginRequest = serializer.fromJson(req.body(), LoginRequest.class);
        //stuff
        UserService loginService = new UserService(authDAO,userDAO);
        try {
            var loginResult = loginService.login(loginRequest);
            return serializer.toJson(loginResult);
        } catch (UnauthorizedException e) {
            return getString401(res, e, serializer);
        } catch (BadRequestException e) {
            return getString500BadReq(res, e, serializer);
        }
    }

    public Object logout(Request req, Response res){
        var serializer = new Gson();
        UserService logoutService = new UserService(authDAO,userDAO);
        String authToken = req.headers("Authorization");
        try {
            return serializer.toJson(logoutService.logout(authToken));
        } catch (UnauthorizedException e) {
            return getString401(res, e, serializer);
        } catch (BadRequestException e) {
            return getString500BadReq(res, e, serializer);
        }
    }

    private static String getString500BadReq(Response res, BadRequestException e, Gson serializer) {
        Map<String, Object> temp = new HashMap<>();
        temp.put("message", e.getMessage());
        res.status(500);
        res.body(serializer.toJson(temp));
        return res.body();
    }
}
