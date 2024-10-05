package edu.brown.cs.student.main.Searcher;


import edu.brown.cs.student.main.CSV_Exceptions.CSVParserException;
import edu.brown.cs.student.main.ObjectCreators.TrivialCreator;
import edu.brown.cs.student.main.Parse.Parser;
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
   * Search the CSV for rows that contain the specified value in a given column or across all columns.
   *
   * @param filename The CSV file path.
   * @param searchValue The value to search for.
   * @param columnIdentifier The column index (0-based) or column name to search within. If "none", search all columns.
   * @param hasHeader Whether the CSV file contains a header row.
   * @return A list of matching rows.
   * @throws FileNotFoundException if the CSV file is not found.
   * @throws CSVParserException if there's an error parsing the CSV file.
   * @throws IOException if there's an issue reading the CSV file.
   */
  public List<List<String>> search(
      String filename, String searchValue, String columnIdentifier, boolean hasHeader)
      throws FileNotFoundException, CSVParserException, IOException {

    FileReader reader = new FileReader(filename);
    parser = new Parser<>(reader, new TrivialCreator());
    parser.parse();

    List<List<String>> rows = parser.getParsedContent();
    List<String> headerRow = hasHeader ? rows.get(0) : null;

    List<List<String>> matchingRows = new ArrayList<>();
    for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
      List<String> row = rows.get(i);
      if (matchesRow(row, searchValue, columnIdentifier, headerRow)) {
        matchingRows.add(row);
      }
    }

    return matchingRows;
  }

  private boolean matchesRow(
      List<String> row, String searchValue, String columnIdentifier, List<String> headerRow) {
    if (columnIdentifier.equals("none")) {
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
}