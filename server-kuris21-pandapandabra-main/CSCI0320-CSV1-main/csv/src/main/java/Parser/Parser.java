package Parser;

import CSV_Exceptions.FactoryFailureException;
import ObjectCreators.CreatorFromRow;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import CSV_Exceptions.CSVParserException;

/**
 * CSV parser class that reads and parses CSV data from any Reader object.
 */
public class Parser<T> {

  private BufferedReader bufferedReader;
  private List<T> parsedContent;
  private CreatorFromRow<T> creator;

  /**
   * Constructor that accepts any Reader object. The Reader could be reading from a file,
   * a string, or any other data source that extends the Reader class.
   *
   * @param reader - a Reader object to read the CSV data
   * @param creator - an object that implements CreatorFromRow<T>, used to convert rows to objects
   * @throws CSVParserException if the provided reader is null
   */
  public Parser(Reader reader, CreatorFromRow<T> creator) throws CSVParserException {
    if (reader == null) {
      throw new CSVParserException("Reader cannot be null");
    }
    if (creator == null) {
      throw new CSVParserException("Creator cannot be null");
    }
    try {
      this.bufferedReader = new BufferedReader(reader);
    } catch (Exception e) {
      throw new CSVParserException("Failed to initialize BufferedReader", e);
    }
    this.parsedContent = new ArrayList<>();
    this.creator = creator;
  }

  /**
   * Parses the CSV data from the provided Reader.
   *
   * @throws CSVParserException if an error occurs during parsing
   */
  public void parse() throws CSVParserException {
    String line;
    Pattern regexSplitCSVRow = Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

    try {
      while ((line = bufferedReader.readLine()) != null) {
        String[] result = regexSplitCSVRow.split(line);
        List<String> lineToArr = Arrays.stream(result).toList();
        // parsedContent.add(lineToArr);

      try {
        T obj = creator.create(lineToArr);
        parsedContent.add(obj);
      } catch (FactoryFailureException e){
        throw new CSVParserException("Failed to create object from row: " + e.getMessage(), e);
        }
      }

    } catch (IOException e) {
      throw new CSVParserException("Error reading CSV data", e);

    } finally {

      try {
        bufferedReader.close();
      } catch (IOException e) {
        throw new CSVParserException("Error closing reader", e);
      }
    }
  }

  /**
   * Returns the parsed content as a List of Strings.
   *
   * @return parsed content
   */
  public List<T> getParsedContent() {
    return parsedContent;
  }
}