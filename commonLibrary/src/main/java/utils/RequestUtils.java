package utils;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RequestUtils {

    public static RequestBody createRequestBody(String jsonString) {
        return RequestBody.create(MediaType.parse("application/json"),
                jsonString);

    }

    public static RequestBody createRequestBody(Map map) {
        return RequestBody.create(MediaType.parse("application/json"),
                new JSONObject(map).toString());

    }
}
