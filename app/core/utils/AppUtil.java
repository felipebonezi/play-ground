/*
 * Copyright 2022 Felipe Bonezi <https://about.me/felipebonezi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
