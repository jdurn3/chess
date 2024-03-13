package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.*;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateGame {
    public Object createGame(Request req, Response res) {
        GameName newGameName = new Gson().fromJson(req.body(), GameName.class);
        String authToken = req.headers("Authorization");
        int gameID;
        try {
            gameID = new GameService().createGame(authToken, newGameName.gameName(), Server.authDAO, Server.gameDAO);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return Exceptions.giveException(e, res);
        }
        var body = new Gson().toJson(new CreateResponse(gameID));
        res.type("application/json");
        res.status(200);
        res.body(body);
        return body;
    }
}
