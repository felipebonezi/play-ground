package core.utils;

import com.typesafe.config.Config;
import play.mvc.Call;

/** Application utilities methods. */
public final class AppUtil {
  
  private AppUtil() {
  }
  
  private static boolean devMode;
  private static boolean stageMode;
  private static boolean prodMode;
  private static boolean copyProdMode;
  private static String  appHost;
  
  /**
   * Initialize application infos.
   *
   * @param config Play configuration.
   */
  public static void init(Config config) {
    appHost = config.getString("play.app.host");
    
    String environment = config.getString("play.app.environment");
    devMode      = environment.equalsIgnoreCase("development");
    stageMode    = environment.equalsIgnoreCase("staging");
    prodMode     = environment.equalsIgnoreCase("production");
    copyProdMode = environment.equalsIgnoreCase("copy-production");
  }
  
  public static String redirectWithHost(Call route) {
    return redirectWithHost(route.url());
  }
  
  public static String redirectWithHost(String route) {
    return String.format("%s%s", appHost, route);
  }
  
  public static boolean isDevMode() {
    return devMode;
  }
  
  public static boolean isStagMode() {
    return stageMode;
  }
  
  public static boolean isProdMode() {
    return prodMode;
  }
  
  public static boolean isCopyProdMode() {
    return copyProdMode;
  }
  
}
