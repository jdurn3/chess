package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class Logout {
    public Object logout(Request req, Response res) {
        String authToken = req.headers("Authorization");
        try {
            new UserService().logout(authToken);
        } catch (DataAccessException e) {
            if (e.getMessage().equals(Constants.UNAUTHORIZED)) {
                var body = new Gson().toJson(e.getMessage());
                res.type("application/json");
                res.status(401);
                res.body(body);
                return body;
            }
        }
        res.status(200);
        return res;
    }
}
