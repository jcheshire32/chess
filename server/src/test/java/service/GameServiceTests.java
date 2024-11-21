package service;

import recordclasses.*;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.memory.MemoryAuth;
import dataaccess.memory.MemoryGame;
import dataaccess.memory.MemoryUser;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.List;

public class GameServiceTests {

    private GameService gameService;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    @BeforeEach
    public void setup() {
        authDAO = new MemoryAuth();
        userDAO = new MemoryUser();
        gameDAO = new MemoryGame();
        gameService = new GameService(authDAO,gameDAO);
    }

    @Test
    @Order(1)
    @DisplayName("List Games Success")
    public void listGamesPositive() throws
            ServiceException, UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(
                new RegisterRequest("Jim", "Jim", "Jim"));

        gameService.createGame(registeredPerson.authToken(), "game1");

        String authtoken = authDAO.getAuth(registeredPerson.authToken());
        Assertions.assertNotNull(authtoken);

        ListGamesResult listGamesResult = gameService.listGames(registeredPerson.authToken());
        Assertions.assertNotNull(listGamesResult);

        List<GameData> games = listGamesResult.games();
        Assertions.assertEquals(1, games.size());
        Assertions.assertEquals("game1", games.get(0).gameName());
    }

    @Test
    @Order(2)
    @DisplayName("List Games Fail")
    public void listGamesFail() throws
            UnauthorizedException, BadRequestException, AlreadyTakenException, ServiceException, DataAccessException {
        RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(
                new RegisterRequest("Jim", "Jim", "Jim"));

        gameService.createGame(registeredPerson.authToken(), "game1");

        String authtoken = authDAO.getAuth(registeredPerson.authToken());
        Assertions.assertNotNull(authtoken);

        Assertions.assertThrows(UnauthorizedException.class, ()-> gameService.listGames(authtoken));
    }

    @Test
    @Order(3)
    @DisplayName("Create Game Success")
    public void createGamePositive() throws
            ServiceException, UnauthorizedException, BadRequestException, AlreadyTakenException {
        RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(
                new RegisterRequest("Jim", "Jim", "Jim"));
        Assertions.assertNotNull(registeredPerson.authToken());

        CreateGameResult gameID = gameService.createGame(registeredPerson.authToken(), "game1");
        Assertions.assertNotNull(gameID);
    }

    @Test
    @Order(4)
    @DisplayName("Create Game Fail")
    public void createGameFail() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(
                new RegisterRequest("Jim", "Jim", "Jim"));
        Assertions.assertNotNull(registeredPerson.authToken());

        Assertions.assertThrows(BadRequestException.class,
                ()-> gameService.createGame(registeredPerson.authToken(), null));
    }

    @Test
    @Order(5)
    @DisplayName("Join Game Success")
    public void joinGamePositive() throws
            UnauthorizedException, BadRequestException, AlreadyTakenException,
            ServiceException, DataAccessException, OtherException {
        RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(
                new RegisterRequest("Jim", "Jim", "Jim"));
        Assertions.assertNotNull(registeredPerson.authToken());

        CreateGameResult gameID = gameService.createGame(registeredPerson.authToken(), "game1");
        Assertions.assertNotNull(gameID);

        gameService.joinGame(registeredPerson.authToken(),
                new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID.gameID()));
        GameData game = gameDAO.findGame(gameID.gameID());
        Assertions.assertNotNull(game.whiteUsername());
        Assertions.assertEquals("Jim", game.whiteUsername());
    }

    @Test
    @Order(6)
    @DisplayName("Join Game Fail")
    public void joinGameFail() throws
            UnauthorizedException, BadRequestException, AlreadyTakenException, ServiceException {
        RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(
                new RegisterRequest("Jim", "Jim", "Jim"));
        Assertions.assertNotNull(registeredPerson.authToken());

        CreateGameResult gameID = gameService.createGame(registeredPerson.authToken(), "game1");
        Assertions.assertNotNull(gameID);

        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, 123);
        Assertions.assertThrows(BadRequestException.class,
                ()-> gameService.joinGame(registeredPerson.authToken(), request));
    }
}
