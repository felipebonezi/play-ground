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

import play.Logger;

/** Debug helper method. */
public final class DebugUtil {
  
  private DebugUtil() {
    // Do nothing.
  }
  
  /**
   * Tag default do log.
   */
  private static final String DEFAULT_TAG = "CORE";
  
  /**
   * Tag for complex logs (start).
   */
  private static final String DEBUG_START = ">>>>>> DEBUG START <<<<<<";
  
  /**
   * Tag for complex logs (end).
   */
  private static final String DEBUG_END = ">>>>>> DEBUG END <<<<<<";
  
  /**
   * Debug log.
   *
   * @param tag      Tag.
   * @param messages Messages.
   */
  public static void debug(String tag, String... messages) {
    debug(tag, DEBUG_START);
    if (messages != null && messages.length > 0) {
      for (String message : messages) {
        debug(tag, message);
      }
    }
    debug(tag, DEBUG_END);
  }
  
  /**
   * Debug log.
   *
   * @param message Message.
   */
  public static void debug(String message) {
    debug(DEFAULT_TAG, message);
  }
  
  /**
   * Debug log.
   *
   * @param tag     Tag
   * @param message Message.
   */
  public static void debug(String tag, String message) {
    if (AppUtil.isProdMode()) {
      return;
    }
    
    Logger.of(tag).debug(message);
  }
  
  /**
   * Warning log.
   *
   * @param message Message.
   */
  public static void warn(String message) {
    warn(DEFAULT_TAG, message);
  }
  
  /**
   * Warning log.
   *
   * @param tag     Tag
   * @param message Message.
   */
  public static void warn(String tag, String message) {
    Logger.of(tag).warn(message);
  }
  
  /**
   * Info log.
   *
   * @param message Message.
   */
  public static void info(String message) {
    info(DEFAULT_TAG, message);
  }
  
  /**
   * Info log.
   *
   * @param tag     Tag
   * @param message Message.
   */
  public static void info(String tag, String message) {
    Logger.of(tag).info(message);
  }
  
  /**
   * Error log.
   *
   * @param message Message.
   */
  public static void error(String message) {
    error(DEFAULT_TAG, message);
  }
  
  /**
   * Error log.
   *
   * @param tag     Tag
   * @param message Message.
   */
  public static void error(String tag, String message) {
    Logger.of(tag).error(message);
  }
  
  /**
   * Error log.
   *
   * @param exception Exceção a ser salva.
   */
  public static void error(Exception exception) {
    error(DEFAULT_TAG, exception);
  }
  
  /**
   * Error log.
   *
   * @param tag       Tag
   * @param exception Exceção a ser salva.
   */
  public static void error(String tag, Exception exception) {
    Logger.of(tag).error(exception.toString());
  }
  
  /**
   * Error log.
   *
   * @param tag       Tag
   * @param throwable Exceção a ser salva.
   */
  public static void error(String tag, Throwable throwable) {
    Logger.of(tag).error(throwable.toString());
  }
  
}