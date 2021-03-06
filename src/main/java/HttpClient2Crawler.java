import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClient2Crawler {

  private static Pattern hrefPattern = Pattern.compile("href=[\"'](\\S*)[\"']");
  private static List<String> knownUrls = new LinkedList<>();

  public static void main(String[] args) {
    String host = "https://blog.codinghorror.com";
    readAllUrls(host, "/")
        .forEach(x -> readAllUrls(host, x)
            .forEach(y -> readAllUrls(host, y)));
    for (String path : knownUrls) {
      System.out.println(host + path);
    }
  }

  private static List<String> getAllUrls(String host, String input) {
    LinkedList<String> list = new LinkedList<>();
    Matcher result = hrefPattern.matcher(input);
    while (result.find()) {
      String url = result.group(1);
      if (url.startsWith("/") && !url.startsWith("//")) {
        list.add(url);
      } else if (url.startsWith(host)) {
        list.add(url.replaceFirst(host, ""));
      }
    }
    return list;
  }

  private static List<String> readAllUrls(String host, String path) {
    if (knownUrls.stream().anyMatch(x -> x.equals(path))) {
      return Collections.emptyList();
    }
    System.out.println("Reading " + host + path);
    try {
      HttpClient client = HttpClient.newBuilder().build();
      HttpRequest getRequest = HttpRequest.newBuilder(URI.create(host + path)).GET().build();
      HttpResponse<String> response =
          client.send(getRequest, HttpResponse.BodyHandlers.ofString());
      knownUrls.add(path);
      return getAllUrls(host, response.body());
    } catch (IOException | InterruptedException ex) {
      return Collections.emptyList();
    }
  }
}
