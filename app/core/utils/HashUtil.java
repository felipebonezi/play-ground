package core.utils;

import org.mindrot.jbcrypt.BCrypt;

public final class HashUtil {
  
  private HashUtil() {
  }
  
  public static String createPassword(String clearString) {
    return BCrypt.hashpw(clearString, BCrypt.gensalt());
  }
  
  public static boolean checkPassword(String candidate, String encryptedPassword) {
    return candidate != null && encryptedPassword != null && BCrypt.checkpw(candidate, encryptedPassword);
  }
  
}
