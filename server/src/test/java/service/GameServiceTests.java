package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.memory.MemoryAuth;
import dataaccess.memory.MemoryGame;
import dataaccess.memory.MemoryUser;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.List;

public class GameServiceTests {

    private GameService gameService;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;
    private String existingAuth;


    @BeforeEach
    public void setup() {
        authDAO = new MemoryAuth();
        userDAO = new MemoryUser();
        gameDAO = new MemoryGame();
        gameService = new GameService(authDAO,gameDAO);
        //register a user here or each test
//        existingAuth =
    }

    @Test
    @Order(1)
    @DisplayName("List Games Success")
    public void listGamesPositive() throws ServiceException, UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        UserService.RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(new UserService.RegisterRequest("Jim", "Jim", "Jim"));

        gameService.createGame(registeredPerson.authToken(), "game1");

        String authtoken = authDAO.getAuth(registeredPerson.authToken());
        Assertions.assertNotNull(authtoken);

        GameService.ListGamesResult listGamesResult = gameService.listGames(registeredPerson.authToken());
        Assertions.assertNotNull(listGamesResult);

        List<GameData> games = listGamesResult.games();
        Assertions.assertEquals(1, games.size());
        Assertions.assertEquals("game1", games.get(0).gameName());
    }

    @Test
    @Order(2)
    @DisplayName("List Games Fail")
    public void listGamesFail() {

    }

    @Test
    @Order(3)
    @DisplayName("Create Game Success")
    public void createGamePositive() {

    }

    @Test
    @Order(4)
    @DisplayName("Create Game Fail")
    public void createGameFail() {

    }

    @Test
    @Order(5)
    @DisplayName("Join Game Success")
    public void joinGamePositive() {

    }

    @Test
    @Order(6)
    @DisplayName("Join Game Fail")
    public void joinGameFail() {

    }
}
