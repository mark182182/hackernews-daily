package hackernews.mailsender.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hackernews.mailsender.configuration.MailSenderConfiguration;
import hackernews.mailsender.mail.EmailComposer;
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

    try {
      MimeMessage mail = new EmailComposer(this.mailHandler.getSession())
          .setFrom(MailSenderConfiguration.SMTP_USER.get()).setReceivers(MailSenderConfiguration.MAIL_RECEIVERS.get())
          .setSubject("test").setBody("test1234").compose();
      
      this.mailHandler.send(mail);

    } catch (MessagingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
