package edu.brown.cs.student.main.ObjectCreators;

import edu.brown.cs.student.main.CSV_Exceptions.FactoryFailureException;
import edu.brown.cs.student.main.DataObjects.Star;
import java.util.List;

public class StarCreator implements CreatorFromRow<Star> {

  @Override
  public Star create(List<String> row) throws FactoryFailureException {
    if (row.size() != 5) {
      throw new FactoryFailureException("Expected 5 columns but got " + row.size(), row);
    }

    try {
      int starID = Integer.parseInt(row.get(0));

      String properName = row.get(1);
      if (properName == null || properName.trim().isEmpty()) {
        throw new FactoryFailureException("Invalid or missing star name in row: " + row, row);
      }

      double x = Double.parseDouble(row.get(2));
      double y = Double.parseDouble(row.get(3));
      double z = Double.parseDouble(row.get(4));

      return new Star(starID, properName, x, y, z);

    } catch (NumberFormatException e) {
      throw new FactoryFailureException("Invalid number format in row: " + row, row);
    }
  }
}
