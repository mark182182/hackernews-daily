package hackernews.mailsender.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestBuilder {

  public static final Logger LOG = LoggerFactory.getLogger(RequestBuilder.class);

  private final Builder builder = HttpRequest.newBuilder();

  private String uri;

  public RequestBuilder() {
    // no-op
  }

  public RequestBuilder setUri(final String uri) {
    this.uri = uri;
    return this;
  }

  public HttpRequest build() throws URISyntaxException {
    return this.builder.uri(new URI(this.uri)).build();
  }
}
