package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.Error;
import model.GameData;
import model.Join;
import model.UserData;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGame {
    public Object joinGame(Request req, Response res) {
        String authToken = req.headers("authorization");
        Join newPlayer = new Gson().fromJson(req.body(), Join.class);
        try {
            new GameService().joinGame(authToken, newPlayer.playerColor(), newPlayer.gameID(), Server.authDAO, Server.gameDAO);
        } catch (DataAccessException e) {
            return Exceptions.giveException(e, res);
        }
        res.status(200);
        return "{}";

    }
}
