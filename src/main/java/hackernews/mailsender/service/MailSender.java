package hackernews.mailsender.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hackernews.mailsender.MailSenderService;
import hackernews.mailsender.configuration.MailSenderConfiguration;
import hackernews.mailsender.configuration.ServiceConfiguration;
import hackernews.mailsender.http.RequestHandler;
import hackernews.mailsender.mail.EmailComposer;
import hackernews.mailsender.mail.MailHandler;

public class MailSender extends Service {

  public static Logger LOG = LoggerFactory.getLogger(MailSender.class);

  private final MailHandler mailHandler = new MailHandler();
  private final RequestHandler requestHandler = new RequestHandler();
  private ExecutorService executor;

  public MailSender() {
    // no-op
  }

  @Override
  public void init() {
    ServiceConfiguration.resolveFor(MailSenderConfiguration.class);
    this.mailHandler.setup();
  }

  @Override
  public void run() {
    this.executor = Executors.newSingleThreadExecutor();
    this.executor.execute(() -> {
      Set<String> items = new HashSet<>();

      try {
        String newStories = this.requestHandler.send("newstories.json");
        JSONArray ids = new JSONArray(newStories);

        Set<String> paths = new HashSet<>();

        for (Object id : ids) {
          String itemId = String.valueOf(id);
          paths.add("item/".concat(itemId).concat(".json"));
        }

        int i = 0;
        for (String path : paths) {
          String item = this.requestHandler.send(path);
          items.add(String.valueOf(item));
          if (i == Integer.parseInt(MailSenderConfiguration.POST_LENGTH.get()) - 1) {
            break;
          }
          i++;
        }
      } catch (IOException | InterruptedException | URISyntaxException e) {
        LOG.error("Failed to send request: {}", e.getMessage(), e);
      }

      StringBuilder builder = new StringBuilder();
      builder.append("<h1>Hackernews newest posts</h1>");
      String style = "style=\"font-family: Consolas; text-decoration: none; color: #0f0f0f;\"";

      for (String item : items) {
        builder.append("<section style=\"border: 2px dotted black; margin: 5px\">");
        JSONObject obj = new JSONObject(item);
        if (obj.has("url")) {
          builder.append("<h2><a href=\"").append(obj.getString("url")).append("\"").append(style).append(">")
              .append(obj.getString("title")).append("</a></h2>");
        } else if (obj.has("text")) {
          builder.append("<section>");
          builder.append("<h2>").append(obj.getString("title")).append("</h2>");
          builder.append("<p>").append(obj.getString("text")).append("</p>");
          builder.append("</section>");
        }
        builder.append("</section>");
      }

      LOG.info("Loaded {} items", String.valueOf(items.size()));

      try {
        String currentDay = String.valueOf(LocalDateTime.now().getDayOfMonth());
        MimeMessage mail = new EmailComposer(this.mailHandler.getSession())
            .setFrom(MailSenderConfiguration.SMTP_USER.get()).setReceivers(MailSenderConfiguration.MAIL_RECEIVERS.get())
            .setSubject("HN_DAILY ".concat(currentDay)).setBody(builder.toString()).compose();

        this.mailHandler.send(mail);

      } catch (MessagingException e) {
        LOG.error("Failed to send email: {}", e.getMessage(), e);
      } finally {
        Webserver.latch.countDown();
        MailSenderService.MAIN_LATCH.countDown();
      }
    });

  }

  @Override
  public void stop() {
    this.executor.shutdown();
  }

}