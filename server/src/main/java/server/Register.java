package server;
import dataAccess.DataAccessException;
import model.Error;
import model.RegisterResponse;
import model.UserData;
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
            authToken = new UserService().register(newUser, Server.userDAO, Server.authDAO);
        } catch (DataAccessException e) {
            return Exceptions.giveException(e, res);
        }
        RegisterResponse response = new RegisterResponse(newUser.username(), authToken.authToken());
        var body = new Gson().toJson(response);
        res.type("application/json");
        res.status(200);
        res.body(body);
        return body;
        }
    }

