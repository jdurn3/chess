package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.Error;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class ListGames {
    public Object listGames(Request req, Response res) {
        String authToken = req.headers("Authorization");
        Object[] list = new Object[0];
        try {
            list = new GameService().listGames(authToken, Server.authDAO, Server.gameDAO).toArray();
        } catch (DataAccessException e) {
            if (e.getMessage().equals(Constants.UNAUTHORIZED)) {
                Error error = new Error(e.getMessage());
                var body = new Gson().toJson(error);
                res.type("application/json");
                res.status(401);
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
        }
        var body = new Gson().toJson(Map.of("games", list));
        res.type("application/json");
        res.status(200);
        res.body(body);
        return body;
    }
}
