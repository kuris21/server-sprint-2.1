package edu.brown.cs.student.main.server;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler for the viewcsv endpoint. This sends back the entire loaded CSV file's contents
 * as a JSON 2-dimensional array.
 */
public class ViewCsvHandler implements Route {

    public Object handle(Request request, Response response) {
        Map<String, Object> responseMap = new HashMap<>();

        // Check if CSV data has been loaded
        if (csvData == null || csvData.isEmpty()) {
            responseMap.put("result", "error_datasource");
            responseMap.put("message", "No CSV data loaded.");
            return responseMap;
        }

        // Construct response
        responseMap.put("result", "success");
        responseMap.put("data", csvData);
        return responseMap;
    }
}