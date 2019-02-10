import static java.net.http.HttpClient.Version.HTTP_2;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HttpClientTest {

  private HttpClient client;

  @BeforeEach
  void setup() {
    client = HttpClient.newBuilder()
        // just to show off; HTTP/2 is the default
        .version(HTTP_2)
        .connectTimeout(Duration.ofSeconds(5))
        .followRedirects(Redirect.ALWAYS)
        .build();
  }

  @Test
  void configuringHttpRequest() {
    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create("http://codefx.org"))
        .header("Accept-Language", "en-US,en;q=0.5")
        .build();
  }

  @Test
  void httpRequestWithBody() {
    BodyPublisher requestBody = BodyPublishers
        .ofString("{ request body }");
    /*
      BodyPublishers – depending in what form your body comes, you can call these (and a few more) static methods on it:

      ofByteArray(byte[])
      ofFile(Path)
      ofString(String)
      ofInputStream(Supplier<InputStream>)
     */
    HttpRequest request = HttpRequest.newBuilder()
        .POST(requestBody)
        .uri(URI.create("http://codefx.org"))
        .build();
  }

  @Test
  void receivingHttpResponse() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create("http://codefx.org"))
        .header("Accept-Language", "en-US,en;q=0.5")
        .build();

    HttpResponse<String> response = client.send(
        request,
        BodyHandlers.ofString());

    assertEquals(200, response.statusCode());
  }

  @Test
  void synchronousHttpRequestHandling() {
    String searchTerm = "Foo";
    HttpClient httpClient = HttpClient.newBuilder().build();
    List<URI> URLS = Stream.of(
        "https://en.wikipedia.org/wiki/List_of_compositions_by_Franz_Schubert",
        "https://en.wikipedia.org/wiki/2018_in_American_television",
        "https://en.wikipedia.org/wiki/List_of_compositions_by_Johann_Sebastian_Bach",
        "https://en.wikipedia.org/wiki/List_of_Australian_treaties",
        "https://en.wikipedia.org/wiki/2016%E2%80%9317_Coupe_de_France_Preliminary_Rounds",
        "https://en.wikipedia.org/wiki/Timeline_of_the_war_in_Donbass_(April%E2%80%93June_2018)",
        "https://en.wikipedia.org/wiki/List_of_giant_squid_specimens_and_sightings",
        "https://en.wikipedia.org/wiki/List_of_members_of_the_Lok_Sabha_(1952%E2%80%93present)",
        "https://en.wikipedia.org/wiki/1919_New_Year_Honours",
        "https://en.wikipedia.org/wiki/List_of_International_Organization_for_Standardization_standards")
        .map(URI::create)
        .collect(toList());

    URLS.forEach(url -> {
      boolean found = blockingSearch(httpClient, url, searchTerm);
      System.out.println("Completed " + url + " / found: " + found);
    });
  }

  @Test
  void asyncHttpRequestHandling() {
    String searchTerm = "Foo";
    List<URI> URLS = Stream.of(
        "https://en.wikipedia.org/wiki/List_of_compositions_by_Franz_Schubert",
        "https://en.wikipedia.org/wiki/2018_in_American_television",
        "https://en.wikipedia.org/wiki/List_of_compositions_by_Johann_Sebastian_Bach",
        "https://en.wikipedia.org/wiki/List_of_Australian_treaties",
        "https://en.wikipedia.org/wiki/2016%E2%80%9317_Coupe_de_France_Preliminary_Rounds",
        "https://en.wikipedia.org/wiki/Timeline_of_the_war_in_Donbass_(April%E2%80%93June_2018)",
        "https://en.wikipedia.org/wiki/List_of_giant_squid_specimens_and_sightings",
        "https://en.wikipedia.org/wiki/List_of_members_of_the_Lok_Sabha_(1952%E2%80%93present)",
        "https://en.wikipedia.org/wiki/1919_New_Year_Honours",
        "https://en.wikipedia.org/wiki/List_of_International_Organization_for_Standardization_standards")
        .map(URI::create)
        .collect(toList());
    HttpClient httpClient = HttpClient.newBuilder().build();

    CompletableFuture[] futures = URLS.stream()
        .map(url -> asyncSearch(httpClient, url, searchTerm))
        .toArray(CompletableFuture[]::new);
    CompletableFuture.allOf(futures).join();
  }

  static boolean blockingSearch(HttpClient client, URI url, String term) {
    try {
      HttpRequest request = HttpRequest.newBuilder(url).GET().build();
      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      return response.body().contains(term);
    } catch (InterruptedException | IOException e) {
      return false;
    }
  }

  static CompletableFuture<Void> asyncSearch(HttpClient client, URI url, String term) {
    HttpRequest request = HttpRequest.newBuilder(url).GET().build();
    return client
        .sendAsync(request, BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenApply(body -> body.contains(term))
        .exceptionally(__ -> false)
        .thenAccept(found -> System.out.println("Completed " + url + " / found: " + found));
  }


  @Test
  void test1() throws ExecutionException, InterruptedException, IOException {
    final HttpClient hc = HttpClient.newBuilder()
        .version(HTTP_2)
        .build();

    final HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create("https://metebalci.com"))
        .build();

    final HttpResponse<String> resSync = hc.send(req, BodyHandlers.ofString());

    System.out.println(resSync.statusCode());

    hc.sendAsync(req, BodyHandlers.ofString())
        .thenApply(HttpResponse::statusCode)
        .thenAccept(System.out::println)
        .get();
  }

  @Test
  void shouldSynchronousGet() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://github.com/olszewskimichal"))
        .GET()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertEquals(200, response.statusCode());
  }

  @Test
  void ShouldAsynchronousGet() throws Exception {

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI("https://github.com/olszewskimichal"))
        .GET()
        .build();
    CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,
        HttpResponse.BodyHandlers.ofString());

    HttpResponse<String> actualResponse = response.get(10, TimeUnit.SECONDS);
    assertEquals(200, actualResponse.statusCode());

  }


  /*@Test
  public void shouldReturn404OnWrongRequest() throws Exception {
    HttpClient client = HttpClient.newBuilder().build();
    HttpRequest request = HttpRequest
        .newBuilder(URI.create("http://localhost:8080/signup"))
        .GET()
        .build();
    HttpResponse<Stream<String>> response =
        client.send(request, HttpResponse.BodyHandlers.ofLines());
    assertEquals(response.statusCode(), 404);
  }

  @Test
  public void shouldReturn200WhenSendingCorrectEmail() throws Exception {
    var f = File.createTempFile("testing_data", "temp");
    HttpClient client = HttpClient.newBuilder().build();
    HttpRequest request = HttpRequest.
        newBuilder(URI.create("http://localhost:8080/signup"))
        .POST(HttpRequest.BodyPublishers.ofString("email=correct@email.com"))
        .header("Content-Type", "application/x-www-form-urlencoded")
        .build();
    HttpResponse<Stream<String>> response =
        client.send(request, HttpResponse.BodyHandlers.ofLines());
    assertEquals(response.statusCode(), 200);

    FileReader fr = new FileReader(new File(f.getPath()));
    try (BufferedReader reader = new BufferedReader(fr)) {
      assertEquals(reader.readLine(), "correct@email.com");
    }
  }*/
}

