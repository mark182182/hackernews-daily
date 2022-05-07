package hackernews.mailsender.configuration;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hackernews.mailsender.util.Pair;
import hackernews.mailsender.util.StringUtil;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ServiceConfiguration {

  public static final Logger LOG = LoggerFactory.getLogger(ServiceConfiguration.class);

  private static PublishSubject<Pair<String, String>> subject = PublishSubject.create();
  static Map<String, String> envVars = getEnvVars();

  static {
    subject.subscribe(pair -> LOG.info("Resolved field '{}' with value: '{}'", pair.getLeft(), pair.getRight()));
  }

  public static void resolveFor(final Class<?> envVarConfig) {
    if (envVarConfig == null || !EnvVarConfiguration.class.isAssignableFrom(envVarConfig)) {
      throw new IllegalArgumentException("Invalid configuration provided!");
    }
    
    try {
      Field[] configFields = envVarConfig.getDeclaredFields();

      for (Field field : configFields) {
        String fieldName = field.getName();
        String envVarValue = envVars.get(fieldName);

        if (!StringUtil.isEmpty(envVarValue)) {
          field.set(null, Optional.of(envVarValue));
          subject.onNext(new Pair<>(fieldName, field.isAnnotationPresent(NotLoggable.class) ? "****" : envVarValue));
        } else {
          LOG.warn("Couldn't resolve value for field: '{}'!", field.getName());
        }
      }
    } catch (IllegalAccessException e) {
      LOG.error("Failed to resolve configuration: {}", e.getMessage(), e);
    }
  }

  // In order to mock them from tests
  public static Map<String, String> getEnvVars() {
    return System.getenv();
  }

}
