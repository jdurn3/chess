package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.Error;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class Login {
    public Object login(Request req, Response res) {
        String information = req.body();
        UserData newUser = new Gson().fromJson(information, UserData.class);
        AuthData authToken = null;
        try {
            authToken = new UserService().register(newUser);
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
        var body = new Gson().toJson(authToken);
        res.type("application/json");
        res.status(200);
        res.body(body);
        return body;
    }
}
