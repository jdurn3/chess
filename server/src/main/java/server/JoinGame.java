package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.Error;
import model.GameData;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGame {
    public Object joinGame(Request req, Response res) {
        String authToken = req.headers("authorization");
        String playerColor = req.queryParams("playerColor");
        int gameID = Integer.parseInt(req.queryParams("gameID"));
        try {
            return new GameService().joinGame(authToken, playerColor, gameID);
        } catch (DataAccessException e) {
            if (e.getMessage().equals(Constants.BAD_REQUEST)) {
                Error error = new Error(e.getMessage());
                var body = new Gson().toJson(error);
                res.type("application/json");
                res.status(400);
                res.body(body);
                return body;
            }
            if (e.getMessage().equals(Constants.ALREADY_TAKEN)) {
                Error error = new Error(e.getMessage());
                var body = new Gson().toJson(error);
                res.type("application/json");
                res.status(403);
                res.body(body);
                return body;
            }
            if (e.getMessage().equals(Constants.DESCRIPTION)) {
                Error error = new Error(e.getMessage());
                var body = new Gson().toJson(error);
                res.type("application/json");
                res.status(500);
                res.body(body);
                return body;
            }
            if (e.getMessage().equals(Constants.UNAUTHORIZED)) {
                Error error = new Error(e.getMessage());
                var body = new Gson().toJson(error);
                res.type("application/json");
                res.status(500);
                res.body(body);
                return body;
            }
        }
        res.status(200);
        return "{}";
    }
}
