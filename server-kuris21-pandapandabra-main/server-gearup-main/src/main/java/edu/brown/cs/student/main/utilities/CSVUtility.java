package edu.brown.cs.student.main.utilities;

import java.util.Collections;
import java.util.List;

public class CSVUtility {

  String path;
  /** Allows easy check is a csv has been loaded for search and view csv */
  boolean isLoaded;

  /** Allows search and view to access parsed csv */
  List<List<String>> parsedCSV;

  /**
   * Constructor for Loaded csv Will always initialize with these values, and will be filled when
   * loadcsv is requested
   */
  public CSVUtility() {
    this.path = "";
    this.isLoaded = false;
    this.parsedCSV = Collections.emptyList();
  }

  /**
   * Setter for path
   *
   * @param path csv file path String
   */
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * Getter for path
   *
   * @return csv file path String
   */
  public String getPath() {
    return path;
  }

  /**
   * Setter for isLoaded
   *
   * @param isLoaded boolean if a csv has been loaded
   */
  public void setIsLoaded(boolean isLoaded) {
    this.isLoaded = isLoaded;
  }

  /**
   * Getter for isLoaded
   *
   * @return boolean if csv has been loaded
   */
  public boolean getIsLoaded() {
    return this.isLoaded;
  }

  /**
   * Setter for parsedCSV
   *
   * @param parsedCSV List<List<String>> of the parsed csv
   */
  public void setParsedCSV(List<List<String>> parsedCSV) {
    this.parsedCSV = parsedCSV;
  }

  /**
   * Getter for parsed csv
   *
   * @return List<List<String>> of the parsed csv
   */
  public List<List<String>> getParsedCSV() {
    return parsedCSV;
  }
}
