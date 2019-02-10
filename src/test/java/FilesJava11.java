import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class FilesJava11 {

  @Test
  void readStringExample() throws IOException {
    var studentFileContent = Files.readString(Path.of("student.txt"));

    assertEquals("asdasdas   -  asdasdas  --bbb", studentFileContent);
  }

  @Test
  void writeStringExample() throws IOException {
    Files.writeString(Path.of("student-mod.txt"), "asdasdsaa");

    assertEquals(Files.readString(Path.of("student-mod.txt")), "asdasdsaa");
  }

  @Test
  void isSameFile() throws IOException {
    Path path1 = Path.of("student.txt");
    Path path2 = Path.of("student.txt");

    assertTrue(Files.isSameFile(path1, path2));
  }

}
