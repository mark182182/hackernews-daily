package hackernews.mailsender.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailComposer {

  private final Session session;

  private String receiver;
  private String subject;
  private String body;

  public EmailComposer(final Session session) {
    this.session = session;
  }

  public EmailComposer setReceiver(final String receiver) {
    this.receiver = receiver;
    return this;
  }

  public EmailComposer setSubject(final String subject) {
    this.subject = subject;
    return this;
  }

  public EmailComposer setBody(final String body) {
    this.body = body;
    return this;
  }

  public MimeMessage compose() throws AddressException, MessagingException {
    MimeMessage message = new MimeMessage(this.session);
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.receiver));
    message.setSubject(this.subject);
    message.setContent(this.body, "text/html");
    return message;
  }

}
