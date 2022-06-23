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