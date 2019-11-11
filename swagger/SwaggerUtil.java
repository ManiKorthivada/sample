package ahm.content.service.core.utils;

import ahm.content.service.core.services.SwaggerJsonService;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SwaggerUtil {

    public static void setDefaultJsonValues(JSONObject finalSwaggerJson) throws JSONException {
        finalSwaggerJson.put("swagger", 2.0);
        finalSwaggerJson.put("host", "localhost:4502");
        finalSwaggerJson.put("basePath", "/digital");
    }

    public static JSONObject setInfoObject() throws JSONException {
        JSONObject infoObject = new JSONObject();
        infoObject.put("title", "Digital Service");
        infoObject.put("version", "0.0.1");
        return infoObject;
    }

    public static JSONArray setSchemaArray() {
        JSONArray schemaJson = new JSONArray();
        schemaJson.put("https");
        schemaJson.put("http");
        return schemaJson;
    }

    public static void setEachMethodJson(JSONObject pathsJson, SwaggerJsonService swaggerJsonService) throws JSONException {
        JSONObject getJson = new JSONObject();
        JSONObject contentJson = new JSONObject();
        pathsJson.put("/content/{" + swaggerJsonService.getApiName() + "}", contentJson);
        contentJson.put("get", getJson);
        JSONArray tagsArray = new JSONArray();
        tagsArray.put("Widgets");
        getJson.put("tags", tagsArray);
        getJson.put("summary", swaggerJsonService.getApiDescription());
        JSONArray parametersArray = new JSONArray();
        getJson.put("parameters", parametersArray);
        JSONObject insideParam = new JSONObject();
        insideParam.put("name", swaggerJsonService.getApiName());
        insideParam.put("description", swaggerJsonService.getApiDescription());
        insideParam.put("required", true);
        insideParam.put("type", "String");
        insideParam.put("in", "path");
        parametersArray.put(insideParam);

        getResponseCodes(swaggerJsonService, getJson);
    }

    private static void getResponseCodes(SwaggerJsonService swaggerJsonService, JSONObject getJson) throws JSONException {
        String[] apiResponses = swaggerJsonService.getApiResponses();
        JSONObject responses = new JSONObject();
        for (String eachResponse : apiResponses) {
            JSONObject responseObject = new JSONObject();
            String[] responseSplit = eachResponse.split(":");
            responseObject.put("description", responseSplit[1]);
            responses.put(responseSplit[0], responseObject);
        }
        getJson.put("responses", responses);
    }

}
