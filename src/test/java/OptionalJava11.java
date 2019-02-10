import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class OptionalJava11 {

  @Test
  void optionalIsEmpty() {
    var optional = Optional.empty();

    assertTrue(optional.isEmpty());
  }

}
