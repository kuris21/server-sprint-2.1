package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.utilities.CSVUtility;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadCSVHandler implements Route {
  private CSVUtility csvUtility;

  public LoadCSVHandler(CSVUtility csvUtility) {
    this.csvUtility = csvUtility;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String filepath = request.queryParams("filepath");

    if (filepath == null || filepath.isEmpty()) {
      return new FileFailureLoaded("failure", filepath, "input is null or empty").serialize();
    }

    try {
      this.csvUtility.setPath(filepath);
      // this.csvUtility.loadAndParseCSV(); // Make sure this method is implemented in CSVUtility
      this.csvUtility.setIsLoaded(true);
      return new FileSuccessfullyLoaded(this.csvUtility.getPath()).serialize();
    } catch (Exception e) {
      return new FileFailureLoaded("failure", filepath, "error_datasource: " + e.getMessage())
          .serialize();
    }
  }

  public record FileSuccessfullyLoaded(String result, String filePath) {
    public FileSuccessfullyLoaded(String filePath) {
      this("success", filePath);
    }

    public String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<FileSuccessfullyLoaded> adapter = moshi.adapter(FileSuccessfullyLoaded.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to serialize: " + e.getMessage());
      }
    }
  }

  public record FileFailureLoaded(String result, String filePath, String errorMessage) {
    public String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<FileFailureLoaded> adapter = moshi.adapter(FileFailureLoaded.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to serialize: " + e.getMessage());
      }
    }
  }
}
