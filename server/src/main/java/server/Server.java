package server;

import dataaccess.*;
import dataaccess.SQL.SQLAuth;
import dataaccess.SQL.SQLGame;
import dataaccess.SQL.SQLUser;
import dataaccess.memory.MemoryAuth;
import dataaccess.memory.MemoryGame;
import dataaccess.memory.MemoryUser;
import spark.*;

import java.sql.SQLException;

public class Server {

    private AuthDAO authDAO = new MemoryAuth();
    private GameDAO gameDAO = new MemoryGame();
    private UserDAO userDAO = new MemoryUser();

    public void createDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
    }


    public int run(int desiredPort) { //startup
        try {
            createDatabase();
            //call table creation
            authDAO = new SQLAuth();
            userDAO = new SQLUser();
            gameDAO = new SQLGame();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage()); //if database creation fails:
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }


        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (request, response) -> new UserHandler(authDAO,userDAO).register(request, response));
        Spark.post("/session", (request, response) -> new UserHandler(authDAO,userDAO).login(request, response));
        Spark.delete("/session", (request, response) -> new UserHandler(authDAO,userDAO).logout(request, response));
        Spark.get("/game", ((request, response) -> new GameHandler(authDAO,gameDAO).listGames(request, response)));
        Spark.post("/game", ((request, response) -> new GameHandler(authDAO,gameDAO).createGame(request, response)));
        Spark.put("/game", ((request, response) -> new GameHandler(authDAO,gameDAO).joinGame(request, response)));
        Spark.delete("/db", ((request, response) -> new ClearHandler(authDAO,userDAO,gameDAO).clear(request, response)));

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
