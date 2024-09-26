package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.*;
import CSV_Exceptions.CSVParserException;
import ObjectCreators.TrivialCreator;
import Parser.Parser;
import java.io.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import DataObjects.Star;
import ObjectCreators.StarCreator;

/**
 * TODO: add more tests in this file to build an extensive test suite for your parser and parsing
 * functionalities
 *
 * <p>Tests for the parser class
 */
public class ParserTest {

  Parser incomeByRaceParser;
  Parser malformedParser;

  @Test
  public void testParseRegCSV() {
    try {
      FileReader reader = new FileReader("data/census/income_by_race.csv");
      TrivialCreator creator = new TrivialCreator();
      incomeByRaceParser = new Parser(reader, creator);
      incomeByRaceParser.parse();

    } catch (FileNotFoundException e) {
      fail("File not found: " + e.getMessage());
    } catch (CSVParserException e) {
      fail("CSV parser exception: " + e.getMessage()); // Catch your custom exception
    }

     incomeByRaceParser.getParsedContent();

    assertEquals(324, incomeByRaceParser.getParsedContent().size());

    assertEquals(
        List.of(
            "7",
            "Two Or More",
            "2017",
            "2017",
            "44000",
            "11831",
            "\"Kent County, RI\"",
            "05000US44003",
            "kent-county-ri"),
        incomeByRaceParser.getParsedContent().get(143));
    assertFalse(
        incomeByRaceParser.getParsedContent().contains(List.of("Gemini", "Roberto", "Nick")));
  }

  @Test
  public void testParseMalformedCSV() {
    try {
      FileReader reader = new FileReader("data/malformed/malformed_signs.csv");
      malformedParser = new Parser(reader, new TrivialCreator());
      malformedParser.parse();

    } catch (FileNotFoundException e) {
      fail("File not found: " + e.getMessage());
    } catch (CSVParserException e) {
      fail("CSV parser exception: " + e.getMessage()); // Catch your custom exception
    }

    assertEquals(13, malformedParser.getParsedContent().size());
    assertEquals(List.of("Aquarius"), malformedParser.getParsedContent().get(11));
    assertEquals(List.of("Gemini", "Roberto", "Nick"), malformedParser.getParsedContent().get(3));
  }

  @Test
  public void testFileNotFoundParse(){
    assertThrows(FileNotFoundException.class,
        () -> {
      FileReader reader = new FileReader("data/census/housing.csv");
      new Parser(reader, new TrivialCreator());
        });
  }

  @Test
  public void testNullReaderThrowsException() {
    CSVParserException exception = assertThrows(CSVParserException.class, () -> {
      new Parser<>(null, new TrivialCreator());
    });
    assertTrue(exception.getMessage().contains("Reader cannot be null"));
  }

  @Test
  public void testNullCreatorThrowsException() {
    CSVParserException exception = assertThrows(CSVParserException.class, () -> {
      new Parser<>(new StringReader("Name,Age,Location"), null);
    });
    assertTrue(exception.getMessage().contains("Creator cannot be null"));
  }

  @Test
  public void testValidParsingWithStarCreator() {
    try {
      String csvData = "0,Sol,0,0,0\n" + "1,Andreas,282.43485,0.00449,5.36884";
      Parser<Star> parser = new Parser<>(new StringReader(csvData), new StarCreator());
      parser.parse();

      List<Star> parsedStars = parser.getParsedContent();
      assertEquals(2, parsedStars.size());

      Star firstStar = parsedStars.get(0);
      assertEquals(0, firstStar.getStarID());
      assertEquals("Sol", firstStar.getName());
      assertEquals(0.0, firstStar.getX());
      assertEquals(0.0, firstStar.getY());
      assertEquals(0.0, firstStar.getZ());

      Star secondStar = parsedStars.get(1);
      assertEquals(1, secondStar.getStarID());
      assertEquals("Andreas", secondStar.getName());
      assertEquals(282.43485, secondStar.getX());
      assertEquals(0.00449, secondStar.getY());
      assertEquals(5.36884, secondStar.getZ());

    } catch (CSVParserException e) {
      fail("Parsing failed: " + e.getMessage());
    }
  }

  @Test
  public void testIncompleteRowThrowsException() {
    String csvData = "StarID,ProperName,X,Y,Z\n" +
        "0,Sol,0,0";

    Parser<Star> parser;
    try {
      parser = new Parser<>(new StringReader(csvData), new StarCreator());
      CSVParserException exception = assertThrows(CSVParserException.class, parser::parse);
      assertTrue(exception.getMessage().contains("Failed to create object from row"));
    } catch (CSVParserException e) {
      fail("Test setup failed: " + e.getMessage());
    }
  }

  // Test handling invalid number format in a row
  @Test
  public void testInvalidNumberFormatThrowsException() {
    String csvData = "StarID,ProperName,X,Y,Z\n" +
        "0,Sol,notANumber,0,0";  // Invalid number for X coordinate

    Parser<Star> parser;
    try {
      parser = new Parser<>(new StringReader(csvData), new StarCreator());
      CSVParserException exception = assertThrows(CSVParserException.class, parser::parse);
      assertTrue(exception.getMessage().contains("Invalid number format in row"));
    } catch (CSVParserException e) {
      fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  public void testEmptyStarNameThrowsException() {
    String csvData = "1,,282.43485,0.00449,5.36884";  // Empty name
    Parser<Star> parser;
    try {
      parser = new Parser<>(new StringReader(csvData), new StarCreator());
      // The parser should throw a CSVParserException
      CSVParserException exception = assertThrows(CSVParserException.class, parser::parse);
      // Check that the message of the CSVParserException contains the correct error message
      assertTrue(exception.getMessage().contains("Invalid or missing star name"));
      assertTrue(exception.getMessage().contains("Failed to create object from row"));
    } catch (CSVParserException e) {
      fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  public void testRowWithIncorrectColumnCountThrowsException() {
    String csvData = "1,Sol,282.43485,0.00449";

    Parser<Star> parser;
    try {
      parser = new Parser<>(new StringReader(csvData), new StarCreator());
      // The parser should throw a CSVParserException
      CSVParserException exception = assertThrows(CSVParserException.class, parser::parse);
      // Check that the message of the CSVParserException contains the correct error message
      assertTrue(exception.getMessage().contains("Expected 5 columns but got 4"),
          "Expected the exception message to indicate the wrong number of columns.");
      assertTrue(exception.getMessage().contains("Failed to create object from row"),
          "Expected the exception to indicate a failed object creation.");
    } catch (CSVParserException e) {
      fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  public void testRowWithInvalidNumberFormatThrowsException() {
    String csvData = "1,Sol,invalid,0.00449,5.36884";
    Parser<Star> parser;
    try {
      parser = new Parser<>(new StringReader(csvData), new StarCreator());
      // The parser should throw a CSVParserException
      CSVParserException exception = assertThrows(CSVParserException.class, parser::parse);
      // Check that the message of the CSVParserException contains the correct error message
      assertTrue(exception.getMessage().contains("Invalid number format in row"),
          "Expected the exception message to indicate an invalid number format.");
      assertTrue(exception.getMessage().contains("Failed to create object from row"),
          "Expected the exception to indicate a failed object creation.");
    } catch (CSVParserException e) {
      fail("Test setup failed: " + e.getMessage());
    }
  }
}