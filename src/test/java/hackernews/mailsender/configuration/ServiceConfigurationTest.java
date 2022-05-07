package hackernews.mailsender.configuration;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ServiceConfigurationTest {

  public static final String POST_LENGTH = "50";
  public static final String SMTP_HOST = "foo";
  public static final String SMTP_PORT = "bar";
  public static final String SMTP_USER = "baz";
  public static final String SMTP_PASSWORD = "foo_bar";
  public static final String MAIL_RECEIVERS = "test";

  @Before
  public void setup() {
    Map<String, String> mockEnvVars = new HashMap<>();
    mockEnvVars.put("POST_LENGTH", POST_LENGTH);
    mockEnvVars.put("SMTP_HOST", SMTP_HOST);
    mockEnvVars.put("SMTP_PORT", SMTP_PORT);
    mockEnvVars.put("SMTP_USER", SMTP_USER);
    mockEnvVars.put("SMTP_PASSWORD", SMTP_PASSWORD);
    mockEnvVars.put("MAIL_RECEIVERS", MAIL_RECEIVERS);
    
    ServiceConfiguration.envVars = mockEnvVars;
  }

  @Test
  public void should_resolve_for_mail_sender() throws Exception {
    ServiceConfiguration.resolveFor(MailSenderConfiguration.class);

    assertEquals(POST_LENGTH, MailSenderConfiguration.POST_LENGTH.get());
    assertEquals(SMTP_HOST, MailSenderConfiguration.SMTP_HOST.get());
    assertEquals(SMTP_PORT, MailSenderConfiguration.SMTP_PORT.get());
    assertEquals(SMTP_USER, MailSenderConfiguration.SMTP_USER.get());
    assertEquals(SMTP_PASSWORD, MailSenderConfiguration.SMTP_PASSWORD.get());
    assertEquals(MAIL_RECEIVERS, MailSenderConfiguration.MAIL_RECEIVERS.get());

  }

}
