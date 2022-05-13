package hackernews.mailsender.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
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
    Properties props = new Properties();
    props.put("mail.smtp.host", MailSenderConfiguration.SMTP_HOST.get());
    props.put("mail.smtp.port", MailSenderConfiguration.SMTP_PORT.get());
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.connectiontimeout", MailSenderConfiguration.SMTP_CONNECTION_TIMEOUT.get());
    props.put("mail.smtp.timeout", MailSenderConfiguration.SMTP_IO_TIMEOUT.get());

    this.session = Session.getInstance(props, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(MailSenderConfiguration.SMTP_USER.get(),
            MailSenderConfiguration.SMTP_PASSWORD.get());
      }
    });
  }

  public void send(final MimeMessage message) {
    try {
      Transport.send(message, message.getAllRecipients());
      LOG.info("Email has been sent");
    } catch (MessagingException e) {
      LOG.error("Error while sending mail: {}", e.getMessage(), e);
    }
  }

  public Session getSession() {
    return this.session;
  }
}
