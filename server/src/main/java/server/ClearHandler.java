package server;

import com.google.gson.Gson;
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
        var clearRequest = serializer.fromJson(req.body(), ClearService.class);
        //do I need the lines here from userhandler?
        try {
            var result = serializer.toJson(clearRequest);
            res.body(result);
            return res.body();
        } catch (Exception e) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("message", e.getMessage());
            res.status(500);
            res.body(serializer.toJson(temp));
            return res.body();
        }
    }
}
