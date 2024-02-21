package server;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import service.UserService;
import spark.*;
import com.google.gson.Gson;

public class Register {
    public Object register(Request req, Response res){
        String information = req.body();
        UserData newUser = new Gson().fromJson(information, UserData.class);
        new UserService().register(newUser);
    }
}
