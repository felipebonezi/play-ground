package core.utils;

import com.google.common.base.Strings;
import java.text.Normalizer;

/** String helper class. */
public final class StringUtil {
  
  private StringUtil() {
  }
  
  public static final String EMPTY = "";
  public static final String COMMA = ",";
  public static final String SPACE = " ";
  public static final String DASH  = "-";
  
  /**
   * Transform string to numbers.
   *
   * @param text Text to transform.
   *
   * @return Only numbers.
   */
  public static String toOnlyNumbers(String text) {
    if (Strings.isNullOrEmpty(text)) {
      return text;
    }
    
    return text.replaceAll("\\D", "");
  }
  
  /**
   * Return string to ASCII char table.
   *
   * @param text Text.
   *
   * @return Text as ASCII.
   */
  public static String toNormalized(String text) {
    if (Strings.isNullOrEmpty(text)) {
      return "";
    }
    
    return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
  }
  
}
