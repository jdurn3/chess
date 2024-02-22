package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class Login {
    public Object login(Request req, Response res) throws DataAccessException {
        String information = req.body();
        UserData newUser = new Gson().fromJson(information, UserData.class);
        AuthData authToken = new UserService().register(newUser);
        return new Gson().toJson(authToken);
    }
}
