import static java.util.function.Predicate.not;

import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class NegatePredicate {

  @Test
  void negatePredicate() {
    Stream.of("a", "b", "c", "")
        .filter(not(String::isBlank))
        .forEach(System.out::println);
  }

  @Test
  void negatePredicateBeforeJava11() {
    Stream.of("a", "b", "c", "")
        .filter(((Predicate<String>) String::isBlank).negate())
        .forEach(System.out::println);
  }
}
