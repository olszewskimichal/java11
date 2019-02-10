import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class TimeUnitConvert {

  @Test
  void convertDayToHour() {
    TimeUnit day = TimeUnit.DAYS;

    assertEquals(2L, day.convert(Duration.ofHours(50)));
    assertEquals(1L, day.convert(Duration.ofHours(24)));
  }

}
