package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
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
                var body = new Gson().toJson(e.getMessage());
                res.type("application/json");
                res.status(400);
                res.body(body);
                return body;
            }
            if (e.getMessage().equals(Constants.ALREADY_TAKEN)) {
                var body = new Gson().toJson(e.getMessage());
                res.type("application/json");
                res.status(403);
                res.body(body);
                return body;
            }
            if (e.getMessage().equals(Constants.DESCRIPTION)) {
                var body = new Gson().toJson(e.getMessage());
                res.type("application/json");
                res.status(500);
                res.body(body);
                return body;
            }
            if (e.getMessage().equals(Constants.UNAUTHORIZED)) {
                var body = new Gson().toJson(e.getMessage());
                res.type("application/json");
                res.status(500);
                res.body(body);
                return body;
            }
        }
        new GameService().clear();
        res.status(200);
        return res;
    }
}
