import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class PathJava11 {

  @Test
  void pathOfMethodExample() {
    Path studentFilePath = Path.of("/home/Students/student.txt");

    System.out.println(studentFilePath);
  }

}
