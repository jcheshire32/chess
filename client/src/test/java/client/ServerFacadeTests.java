package client;

import RecordClasses.*;
import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import web.ServerFacade;

import java.util.List;


public class ServerFacadeTests {

    private static Server server;

    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        facade.clear();
    }

    @BeforeEach
    public void clear(){
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("Register Success")
    public void registerSuccess() {
        RegisterResult person = facade.register(new RegisterRequest("jim","jim","jim"));
        Assertions.assertTrue(person.username().equals("jim"));
    }

    @Test
    @DisplayName("Register Fail")
    public void registerFail() {
        RegisterResult person = facade.register(new RegisterRequest("jim","jim","jim"));
        try {
            RegisterResult person2 = facade.register(new RegisterRequest("jim", "jim", "jim"));
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("[ERROR]"));
        }
    }

    @Test
    @DisplayName("Login Success")
    public void loginSuccess() {
        //register
        RegisterRequest person = new RegisterRequest("jim", "jim", "jim");
        RegisterResult jim = facade.register(person);

        //actual login
        LoginRequest req = new LoginRequest("jim", "jim");
        LoginResult res = facade.login(req);
        Assertions.assertNotNull(res);
        Assertions.assertNotNull(res.authToken());
    }

    @Test
    @DisplayName("Login Fail")
    public void loginFail() {
        LoginRequest person = new LoginRequest("fakejim", "fakejim");
        try {
            LoginResult jim = facade.login(person);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("[ERROR]"));
        }
    }

    @Test
    @DisplayName("Logout Success")
    public void logoutSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("jim", "jim", "");
        RegisterResult registerResult = facade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("jim", "jim");
        LoginResult loginResult = facade.login(loginRequest);


        LogoutResult logoutResult = facade.logout(loginResult.authToken());
        Assertions.assertNotNull(logoutResult);
    }

    @Test
    @DisplayName("Logout Fail")
    public void logoutFail() {
        RegisterRequest registerRequest = new RegisterRequest("jim", "jim", "");
        RegisterResult registerResult = facade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("jim", "jim");
        LoginResult loginResult = facade.login(loginRequest);

        try {
            LogoutResult logoutResult = facade.logout(loginResult.username());
            Assertions.assertNotNull(logoutResult);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("[ERROR]"));
        }
    }

    @Test
    @DisplayName("Create Game Success")
    public void createSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("jim", "jim", "");
        RegisterResult registerResult = facade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("jim", "jim");
        LoginResult loginResult = facade.login(loginRequest);

        CreateGameResult createGameResult = facade.createGame(loginResult.authToken());
        Assertions.assertNotNull(createGameResult);
    }

    @Test
    @DisplayName("Create Game Fail")
    public void createFail() {
        RegisterRequest registerRequest = new RegisterRequest("jim", "jim", "");
        RegisterResult registerResult = facade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("jim", "jim");
        LoginResult loginResult = facade.login(loginRequest);

        try {
            CreateGameResult createGameResult = facade.createGame(loginResult.username());
            Assertions.assertNotNull(createGameResult);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("[ERROR]"));
        }
    }

    @Test
    @DisplayName("List Games Success")
    public void listSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("jim", "jim", "");
        RegisterResult registerResult = facade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("jim", "jim");
        LoginResult loginResult = facade.login(loginRequest);

        CreateGameResult createGameResult = facade.createGame(loginResult.authToken());

        List<GameData> list = facade.listGames();
        Assertions.assertNotNull(list);
    }

    @Test
    @DisplayName("List Games Fail")
    public void listFail() {
        RegisterRequest registerRequest = new RegisterRequest("jim", "jim", "");
        RegisterResult registerResult = facade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("jim", "jim");
        LoginResult loginResult = facade.login(loginRequest);

        try {
            List<GameData> list = facade.listGames();
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("[ERROR]"));
        }
    }

    @Test
    @DisplayName("Join Success")
    public void joinSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("jim", "jim", "");
        RegisterResult registerResult = facade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("jim", "jim");
        LoginResult loginResult = facade.login(loginRequest);

        CreateGameResult createGameResult = facade.createGame(loginResult.authToken());
        int gameID = createGameResult.gameID();

        JoinGameResult joinedGame = facade.joinGame(gameID, ChessGame.TeamColor.WHITE);
        Assertions.assertNotNull(joinedGame);
    }

    @Test
    @DisplayName("Join Fail")
    public void joinFail() {
        RegisterRequest registerRequest = new RegisterRequest("jim", "jim", "");
        RegisterResult registerResult = facade.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("jim", "jim");
        LoginResult loginResult = facade.login(loginRequest);

        CreateGameResult createGameResult = facade.createGame(loginResult.authToken());
        int gameID = createGameResult.gameID();

        try {
            JoinGameResult joinedGame = facade.joinGame(gameID, null);
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("[ERROR]"));
        }
    }

}
