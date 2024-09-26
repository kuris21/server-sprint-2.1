package ObjectCreators;

import java.util.List;

public class TrivialCreator implements CreatorFromRow<List<String>> {
  @Override
  public List<String> create(List<String> row) {
    return row;
  }

  public static int rowSize(List<String> row) {
    return row.size();
  }
}
