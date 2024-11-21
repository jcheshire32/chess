package service;


import recordclasses.LoginRequest;
import recordclasses.RegisterRequest;
import recordclasses.RegisterResult;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.memory.MemoryAuth;
import dataaccess.memory.MemoryGame;
import dataaccess.memory.MemoryUser;
import org.junit.jupiter.api.*;

public class UserServiceTests {

    private UserService userService;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;
    private ClearService clearService;

    @BeforeEach
    public void setup() throws OtherException {
        authDAO = new MemoryAuth();
        userDAO = new MemoryUser();
        gameDAO = new MemoryGame();
        userService = new UserService(authDAO,userDAO);
        clearService = new ClearService(authDAO,userDAO,gameDAO);
        clearService.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Register Success")
    public void registerSuccess() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(
                new RegisterRequest("Jim", "Jim", "Jim"));
        Assertions.assertEquals("Jim", registeredPerson.username());
    }

    @Test
    @Order(2)
    @DisplayName("Register Fail")
    public void registerFail() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        Assertions.assertThrows(BadRequestException.class, ()->new UserService(authDAO,userDAO).register(
                new RegisterRequest(null, "Jim", "Jim")));
    }

    @Test
    @DisplayName("Login Success")
    public void loginSuccess() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(
                new RegisterRequest("Jim", "Jim", "Jim"));
        Assertions.assertEquals("Jim", registeredPerson.username());

       String authToken = registeredPerson.authToken();
       Assertions.assertNotNull(authToken);
    }

    @Test
    @DisplayName("Login Fail")
    public void loginFail() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(
                new RegisterRequest("Jim", "Jim", "Jim"));
        Assertions.assertEquals("Jim", registeredPerson.username());

        LoginRequest request = new LoginRequest("phil", "phil");
        Assertions.assertThrows(UnauthorizedException.class, ()-> userService.login(request));
    }

    @Test
    @DisplayName("Logout Success")
    public void logoutSuccess() throws
            UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        //make sure auth token is deleted
        RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(
                new RegisterRequest("Jim", "Jim", "Jim"));
        Assertions.assertEquals("Jim", registeredPerson.username());

        String authToken = registeredPerson.authToken();
        Assertions.assertNotNull(authToken);

        userService.logout(authToken);
        Assertions.assertNull(authDAO.getAuth(authToken));
    }

    @Test
    @DisplayName("Logout Fail")
    public void logoutFail() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        RegisterResult registeredPerson = new UserService(authDAO,userDAO).register(
                new RegisterRequest("Jim", "Jim", "Jim"));
        Assertions.assertEquals("Jim", registeredPerson.username());

        String authToken = registeredPerson.authToken();
        Assertions.assertNotNull(authToken);

        Assertions.assertThrows(UnauthorizedException.class, ()-> userService.logout("123123"));
    }
}
