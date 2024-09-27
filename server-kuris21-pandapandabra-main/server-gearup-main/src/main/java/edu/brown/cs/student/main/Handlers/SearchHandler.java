

package edu.brown.cs.student.main.server;


import spark.Request;
import spark.Response;
import spark.Route;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Handler for the searchcsv endpoint. This searches a loaded CSV file
 * for rows matching the search criteria.
 */
public class SearchCsvHandler implements Route {


    // Map to store the loaded CSV data.
    private List<List<String>> csvData;
    private Map<String, Integer> columnHeaders;


    // Constructor to initialize the handler with the loaded CSV data
    public SearchCsvHandler(List<List<String>> csvData, Map<String, Integer> columnHeaders) {
        this.csvData = csvData;
        this.columnHeaders = columnHeaders;
    }


    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, Object> responseMap = new HashMap<>();


        // Check if CSV data has been loaded
        if (csvData == null || csvData.isEmpty()) {
            responseMap.put("result", "error_datasource");
            responseMap.put("message", "No CSV data loaded.");
            return responseMap;
        }


        // Get query parameters for search
        String columnIndexParam = request.queryParams("colIndex");
        String columnHeaderParam = request.queryParams("colHeader");
        String searchTerm = request.queryParams("searchTerm");


        // Validate query parameters
        if (searchTerm == null || searchTerm.isEmpty()) {
            responseMap.put("result", "error_bad_request");
            responseMap.put("message", "Missing searchTerm parameter.");
            return responseMap;
        }


        List<List<String>> searchResults = new ArrayList<>();


        // Search by column index
        if (columnIndexParam != null) {
            try {
                int colIndex = Integer.parseInt(columnIndexParam);
                if (colIndex < 0 || colIndex >= csvData.get(0).size()) {
                    responseMap.put("result", "error_bad_request");
                    responseMap.put("message", "Invalid column index: " + colIndex);
                    return responseMap;
                }
                searchResults = searchByColumnIndex(colIndex, searchTerm);
            } catch (NumberFormatException e) {
                responseMap.put("result", "error_bad_request");
                responseMap.put("message", "Invalid column index format.");
                return responseMap;
            }
        }
        // Search by column header
        else if (columnHeaderParam != null) {
            Integer colIndex = columnHeaders.get(columnHeaderParam);
            if (colIndex == null) {
                responseMap.put("result", "error_bad_request");
                responseMap.put("message", "Invalid column header: " + columnHeaderParam);
                responseMap.put("available_headers", columnHeaders.keySet());
                return responseMap;
            }
            searchResults = searchByColumnIndex(colIndex, searchTerm);
        }
        // Search across all columns
        else {
            searchResults = searchAcrossAllColumns(searchTerm);
        }


        // Construct response
        responseMap.put("result", "success");
        responseMap.put("data", searchResults);
        return responseMap;
    }


    /**
     * Searches a specific column of the CSV for matching rows.
     *
     * @param colIndex   The index of the column to search.
     * @param searchTerm The search term to look for.
     * @return A list of matching rows.
     */
    private List<List<String>> searchByColumnIndex(int colIndex, String searchTerm) {
        List<List<String>> results = new ArrayList<>();
        for (List<String> row : csvData) {
            if (row.get(colIndex).equalsIgnoreCase(searchTerm)) {
                results.add(row);
            }
        }
        return results;
    }


    /**
     * Searches all columns of the CSV for matching rows.
     *
     * @param searchTerm The search term to look for.
     * @return A list of matching rows.
     */
    private List<List<String>> searchAcrossAllColumns(String searchTerm) {
        List<List<String>> results = new ArrayList<>();
        for (List<String> row : csvData) {
            for (String field : row) {
                if (field.equalsIgnoreCase(searchTerm)) {
                    results.add(row);
                    break; // Once a match is found in a row, no need to search further
                }
            }
        }
        return results;
    }
}
