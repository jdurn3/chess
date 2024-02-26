package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.Error;
import spark.Response;

public class Exceptions {
    public static String giveException(DataAccessException e, Response res) {
        if (e.getMessage().equals(Constants.BAD_REQUEST)) {
            Error error = new Error(e.getMessage());
            var body = new Gson().toJson(error);
            res.type("application/json");
            res.status(400);
            res.body(body);
            return body;
        }
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
        if (e.getMessage().equals(Constants.ALREADY_TAKEN)) {
            Error error = new Error(e.getMessage());
            var body = new Gson().toJson(error);
            res.type("application/json");
            res.status(403);
            res.body(body);
            return body;
        }
        return null;
    }
}
