package server;
import com.google.gson.Gson;
import dataAccess.AuthMemoryAccess;
import dataAccess.GameMemoryAccess;
import dataAccess.UserMemoryAccess;
import spark.*;

import java.util.Map;

public class Server {
        static UserMemoryAccess userDAO = new UserMemoryAccess();
        static GameMemoryAccess gameDAO = new GameMemoryAccess();
        static AuthMemoryAccess authDAO = new AuthMemoryAccess();
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (request, response) -> new Clear().clear(request, response));
        Spark.post("/user", (request, response) -> new Register().register(request, response));
        Spark.post("/session", (request, response) -> new Login().login(request, response));
        Spark.delete("/session", (request, response) -> new Logout().logout(request, response));
        Spark.get("/game", (request, response) -> new ListGames().listGames(request, response));
        Spark.put("/game", (request, response) -> new CreateGame().createGame(request, response));
        Spark.post("/game", (request, response) -> new JoinGame().joinGame(request, response));
        //Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
