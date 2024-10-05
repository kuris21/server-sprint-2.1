package edu.brown.cs.student.main.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import edu.brown.cs.student.main.Searcher.CSVSearcher;
import edu.brown.cs.student.main.utilities.CSVUtility;
import java.util.List;
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

      Set<String> params = request.queryParams();

      if (!this.csvUtility.getIsLoaded()) {
        return new FailureToSearch(
            "error_datasource",
            params.toString(),
            "No csv loaded. Use endpoint 'loadcsv' with a path to your csv to load, then search."
        ).serialize();
      }

      String searchString = request.queryParams("searchString");
      if (searchString == null || searchString.isEmpty()) {
        return new FailureToSearch(
            "error_bad_request",
            params.toString(),
            "Must specify value of searchString."
        ).serialize();
      }

      String columnIdentifier = request.queryParams("columnIdentifier");
      if (columnIdentifier == null || columnIdentifier.isEmpty()) {
        return new FailureToSearch(
            "error_bad_request",
            params.toString(),
            "Must specify value of columnIdentifier. Write \"none\" if none"
        ).serialize();
      }

      boolean hasHeaders = true;
      String hasHeadersParam = request.queryParams("hasHeader");
      if (hasHeadersParam != null) {
        if (hasHeadersParam.equals("true")) {
          hasHeaders = true;
        } else if (hasHeadersParam.equals("false")) {
          hasHeaders = false;
        } else {
          return new FailureToSearch(
              "error_bad_request",
              params.toString(),
              "Parameter hasHeaders must be either 'true' or 'false'."
          ).serialize();
        }
      }

      try {
        // Assuming csvSearcher.search() returns a List<List<String>> of search results
        List<List<String>> searchResults = this.csvSearcher.search(this.csvUtility.getPath(), searchString, columnIdentifier, hasHeaders);
        System.out.println(searchResults);
        return new SuccessfullySearched(params.toString(), "Found " + searchResults + " matching rows.").serialize();
      } catch (Exception e) {
        return new FailureToSearch(
            "error_bad_request",
            params.toString(),
            "Error during search: " + e.getMessage()
        ).serialize();
      }
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
