package server;

import com.google.gson.Gson;
import model.AuthData;
import service.UnauthorizedException;
import service.UserService;

public class UserHandler {
    //register,login,logout
    //return type? Json??
    public String register(String incomingObject){
        var serializer = new Gson();
        var registerRequest = serializer.fromJson(incomingObject, UserService.RegisterRequest.class);
        UserService registerService = new UserService();
        try {
            var registerResult = registerService.register(registerRequest);
            return serializer.toJson(registerResult);
        } catch (UnauthorizedException e) {
            return "[500] { \"message\": \"Error: (description of error)\" }"; //WHICH FAILURE RESPONSE?
        }
    }

    public String login(String incomingObject){
        var serializer = new Gson();
        var loginRequest = serializer.fromJson(incomingObject, UserService.LoginRequest.class);
        //stuff
        UserService loginService = new UserService();
        try {
            var loginResult = loginService.login(loginRequest);
            return serializer.toJson(loginResult);
        } catch (UnauthorizedException e) {
            return "[401] { \"message\": \"Error: unauthorized\" }";//WHAT FAILURE RESPONSE
        }
    }
    //logout request doesn't receive a json object
    //THIS FUNCTION
    public String logout(String authToken){ //Do I return string or is JSON a type to return?
        UserService logoutService = new UserService();
        try {
            logoutService.logout(authToken);
            return "{200}"; //How do I return the json thing?
        } catch (UnauthorizedException e) {
            return "[401] { \"message\": \"Error: unauthorized\" }"; //WHAT FAILURE RESPONSE
        }
    }
}
