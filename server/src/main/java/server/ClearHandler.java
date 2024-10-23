package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.ClearService;
import service.OtherException;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ClearHandler {
    public ClearService clearService;

    public Object clear(Request req, Response res) { //should I do void or Object?
        var serializer = new Gson();
        //do I need the lines here from userhandler?
        try {
            new ClearService().clear();
            return "{}";
        } catch (Exception e) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(500);
            res.body(serializer.toJson(temp));
            return res.body();
        }
    }
}
