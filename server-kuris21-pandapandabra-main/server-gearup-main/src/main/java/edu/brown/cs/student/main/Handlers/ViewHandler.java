package edu.brown.cs.student.main.Handlers;

import edu.brown.cs.student.main.utilities.CSVUtility;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler for the viewcsv endpoint. This sends back the entire loaded CSV file's contents as a JSON
 * 2-dimensional array.
 */
public class ViewHandler implements Route {

  CSVUtility csvUtility = new CSVUtility();

  public ViewHandler(CSVUtility csvUtility) {
    this.csvUtility = csvUtility;
  }

  @Override
  public Object handle(Request request, Response response) {

    Map<String, Object> responseMap = new HashMap<>();

    Map<String, Object> resultMap = new HashMap<>();

    if (!csvUtility.getIsLoaded()) {
      resultMap.put("result", "error_datasource");
      resultMap.put("message", "No CSV file loaded.");
      return resultMap;
    }

    // Construct response
    resultMap.put("result", "success");
    resultMap.put("data", csvUtility.getParsedCSV());
    return responseMap;
  }
}
