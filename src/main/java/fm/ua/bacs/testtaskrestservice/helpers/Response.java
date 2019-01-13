package fm.ua.bacs.testtaskrestservice.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Response {

    public javax.ws.rs.core.Response makeResponse(String filename, String message, int status) {
        JsonObject jsonObj = new JsonObject();
        JsonArray jsonarray = new JsonArray();

        jsonObj.addProperty("filename", filename);
        jsonarray.add(jsonObj);

        JsonObject jsonMain = new JsonObject();
        jsonMain.add("Search", jsonarray);
        jsonObj.addProperty("message", message);

        return javax.ws.rs.core.Response.status(status).entity(jsonMain.toString()).build();
    }
}
