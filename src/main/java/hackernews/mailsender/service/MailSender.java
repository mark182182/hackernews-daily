package hackernews.mailsender.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hackernews.mailsender.mail.MailHandler;

public class MailSender implements Runnable {

  public static Logger LOG = LoggerFactory.getLogger(MailSender.class);

  private final MailHandler mailHandler = new MailHandler();

  public MailSender() {
    this.mailHandler.setup();
  }

  @Override
  public void run() {
    // TODO get posts and send email
  }

}
