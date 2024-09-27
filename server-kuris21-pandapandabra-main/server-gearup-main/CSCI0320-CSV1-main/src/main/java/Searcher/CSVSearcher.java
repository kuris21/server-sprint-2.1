package Searcher;

import CSV_Exceptions.CSVParserException;
import ObjectCreators.TrivialCreator;
import Parser.Parser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVSearcher {

  private Parser<List<String>> parser;

  public CSVSearcher(Parser<List<String>> parser) {
    this.parser = parser;
  }

  /**
   * Search the CSV for rows that contain the specified value in a given column or across all
   * columns.
   *
   * @param filename The CSV file path.
   * @param searchValue The value to search for.
   * @param columnIdentifier The column index (0-based) or column name to search within. If null,
   *     search all columns.
   * @param hasHeader Whether the CSV file contains a header row.
   */
  public void search(
      String filename, String searchValue, String columnIdentifier, boolean hasHeader) {
    try {
      // Initialize the parser with the file
      FileReader reader = new FileReader(filename);
      parser = new Parser<>(reader, new TrivialCreator());
      parser.parse();

      List<List<String>> rows = parser.getParsedContent();
      List<String> headerRow = hasHeader ? rows.get(0) : null;

      // Filter rows that match the search criteria
      List<List<String>> matchingRows = new ArrayList<>();
      for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) { // Skip header row if applicable
        List<String> row = rows.get(i);
        if (matchesRow(row, searchValue, columnIdentifier, headerRow)) {
          matchingRows.add(row);
        }
      }

      // Output the matching rows
      if (matchingRows.isEmpty()) {
        System.out.println("No matches found.");
      } else {
        printMatches(matchingRows);
      }

    } catch (FileNotFoundException e) {
      System.err.println("Error: File not found - " + filename);
    } catch (CSVParserException e) {
      System.err.println("Error: Failed to parse the CSV file - " + e.getMessage());
    } catch (IOException e) {
      System.err.println("Error: Issue reading the CSV file - " + e.getMessage());
    }
  }

  /**
   * Check if a row matches the search criteria.
   *
   * @param row The row to check.
   * @param searchValue The value to search for.
   * @param columnIdentifier The column index (0-based) or column name to search within. If null,
   *     search all columns.
   * @param headerRow The header row, if applicable.
   * @return true if the row matches the search criteria, false otherwise.
   */
  private boolean matchesRow(
      List<String> row, String searchValue, String columnIdentifier, List<String> headerRow) {
    if (columnIdentifier == null) {
      // Search across all columns
      for (String cell : row) {
        if (cell.contains(searchValue)) {
          return true;
        }
      }
    } else {
      try {
        // If the column identifier is a valid integer, treat it as an index
        int columnIndex = Integer.parseInt(columnIdentifier);
        if (columnIndex >= 0 && columnIndex < row.size()) {
          return row.get(columnIndex).contains(searchValue);
        }
      } catch (NumberFormatException e) {
        // If the column identifier is not an integer, treat it as a column name
        if (headerRow != null) {
          int columnIndex = headerRow.indexOf(columnIdentifier);
          if (columnIndex != -1 && columnIndex < row.size()) {
            return row.get(columnIndex).contains(searchValue);
          }
        }
      }
    }
    return false;
  }

  /**
   * Print the matching rows to the console.
   *
   * @param matchingRows The rows that match the search criteria.
   */
  private void printMatches(List<List<String>> matchingRows) {
    for (List<String> row : matchingRows) {
      System.out.println(String.join(", ", row));
    }
  }
}
