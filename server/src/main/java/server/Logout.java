package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.Error;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class Logout {
    public Object logout(Request req, Response res) {
        String authToken = req.headers("Authorization");
        try {
            new UserService().logout(authToken, Server.authDAO);
        } catch (DataAccessException e) {
            return Exceptions.giveException(e, res);
        }
        res.status(200);
        return "{}";
    }
}
