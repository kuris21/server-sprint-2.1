package edu.brown.cs.student.main.CSV_Exceptions;

public class CSVParserException extends Exception {
  public CSVParserException(String message) {
    super(message);
  }

  public CSVParserException(String message, Throwable cause) {
    super(message, cause);
  }
}
