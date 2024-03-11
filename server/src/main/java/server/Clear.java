package server;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import service.GameService;
import spark.Request;
import spark.Response;


public class Clear {
    public Object clear(Request req, Response res) throws DataAccessException {
        new GameService().clear(Server.userDAO, Server.gameDAO, Server.authDAO);
        res.status(200);
        //var response = new Gson().toJson(res.body());
        return "{}";
    }
}
