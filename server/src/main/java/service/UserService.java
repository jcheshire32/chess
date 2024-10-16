package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataaccess.memory.MemoryUser;
import dataaccess.memory.MemoryAuth;

import java.util.UUID;


public class UserService {
    public 	record RegisterRequest(String username,String password, String email){
    }
    public 	record RegisterResult(String username,String authToken){
    }
    public 	record LoginRequest(String username,String password){
    }
    public 	record LoginResult(String username,String authToken){
    }
    public record LogoutRequest(AuthData authToken){
    }
    public record LogoutResult(){
    }
    public RegisterResult register(RegisterRequest user) throws UnauthorizedException{
        UserData userData;
        //create user
        try{
            userData = new UserData(user.username(), user.password(), user.email());
            MemoryUser.getInstance().createUser(userData);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Creation failed");
        }
        //creat auth
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken,user.username());
        try {
            MemoryAuth.getInstance().createAuth(authData);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Authorization failed");
        }
        return new RegisterResult(user.username(), authToken);
    }
    public LoginResult login(LoginRequest user) throws UnauthorizedException{
        UserData userData;
        try{
            userData = MemoryUser.getInstance().getUser(user.username());
        } catch (DataAccessException e) {
            throw new UnauthorizedException("User not found");
        }
        if (!userData.password().equals(user.password())) {
            throw new UnauthorizedException("Wrong password");
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken,user.username);
        try {
            MemoryAuth.getInstance().createAuth(authData);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Authorization failed");
        }
        return new LoginResult(user.username, authToken);
    }
    public LogoutResult logout(String authToken) throws UnauthorizedException{
        AuthData authData;
        try{
            authData = MemoryAuth.getInstance().getAuth(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Authorization failed");
        }
        try {
            MemoryAuth.getInstance().deleteAuth(authData);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Logout failed");
        }
        return new LogoutResult();
    }
}
