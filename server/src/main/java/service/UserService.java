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
    public RegisterResult register(RegisterRequest user) throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        if (user.username == null || user.password == null || user.email == null) {
            throw new BadRequestException("Error: bad request");
        }
        UserData userData;
        //create user
        try{
            userData = new UserData(user.username(), user.password(), user.email());
            MemoryUser.getInstance().createUser(userData);
        } catch (DataAccessException e) {
            throw new AlreadyTakenException("Error: already taken");
        }
        //creat auth
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken,user.username());
        try {
            MemoryAuth.getInstance().createAuth(authData);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Error: Authorization failed");
        }
        return new RegisterResult(user.username(), authToken);
    }
    public LoginResult login(LoginRequest user) throws UnauthorizedException, BadRequestException {
        UserData userData;
        try{
            userData = MemoryUser.getInstance().getUser(user.username());
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Error: User not found"); //401
        }
        if (!userData.password().equals(user.password())) {
            throw new UnauthorizedException("Error: Wrong password"); //401
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken,user.username);
        try {
            MemoryAuth.getInstance().createAuth(authData);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return new LoginResult(user.username, authToken);
    }
    public LogoutResult logout(String authToken) throws UnauthorizedException, BadRequestException {
        AuthData authData;
        try{
            authData = MemoryAuth.getInstance().getAuth(authToken);
            if (authData == null) {
                throw new UnauthorizedException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        try {
            MemoryAuth.getInstance().deleteAuth(authData);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error: Logout failed"); //putting for 500
        }
        return new LogoutResult();
    }
}
