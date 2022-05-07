package hackernews.mailsender.mail;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hackernews.mailsender.configuration.MailSenderConfiguration;

public class MailHandler {

  public static final Logger LOG = LoggerFactory.getLogger(MailHandler.class);

  private Session session;

  public MailHandler() {
    // no-op
  }

  public void setup() {
    Properties props = System.getProperties();
    props.setProperty("mail.smtp.port", MailSenderConfiguration.SMTP_HOST.get());
    props.setProperty("mail.smtp.auth", "true");
    props.setProperty("mail.smtp.starttls.enable", "true");
    this.session = Session.getInstance(props, null);
  }

  public void send(final MimeMessage message) {
    try (Transport transport = this.session.getTransport("smtp")) {
      transport.connect(MailSenderConfiguration.SMTP_HOST.get(), MailSenderConfiguration.SMTP_USER.get(),
          MailSenderConfiguration.SMTP_PASSWORD.get());
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();
      LOG.info("Email has been sent");
    } catch (MessagingException e) {
      LOG.error("Error while sending mail: {}", e.getMessage(), e);
    }
  }

  public Session getSession() {
    return this.session;
  }
}
