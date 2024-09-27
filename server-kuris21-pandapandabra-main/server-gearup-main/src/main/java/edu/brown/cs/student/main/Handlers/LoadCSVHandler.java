package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.utilities.CSVUtility;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadCSVHandler implements Route {
  private CSVUtility csvUtility;

  public LoadCSVHandler(CSVUtility csvUtility) {
    this.csvUtility = csvUtility;
  }

  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> resultMap = new HashMap<>();

    // Get the filepath from request
    String filepath = request.queryParams("filepath");

    if (filepath == null || filepath.isEmpty()) {
      resultMap.put("result", "error_bad_request");
      resultMap.put("message", "Missing filepath parameter.");
      return resultMap;
    }

    try {
      csvUtility.loadCSV(filepath); // Use the csvUtility instance
      resultMap.put("result", "success");
      resultMap.put("filepath", filepath);
    } catch (Exception e) {
      resultMap.put("result", "error_datasource");
      resultMap.put("message", e.getMessage());
    }

    return resultMap;
  }
}
