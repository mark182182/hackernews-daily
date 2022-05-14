package hackernews.mailsender;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hackernews.mailsender.service.MailSender;
import hackernews.mailsender.service.Webserver;

public class MailSenderService {

  public static final Logger LOG = LoggerFactory.getLogger(MailSenderService.class);
  public static final CountDownLatch MAIN_LATCH = new CountDownLatch(1);

  public static final MailSender mailSender = new MailSender();
  public static final Webserver ws = new Webserver(80);

  public static void main(String[] args) throws InterruptedException {
    initServices();
    startServices();
    MAIN_LATCH.await();
    tearDown();
  }

  public static void initServices() {
    mailSender.init();
    ws.init();
  }

  public static void startServices() {
    mailSender.run();
    ws.run();
  }

  public static void tearDown() {
    mailSender.stop();
    ws.stop();
  }
}
