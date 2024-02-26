package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.GameData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class CreateGame {
    public Object createGame(Request req, Response res) {
        String gameName = req.queryParams("gameName");
        String authToken = req.headers("Authorization");
        try {
            new GameService().createGame(authToken, gameName);
        } catch (DataAccessException e) {
            if (e.getMessage().equals(Constants.BAD_REQUEST)) {
                var body = new Gson().toJson(e.getMessage());
                res.type("application/json");
                res.status(400);
                res.body(body);
                return body;
            }
            if (e.getMessage().equals(Constants.UNAUTHORIZED)) {
                var body = new Gson().toJson(e.getMessage());
                res.type("application/json");
                res.status(401);
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
        }
        var body = new Gson().toJson(authToken);
        res.type("application/json");
        res.status(200);
        res.body(body);
        return body;
    }
}
