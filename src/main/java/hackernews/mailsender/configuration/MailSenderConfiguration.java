package hackernews.mailsender.configuration;

import java.util.Optional;
import java.util.Set;

public final class MailSenderConfiguration implements EnvVarConfiguration {

  // Service
  public static Optional<String> POST_LENGTH = Optional.empty();
  @NotLoggable
  public static Optional<String> MAIL_RECEIVERS = Optional.empty();

  // SMTP
  public static Optional<String> SMTP_HOST = Optional.empty();
  public static Optional<String> SMTP_PORT = Optional.empty();
  @NotLoggable
  public static Optional<String> SMTP_USER = Optional.empty();
  @NotLoggable
  public static Optional<String> SMTP_PASSWORD = Optional.empty();
}
