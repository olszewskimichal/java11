import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class StringUtilsJava11 {

  @Test
  void repeatedString() {
    var str = "abc";
    var repeated = str.repeat(3);

    assertEquals("abcabcabc", repeated);
  }

  @Test
  void linesFromMultilineString() {
    var multiline = "My\nname is\nabc";
    Stream<String> stream = multiline.lines();
    List<String> collect = stream.map(String::toUpperCase)
        .collect(Collectors.toList());

    assertEquals(collect, List.of("MY", "NAME IS", "ABC"));
  }

  @Test
  void stripString() {
    var s = " Michale ";
    String stripResult = s.strip();

    assertEquals("Michale", stripResult);
  }

  @Test
  void stripLeadingString() {
    var s = " Michale ";
    String stripResult = s.stripLeading();

    assertEquals("Michale ", stripResult);
  }

  @Test
  void stripTrailingString() {
    var s = " Michale ";
    String stripResult = s.stripTrailing();

    assertEquals(" Michale", stripResult);
  }

  @Test
  void stringIsBlank() {
    var str = "";

    assertTrue(str.isBlank());
  }
}
