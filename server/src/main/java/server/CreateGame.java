package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class CreateGame {
    public Object createGame(Request req, Response res) throws DataAccessException {
        String information = req.body();
        String gameName = new Gson().fromJson(information, String.class);
        new GameService().createGame(gameName);
    }
}
