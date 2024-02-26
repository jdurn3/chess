package server;
import com.google.gson.Gson;
import service.GameService;
import spark.Request;
import spark.Response;


public class Clear {
    public Object clear(Request req, Response res) {
        new GameService().clear();
        res.status(200);
        //var response = new Gson().toJson(res.body());
        return "{}";
    }
}
