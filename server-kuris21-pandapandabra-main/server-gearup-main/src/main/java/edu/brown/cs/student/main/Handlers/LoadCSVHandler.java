package edu.brown.cs.student.main.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.ObjectCreators.TrivialCreator;
import edu.brown.cs.student.main.Parse.Parser;
import edu.brown.cs.student.main.utilities.CSVUtility;
import java.io.FileReader;
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
    FileReader readFile = new FileReader(filepath);

    Parser parser = new Parser(readFile, new TrivialCreator());
    parser.parse();

    if (filepath == null || filepath.isEmpty()) {
      return new FileFailureLoaded("failure", filepath, "input is null or empty").serialize();
    }
    try {

      this.csvUtility.setPath(filepath);
      this.csvUtility.setIsLoaded(true);
      this.csvUtility.setParsedCSV(parser.getParsedContent());

      System.out.println(parser.getParsedContent());

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
