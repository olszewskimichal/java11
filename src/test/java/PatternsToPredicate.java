import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class PatternsToPredicate {

  @Test
  void createPredicateFromPattern() {
    Predicate<String> predicate = Pattern.compile("ah").asPredicate();

    long count = Stream.of("coding", "ahsan", "infinite")
        .filter(predicate)
        .count();

    assertEquals(1, count);
  }

  @Test
  void createMatchPredicateFromPattern() {
    Predicate<String> predicate = Pattern.compile("ahsan").asMatchPredicate();

    long count = Stream.of("coding", "ahsan", "infinite")
        .filter(predicate)
        .count();

    assertEquals(1, count);
  }

}
