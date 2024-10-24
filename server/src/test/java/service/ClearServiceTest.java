package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.memory.MemoryAuth;
import dataaccess.memory.MemoryGame;
import dataaccess.memory.MemoryUser;

import org.junit.jupiter.api.*;


public class ClearServiceTest {
    private ClearService clearService;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;
    private GameService gameService;

    @BeforeEach
    public void setup() {
        authDAO = new MemoryAuth();
        userDAO = new MemoryUser();
        gameDAO = new MemoryGame();
        clearService = new ClearService(authDAO,userDAO,gameDAO);
        gameService = new GameService(authDAO,gameDAO);
    }

    @Test
    @DisplayName("Clear Success")
    public void testClear() throws DataAccessException, ServiceException, UnauthorizedException, BadRequestException, AlreadyTakenException, OtherException {
        UserService.RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(new UserService.RegisterRequest("Jim", "Jim", "Jim"));

        gameService.createGame(registeredPerson.authToken(), "game1");

        clearService.clear();
        Assertions.assertNull(authDAO.getAuth(registeredPerson.authToken()));
        Assertions.assertThrows(DataAccessException.class, ()-> userDAO.getUser(registeredPerson.username()));
    }
}
