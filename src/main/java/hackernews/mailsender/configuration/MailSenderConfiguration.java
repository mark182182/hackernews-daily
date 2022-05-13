package hackernews.mailsender.configuration;

import java.util.Optional;

public final class MailSenderConfiguration implements EnvVarConfiguration {

  // Service
  public static Optional<String> POST_LENGTH = Optional.empty();
  public static String REQUEST_HANDLER_THREADS = "10";
  @NotLoggable
  public static Optional<String> MAIL_RECEIVERS = Optional.empty();

  // SMTP
  public static Optional<String> SMTP_CONNECTION_TIMEOUT = Optional.empty();
  public static Optional<String> SMTP_IO_TIMEOUT = Optional.empty();
  public static Optional<String> SMTP_HOST = Optional.empty();
  public static Optional<String> SMTP_PORT = Optional.empty();
  @NotLoggable
  public static Optional<String> SMTP_USER = Optional.empty();
  @NotLoggable
  public static Optional<String> SMTP_PASSWORD = Optional.empty();
}
