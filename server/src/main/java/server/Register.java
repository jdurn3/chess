package server;
import dataAccess.DataAccessException;
import model.Error;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import service.UserService;
import spark.*;
import com.google.gson.Gson;
import model.AuthData;

public class Register {
    public Object register(Request req, Response res) {
        String information = req.body();
        UserData newUser = new Gson().fromJson(information, UserData.class);
        AuthData authToken = null;
        try {
            authToken = new UserService().register(newUser);
        } catch (DataAccessException e) {
            if (e.getMessage().equals(Constants.BAD_REQUEST)) {
                Error error = new Error(e.getMessage());
                var body = new Gson().toJson(error);
                res.type("application/json");
                res.status(400);
                res.body(body);
                return body;
            }
            if (e.getMessage().equals(Constants.ALREADY_TAKEN)) {
                Error error = new Error(e.getMessage());
                var body = new Gson().toJson(error);
                res.type("application/json");
                res.status(403);
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
