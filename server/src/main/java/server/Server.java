package server;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import exception.ResponseException;
import spark.*;

import java.util.Map;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    }

    private Object clear(Request req, Response res) throws ResponseException {
        service.deleteAllPets();
        res.status(204);
        return "";
    }

    private Object register(Request req, Response res) throws ResponseException {}

    private Object login(Request req, Response res) throws ResponseException {}

    private Object logout(Request req, Response res) throws ResponseException {}

    private Object listGames(Request req, Response res) throws ResponseException {}

    private Object createGame(Request req, Response res) throws ResponseException {}

    private Object joinGame(Request req, Response res) throws ResponseException {}

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
