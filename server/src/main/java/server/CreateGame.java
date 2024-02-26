package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.*;
import model.Error;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class CreateGame {
    public Object createGame(Request req, Response res) {
        gameName newGameName = new Gson().fromJson(req.body(), gameName.class);
        String authToken = req.headers("Authorization");
        try {
            new GameService().createGame(authToken, newGameName.gameName(), Server.authDAO, Server.gameDAO);
        } catch (DataAccessException e) {
            return Exceptions.giveException(e, res);
        }
        var body = new Gson().toJson(newGameName);
        res.type("application/json");
        res.status(200);
        res.body(body);
        return "{}";
    }
}
