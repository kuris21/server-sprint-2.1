package edu.brown.cs.student.main.server;

import spark.Request;
import spark.Response;
import spark.Route;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Exception;
import java.util.HashMap;
import java.util.Map;


public class LoadCSVHandler implements Route {
    private static String loadedCsvFilePath = null;

    @Override
    public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();

    //Get the filepath from request
    String filepath = request.queryParams("filepath");

    if (filepath == null) {
        responseMap.put("result", "error_bad_request");
        responseMap.put("message", "Missing filepath parameter.");
        return responseMap;
    }

    try {
        //Load the CSV file if it exists
        loadCSV(filepath);
        loadedCsvFilePath = filepath;
        responseMap.put("result", "success");
        responseMap.put("filepath", filepath);
    } catch (IOException e) {
        responseMap.put("result", "error_datasource");
        responseMap.put("message", "Failed to load the CSV file: " + filepath);
    }
    return responseMap;
    }

/**
 * Loads the CSV file located at the filepath
 *
 * @param filepath
 * @throws IOException if the file is not readable or doesn't exist
 */
private void loadCSV(Stirng filepath) throws IOException {
//Limit access
    if (!filepath.startsWith("csv/data/")) {
        throw new IOException("Access denied to this directory.");
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
        Strng row;
        // skip the first row
        reader.readLine();
        while ((row = reader.readLine()) != null) {
            System.out.println(row);
        }
    }

}
    /**
     * Returns the currently loaded CSV file path.
     *
     * @return loaded CSV file path or null if no CSV is loaded.
     */
    public static String getLoadedCsvFilePath() {
        return loadedCsvFilePath;
    }
}