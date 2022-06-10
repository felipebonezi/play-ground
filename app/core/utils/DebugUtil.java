package core.utils;

import play.Logger;

/**
 * Classe auxiliar para Debug em log do sistema.
 */
public final class DebugUtil {
  
  private DebugUtil() {
    // Do nothing.
  }
  
  /**
   * Tag default do log.
   */
  private static final String DEFAULT_TAG = "CORE";
  
  /**
   * Padrão default para logs complexos.
   */
  private static final String DEBUG_START = ">>>>>> DEBUG START <<<<<<";
  
  /**
   * Padrão default para logs complexos.
   */
  private static final String DEBUG_END = ">>>>>> DEBUG END <<<<<<";
  
  /**
   * Log de modo DEBUG do sistema.
   *
   * @param tag      - Tag
   * @param messages - messages em Array
   */
  public static void d(String tag, String... messages) {
    d(tag, DEBUG_START);
    if (messages != null && messages.length > 0) {
      for (String message : messages) {
        d(tag, message);
      }
    }
    d(tag, DEBUG_END);
  }
  
  /**
   * Log de modo DEBUG do sistema.
   *
   * @param message - message a ser salva.
   */
  public static void d(String message) {
    d(DEFAULT_TAG, message);
  }
  
  /**
   * Log de modo DEBUG do sistema.
   *
   * @param tag     - Tag
   * @param message - message a ser salva.
   */
  public static void d(String tag, String message) {
    if (AppUtil.isProdMode()) {
      return;
    }
    
    Logger.of(tag).debug(message);
  }
  
  /**
   * Log de modo WARNING do sistema.
   *
   * @param message - message a ser salva.
   */
  public static void w(String message) {
    w(DEFAULT_TAG, message);
  }
  
  /**
   * Log de modo WARNING do sistema.
   *
   * @param tag     - Tag
   * @param message - message a ser salva.
   */
  public static void w(String tag, String message) {
    Logger.of(tag).warn(message);
  }
  
  /**
   * Log de modo INFORMATION do sistema.
   *
   * @param message - message a ser salva.
   */
  public static void i(String message) {
    i(DEFAULT_TAG, message);
  }
  
  /**
   * Log de modo INFORMATION do sistema.
   *
   * @param tag     - Tag
   * @param message - message a ser salva.
   */
  public static void i(String tag, String message) {
    Logger.of(tag).info(message);
  }
  
  /**
   * Log de modo MESSAGE do sistema.
   *
   * @param message - message a ser salva.
   */
  public static void e(String message) {
    e(DEFAULT_TAG, message);
  }
  
  /**
   * Log de modo MESSAGE do sistema.
   *
   * @param tag     - Tag
   * @param message - message a ser salva.
   */
  public static void e(String tag, String message) {
    Logger.of(tag).error(message);
  }
  
  /**
   * Log de modo MESSAGE do sistema.
   *
   * @param exception - Exceção a ser salva.
   */
  public static void e(Exception exception) {
    e(DEFAULT_TAG, exception);
  }
  
  /**
   * Log de modo MESSAGE do sistema.
   *
   * @param tag       - Tag
   * @param exception - Exceção a ser salva.
   */
  public static void e(String tag, Exception exception) {
    Logger.of(tag).error(exception.toString());
  }
  
  /**
   * Log de modo MESSAGE do sistema.
   *
   * @param tag       - Tag
   * @param throwable - Exceção a ser salva.
   */
  public static void e(String tag, Throwable throwable) {
    Logger.of(tag).error(throwable.toString());
  }
  
}