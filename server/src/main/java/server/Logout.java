package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class Logout {
    public Object logout(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("Authorization");
        new UserService().logout(authToken);
    }
}
