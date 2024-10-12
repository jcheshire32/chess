package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataaccess.memory.MemoryUser;
import dataaccess.memory.MemoryAuth;

import java.util.UUID;


public class UserService {
    public 	record LoginRequest(String username,String password){
    }
    public 	record LoginResult(String username,String authToken){
    }
    public AuthData register(UserData user) {}
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
    public void logout(AuthData authToken) {}
}
