import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CollectionToArray {

  private String[] expected = {"a", "b", "c"};
  private List<String> list = List.of("a", "b", "c");


  @Test
  void convertListToArray() {
    String[] strings_fun = list.toArray(String[]::new);

    Assertions.assertArrayEquals(expected, strings_fun);
  }

  @Test
  void convertListToArrayBeforeJava11() {
    String[] strings = list.toArray(new String[0]);

    Assertions.assertArrayEquals(expected, strings);
  }

}
