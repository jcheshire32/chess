package service;

import model.AuthData;
import model.GameData;
import model.UserData;

public class UserService {
    public AuthData register(UserData user) {}
    public AuthData login(UserData user) {}
    public void logout(AuthData authToken) {}
    public AuthData listGames(AuthData authToken) {}
    public GameData createGame(AuthData authToken, GameData gameName) {}
    public AuthData joinGame(AuthData authToken, GameData ?colorandID?) {}
    ?clear?
}
