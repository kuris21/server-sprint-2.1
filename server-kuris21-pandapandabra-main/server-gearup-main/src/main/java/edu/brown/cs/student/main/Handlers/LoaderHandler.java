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

    if (filepath == null|| filepath.isEmpty()) {
        responseMap.put("result", "error_bad_request");
        responseMap.put("message", "Missing filepath parameter.");
        return responseMap;
    }

        try {
            csvUtility.loadCSV(filepath);
            resultMap.put("result", "success");
            resultMap.put("filepath", filepath);
        } catch (Exception e) {
            resultMap.put("result", "error_datasource");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }
}

/**
 * Loads the CSV file located at the filepath
 *
 * @param filepath
 * @throws IOException if the file is not readable or doesn't exist
 */
/*
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
/*
    public static String getLoadedCsvFilePath() {
        return loadedCsvFilePath;
    }

 */
}