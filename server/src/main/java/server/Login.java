package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.Error;
import model.RegisterResponse;
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
            authToken = new UserService().login(newUser, Server.userDAO, Server.authDAO);
        } catch (DataAccessException e) {
            return Exceptions.giveException(e, res);
        }
        var body = new Gson().toJson(new RegisterResponse(authToken.username(), authToken.authToken()));
        res.type("application/json");
        res.status(200);
        res.body(body);
        return body;
    }
}
