package edu.brown.cs.student.main.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import edu.brown.cs.student.main.Searcher.CSVSearcher;
import edu.brown.cs.student.main.utilities.CSVUtility;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchHandler implements Route {
  CSVUtility csvUtility;
  CSVSearcher csvSearcher;

  public SearchHandler(CSVUtility csvUtility) {
    this.csvUtility = csvUtility;
  }

  @Override
  public Object handle(Request request, Response response) {

    String searchTerm = "";
    String colName = "";
    boolean hasHeaders = true;

    Set<String> params = request.queryParams();

    if (!this.csvUtility.getIsLoaded()) {
      return new FailureToSearch(
              "error_datasource",
              params.toString(),
              "No csv loaded. Use endpoint 'loadcsv' with a path "
                  + "to your csv to load, then search.")
          .serialize();
    }

    if (params.contains("term")) {
      searchTerm = request.queryParams("term");
    } else {
      return new FailureToSearch(
              "error_bad_request",
              params.toString(),
              "Must have parameter 'term' for term to search.")
          .serialize();
    }

    if (params.contains("col")) {
      colName = request.queryParams("col");
    } else {
      return new FailureToSearch(
              "error_bad_request",
              params.toString(),
              "Must have parameter 'col' for term to search.")
          .serialize();
    }

    if (params.contains("hasHeaders")) {
      if (request.queryParams("hasHeaders").equals("true")) {
        hasHeaders = true;
      } else if (request.queryParams("hasHeaders").equals("false")) {
        hasHeaders = false;
      } else {
        return new FailureToSearch(
                "error_bad_request",
                params.toString(),
                "Parameter hasHeaders must be either 'true' or 'false'.")
            .serialize();
      }
    }

    csvSearcher.search(this.csvUtility.getPath(), searchTerm, colName, hasHeaders);

    return new SuccessfullySearched(params.toString(), "searched");
  }

  /**
   * Record for when item as successfully searched for
   *
   * @param result success
   * @param params params passed into query
   * @param SUCCESSMESSAGE message telling user how many rows item was found in
   */
  public record SuccessfullySearched(String result, String params, String SUCCESSMESSAGE) {
    /**
     * Constructor
     *
     * @param params params passed into query
     * @param SUCCESSMESSAGE message telling user how many rows item was found in
     */
    public SuccessfullySearched(String params, String SUCCESSMESSAGE) {
      this("success", params, SUCCESSMESSAGE);
    }
    /**
     * Method serialize message into json
     *
     * @return json of success message, rows, params
     */
    String serialize() {
      try {
        Moshi moshi =
            new Moshi.Builder()
                .add(PolymorphicJsonAdapterFactory.of(CSVUtility.class, "path"))
                .build();
        JsonAdapter<SuccessfullySearched> adapter = moshi.adapter(SuccessfullySearched.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  /**
   * Record for when search failed
   *
   * @param result correct error message
   * @param params params passed into query
   * @param ERRORMESSAGE message informing user of what went wrong and options to fix
   */
  public record FailureToSearch(String result, String params, String ERRORMESSAGE) {
    /**
     * Constructor
     *
     * @param params params passed into query
     * @param ERRORMESSAGE message informing user of what went wrong and options to fix
     */
    public FailureToSearch(String params, String ERRORMESSAGE) {
      this("failure", params, ERRORMESSAGE);
    }

    /**
     * Method serialize message into json
     *
     * @return json of failure message
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(FailureToSearch.class).toJson(this);
    }
  }
}
