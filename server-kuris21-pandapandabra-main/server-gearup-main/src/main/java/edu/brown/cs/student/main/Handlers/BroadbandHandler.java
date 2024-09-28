package edu.brown.cs.student.main.server;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler for the broadband endpoint. It returns mocked broadband data for a given state and
 * county.
 */
public class BroadbandHandler implements Route {

  // Mocked data for broadband access (State, County, Broadband Percentage)
  private static final String[][] MOCKED_DATA = {{"Shiawassee County", "Michigan", "81.5"}};

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>();

    // Get query parameters for state and county
    String state = request.queryParams("state");
    String county = request.queryParams("county");

    // check for invalid inputs
    if (state == null || county == null || state.isEmpty() || county.isEmpty()) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("message", "Missing state or county parameter.");
      return responseMap;
    }

    // Search for matching state and county
    String broadbandPercentage = null;
    for (String[] data : MOCKED_DATA) {
      if (data[1].equalsIgnoreCase(state) && data[0].equalsIgnoreCase(county)) {
        broadbandPercentage = data[2];
        break;
      }
    }

    if (broadbandPercentage == null) {
      responseMap.put("result", "error_datasource");
      responseMap.put("message", "Broadband data not found for the specified state and county.");
    } else {
      responseMap.put("result", "success");
      responseMap.put("state", state);
      responseMap.put("county", county);
      responseMap.put("broadband_percentage", broadbandPercentage);
      responseMap.put("dateTime", LocalDateTime.now().toString()); // Mocked retrieval dateTime
    }

    return responseMap;
  }
}
