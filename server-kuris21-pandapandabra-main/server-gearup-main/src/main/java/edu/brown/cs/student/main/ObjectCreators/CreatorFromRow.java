package edu.brown.cs.student.main.ObjectCreators;

import edu.brown.cs.student.main.CSV_Exceptions.FactoryFailureException;
import java.util.List;

public interface CreatorFromRow<T> {
  T create(List<String> row) throws FactoryFailureException;
}
