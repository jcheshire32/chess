package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import dataaccess.memory.MemoryUser;
import dataaccess.memory.MemoryAuth;
import org.mindrot.jbcrypt.BCrypt;

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

    private AuthDAO authDAO;
    private UserDAO userDAO;


    public UserService(AuthDAO authDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public RegisterResult register(RegisterRequest user) throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        if (user.username == null || user.password == null || user.email == null) {
            throw new BadRequestException("Error: bad request");
        }
        UserData userData;
        //create user
        try{
            //make hashed passord then pass in
            String hashPassword = BCrypt.hashpw(user.password, BCrypt.gensalt());
            userData = new UserData(user.username(), hashPassword, user.email());
            userDAO.createUser(userData);
        } catch (DataAccessException e) {
            throw new AlreadyTakenException("Error: already taken");
        }
        //creat auth
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken,user.username());
        try {
            authDAO.createAuth(authData);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Error: Authorization failed");
        }
        return new RegisterResult(user.username(), authToken);
    }
    public LoginResult login(LoginRequest user) throws UnauthorizedException, BadRequestException {
        UserData userData;
        try{
            userData = userDAO.getUser(user.username());
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Error: User not found"); //401
        }
        //bcrypt instead of equals
        if (!BCrypt.checkpw(user.password, userData.password())){
            throw new UnauthorizedException("Error: Wrong password"); //still 401
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken,user.username);
        try {
            authDAO.createAuth(authData);
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return new LoginResult(user.username, authToken);
    }
    public LogoutResult logout(String authToken) throws UnauthorizedException, BadRequestException {
        String authData;
        try{
            authData = authDAO.getAuth(authToken);
            if (authData == null) {
                throw new UnauthorizedException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        try {
            authDAO.deleteAuth(authToken);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error: Logout failed"); //putting for 500
        }
        return new LogoutResult();
    }
}
