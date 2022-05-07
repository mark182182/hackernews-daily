package hackernews.mailsender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hackernews.mailsender.configuration.MailSenderConfiguration;
import hackernews.mailsender.configuration.ServiceConfiguration;
import hackernews.mailsender.service.MailSender;

public class MailSenderService {

  public static Logger LOG = LoggerFactory.getLogger(MailSenderService.class);

  public static void main(String[] args) {
    if (args.length > 0) {
      System.out.println(args[0]);
    } else {
      LOG.info("No args given, using default config.");

      ServiceConfiguration.resolveFor(MailSenderConfiguration.class);

      MailSender mailSender = new MailSender();
      mailSender.run();
    }

  }

}
