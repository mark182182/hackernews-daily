package hackernews.mailsender.util;

public class StringUtil {
  public static boolean isEmpty(final String str) {
    if (null == str) {
      return true;
    }
    if (str.isBlank() || str.isBlank()) {
      return true;
    }
    return false;
  }
}
