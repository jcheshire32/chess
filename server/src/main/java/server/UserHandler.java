package server;

import com.google.gson.Gson;
import service.UnauthorizedException;
import service.UserService;

public class UserHandler {
    //register,login,logout
    //return type? Json??
    public register(String incomingObject){
        var serializer = new Gson();
        var registerRequest = serializer.fromJson(incomingObject, UserService.RegisterRequest.class);
        UserService registerService = new UserService();
        try {
            var registerResult = registerService.register(registerRequest);
            return serializer.toJson(registerResult);
        } catch (UnauthorizedException e) {
            //WHICH FAILURE RESPONSE?
        }
    }

    public login(String incomingObject){
        var serializer = new Gson();
        var loginRequest = serializer.fromJson(incomingObject, UserService.LoginRequest.class);
        //stuff
        UserService loginService = new UserService();
        try {
            var loginResult = loginService.login(loginRequest);
            return serializer.toJson(loginResult);
        } catch (UnauthorizedException e) {
            //WHAT FAILURE RESPONSE [401] { "message": "Error: unauthorized" }
        }
    }
}
