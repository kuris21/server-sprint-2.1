package edu.brown.cs.student.main.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Moshi.Builder;
import edu.brown.cs.student.main.utilities.CSVUtility;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler for the viewcsv endpoint. This sends back the entire loaded CSV file's contents as a JSON
 * 2-dimensional array.
 */
public class ViewHandler implements Route {

  CSVUtility csvUtility;

  public ViewHandler(CSVUtility csvUtility) {
    this.csvUtility = csvUtility;
  }

  @Override
  public Object handle(Request request, Response response) {
    if (!this.csvUtility.getIsLoaded()) {
      return new ViewNotLoaded(
              "No csv loaded. Use endpoint 'loadcsv' with a path "
                  + "to your csv to load, then search.")
          .serialize();
    }
    return new ViewLoadedFile(this.csvUtility.getParsedCSV()).serialize();
  }

  /**
   * Record for message sent when csv is loaded
   *
   * @param result success message
   * @param csv json version of loaded csv
   */
  public record ViewLoadedFile(String result, List<List<String>> csv) {
    /**
     * Constructor
     *
     * @param csv<String>> of the csv the parsed csv
     */
    public ViewLoadedFile(List<List<String>> csv) {
      this("success", csv);
    }

    /**
     * Method serialize message into json
     *
     * @return json of the returned csv
     */
    public String serialize() {
      try {
        Moshi moshi = new Builder().build();
        JsonAdapter<ViewLoadedFile> adapter = moshi.adapter(ViewLoadedFile.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to serialize ViewLoadedFile: " + e.getMessage());
      }
    }
  }

  /**
   * Record for message returned when csv is not loaded
   *
   * @param result error_datasource since no csv loaded
   * @param ERRORMESSAGE informative message
   */
  public record ViewNotLoaded(String result, String ERRORMESSAGE) {
    /**
     * contructor
     *
     * @param ERRORMESSAGE informatice message
     */
    public ViewNotLoaded(String ERRORMESSAGE) {
      this("error_datasource", ERRORMESSAGE);
    }

    /**
     * Method serialize message into json
     *
     * @return json of the returned message
     */
    String serialize() {
      Moshi moshi = new Builder().build();
      return moshi.adapter(ViewNotLoaded.class).toJson(this);
    }
  }
}
