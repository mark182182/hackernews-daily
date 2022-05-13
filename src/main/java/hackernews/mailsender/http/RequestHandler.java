package hackernews.mailsender.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hackernews.mailsender.configuration.MailSenderConfiguration;
import hackernews.mailsender.util.Constants;

public class RequestHandler {

  public static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);

  private final HttpClient client = HttpClient.newHttpClient();

  public RequestHandler() {
    // no-op
  }

  /**
   * Sends the given request synchronously
   * 
   * @param HttpRequest request
   * @return HttpResponse<String> response
   * @throws InterruptedException
   * @throws IOException
   */
  private HttpResponse<String> send(final HttpRequest request) throws IOException, InterruptedException {
    return this.client.send(request, BodyHandlers.ofString());
  }

  /**
   * Sends the given request asynchronously
   * 
   * @param HttpRequest request
   * @return CompletableFuture<HttpResponse<String>> response
   * 
   */
  private CompletableFuture<HttpResponse<String>> sendAsync(final HttpRequest request) {
    return this.client.sendAsync(request, BodyHandlers.ofString());
  }

  /**
   * Send a single request to the given URI
   * 
   * @param String path
   * @return String responseBody
   */
  public String send(final String path) throws IOException, InterruptedException, URISyntaxException {
    RequestBuilder builder = new RequestBuilder();
    HttpRequest request = builder.setUri(Constants.HN_BASE_URI.concat(path)).build();
    HttpResponse<String> response = send(request);
    return response.body();
  }

  /**
   * Send multiple requests asynchronously
   * 
   * @param Set<String> paths
   * @return Set<String> responseBodies
   * @throws URISyntaxException
   * 
   */
  public Set<HttpResponse<String>> sendAsync(final Set<String> paths) throws URISyntaxException {
    final ExecutorService executor = Executors
        .newFixedThreadPool(Integer.valueOf(MailSenderConfiguration.REQUEST_HANDLER_THREADS).intValue());

    RequestBuilder builder = new RequestBuilder();
    Set<HttpResponse<String>> responses = new HashSet<>();

    for (String path : paths) {
      HttpRequest request = builder.setUri(Constants.HN_BASE_URI.concat(path)).build();
      CompletableFuture<HttpResponse<String>> future = sendAsync(request);
      executor.execute(() -> {
        try {
          HttpResponse<String> response = future.get();
          responses.add(response);
        } catch (InterruptedException | ExecutionException e) {
          LOG.warn("Failed to get request: {}", e.getMessage(), e);
        }
      });
    }

    executor.shutdown();
    return responses;
  }

}
