package hackernews.mailsender.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Webserver extends Service {

  public static final Logger LOG = LoggerFactory.getLogger(Webserver.class);

  public static final CountDownLatch latch = new CountDownLatch(1);

  private final int port;

  private ExecutorService executor;

  public Webserver(int port) {
    this.port = port;
  }

  @Override
  public void init() {
    // no-op
  }

  @Override
  public void run() {
    this.executor = Executors.newSingleThreadExecutor();
    this.executor.execute(() -> {
      try (final ServerSocket socket = new ServerSocket(this.port)) {
        try {
          Socket connection = null;
          while (Webserver.latch.getCount() == 1) {
            connection = socket.accept();
            if (!socket.isClosed()) {
              OutputStream os = new BufferedOutputStream(connection.getOutputStream());
              PrintStream ps = new PrintStream(os);
              ps.print("foo");
              ps.close();
            }
          }
          if (null != connection && !connection.isClosed()) {
            connection.close();
          }
        } catch (IOException e) {
          LOG.error(e.getMessage(), e);
        }
      } catch (IOException e) {
        LOG.error("Failed to start webserver: {}", e.getMessage(), e);
      }
    });
  }

  @Override
  public void stop() {
    this.executor.shutdown();
  }

}
