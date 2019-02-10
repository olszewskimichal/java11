import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;
import org.junit.jupiter.api.Test;

class LambdasJava11 {

  @Test
  void test1() {
    Function<String, String> append = (/*@NotNull*/ var string) -> string + " World";
    String appendedString = append.apply("Hello");

    assertEquals("Hello World", appendedString);
  }
}
