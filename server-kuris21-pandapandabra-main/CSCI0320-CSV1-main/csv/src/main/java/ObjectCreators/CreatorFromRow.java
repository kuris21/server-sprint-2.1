package ObjectCreators;

import CSV_Exceptions.FactoryFailureException;
import java.util.List;

public interface CreatorFromRow <T> {
  T create(List<String> row) throws FactoryFailureException;
}

