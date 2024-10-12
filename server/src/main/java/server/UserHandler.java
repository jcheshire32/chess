package server;

import com.google.gson.Gson;
import service.UnauthorizedException;
import service.UserService;

public class UserHandler {
    //register,login,logout
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
