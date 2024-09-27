package edu.brown.cs.student.main.utilities;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVUtility {

  private List<String[]> csvData = new ArrayList<>();
  private boolean csvLoaded = false;

  // Load the CSV file from the given filepath
  public void loadCSV(String filepath) throws IOException {
    csvData.clear(); // Clear previous CSV data if any
    try (Scanner scanner = new Scanner(new FileReader(filepath))) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] row = line.split(","); // Assuming the CSV is comma-separated
        csvData.add(row);
      }
      csvLoaded = true;
      System.out.println(
          "Successfully loaded " + filepath); // Mark that a CSV has been successfully loaded
    } catch (IOException e) {
      csvLoaded = false;
      throw new IOException("Failed to load CSV from " + filepath + ": " + e.getMessage());
    }
  }

  // Check if a CSV file is currently loaded
  public boolean isCSVLoaded() {
    return csvLoaded;
  }

  // Retrieve the full CSV data as a List of String arrays
  public List<String[]> getCSVData() {
    return csvData;
  }
}
