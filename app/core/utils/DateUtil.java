package core.utils;

import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Classe utilitária para ações relacionadas a entidades de data.
 */
public final class DateUtil {
  
  private DateUtil() {
  }
  
  /**
   * Formato padrão de armazenamento de data no banco de dados.
   */
  public static final String DEFAULT_DATE_TIME_PATTERN    = "yyyy-MM-dd HH:mm:ss";
  public static final String DEFAULT_DATE_TIME_PATTERN_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  
  /**
   * Formato padrão de armazenamento de data no banco de dados.
   */
  public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
  
  /**
   * Formato padrão de exibição e recuperação de data nas views.
   */
  public static final String DEFAULT_VIEW_DATE_PATTERN = "dd/MM/yyyy";
  
  /**
   * Formato padrão de exibição para utilizasa no JavaScript.
   */
  public static final String DEFAULT_VIEW_DATE_JS_PATTERN = "dd/mm/yyyy";
  
  /**
   * Padrão de data brasileira.
   */
  public static final String DATE_PATTERN_BR = "dd/MM/yyyy";
  
  public static final String DATE_PATTERN_BR_SHORT = "dd/MM/yy";
  
  /**
   * Padrão customizado da data brasileira.
   */
  public static final String PATTERN_EEE_DD_MM = "EEE - dd/MM";
  
  /**
   * Padrão de hora brasileira.
   */
  public static final String HOUR_PATTERN_BR          = "HH:mm:ss";
  public static final String MINIFIED_HOUR_PATTERN_BR = "HH";
  
  /**
   * Padrão de data/hora brasileira.
   */
  public static final String DATE_HOUR_PATTERN_BR = "dd/MM/yyyy HH:mm:ss";
  
  /**
   * Padrão de data/hora brasileira, sem os segundos.
   */
  public static final String DATE_HOUR_WITHOUT_SECONDS_PATTERN_BR       = "dd/MM/yyyy HH:mm";
  public static final String DATE_HOUR_WITHOUT_SECONDS_PATTERN_BR_SHORT = "dd/MM/yy HH";
  public static final String DATE_HOUR_SHORT_WITHOUT_SECONDS_PATTERN_BR = "dd/MM/yy HH:mm";
  public static final String PATTERN_EEEEE                              = "EEEEE";
  public static final String DEFAULT_TIME_PATTERN                       = "HH:mm";
  
  /**
   * Parse value to {@link DateTime}.
   *
   * @param value   Value to be parsed.
   * @param pattern Date time pattern.
   *
   * @return Date time.
   */
  public static DateTime parse(String value, String pattern) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    
    if (pattern == null || pattern.isEmpty()) {
      pattern = DEFAULT_DATE_TIME_PATTERN;
    }
    
    return DateTime.parse(value, DateTimeFormat.forPattern(pattern));
  }
  
  /**
   * Parse value to {@link LocalDate}.
   *
   * @param value   Value to be parsed.
   * @param pattern Date pattern.
   *
   * @return Date time.
   */
  public static LocalDate parseDate(String value, String pattern) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    
    if (pattern == null || pattern.isEmpty()) {
      pattern = DEFAULT_DATE_PATTERN;
    }
    
    return LocalDate.parse(value, DateTimeFormat.forPattern(pattern));
  }
  
  /**
   * Parse a String to {@link LocalDate}.
   *
   * @param date Date as String.
   *
   * @return Date.
   */
  public static LocalDate parseLocalDate(String date, String pattern) {
    if (date == null || date.isEmpty()) {
      return null;
    }
    
    if (pattern == null || pattern.isEmpty()) {
      pattern = DEFAULT_DATE_TIME_PATTERN;
    }
    
    return LocalDate.parse(date, DateTimeFormat.forPattern(pattern));
  }
  
  /**
   * Parse String as {@link LocalTime}.
   *
   * @param time    Time.
   * @param pattern Pattern.
   *
   * @return Formatted time with pattern.
   */
  public static LocalTime parseLocalTime(String time, String pattern) {
    if (Strings.isNullOrEmpty(time)) {
      return null;
    }
    
    if (pattern == null || pattern.isEmpty()) {
      pattern = DEFAULT_TIME_PATTERN;
    }
    
    return LocalTime.parse(time, DateTimeFormat.forPattern(pattern));
  }
  
  /**
   * Format {@link DateTime} as text.
   *
   * @param dateTime Date time.
   *
   * @return Formatted date with default pattern (i.e. `YYYY-MM-dd`)
   */
  public static String format(DateTime dateTime) {
    return format(dateTime, DEFAULT_DATE_PATTERN);
  }
  
  /**
   * Format {@link DateTime} as text.
   *
   * @param dateTime Date time.
   * @param pattern  Pattern.
   *
   * @return Formatted date with pattern.
   */
  public static String format(DateTime dateTime, String pattern) {
    if (dateTime == null) {
      return null;
    }
    
    if (pattern == null || pattern.isEmpty()) {
      pattern = DATE_HOUR_PATTERN_BR;
    }
    
    return dateTime.toString(pattern);
  }
  
  public static String format(LocalTime time) {
    return format(time, HOUR_PATTERN_BR);
  }
  
  /**
   * Format {@link LocalDate} to String.
   *
   * @param date    Date.
   * @param pattern Pattern.
   *
   * @return Formatted date with pattern.
   */
  public static String format(LocalDate date, String pattern) {
    if (date == null) {
      return null;
    }
    
    if (pattern == null || pattern.isEmpty()) {
      pattern = DATE_PATTERN_BR;
    }
    
    return date.toString(pattern);
  }
  
  /**
   * Format {@link LocalTime} as String.
   *
   * @param time    Time.
   * @param pattern Pattern.
   *
   * @return Formatted time.
   */
  public static String format(LocalTime time, String pattern) {
    if (time == null) {
      return null;
    }
    
    if (pattern == null || pattern.isEmpty()) {
      pattern = HOUR_PATTERN_BR;
    }
    
    return time.toString(pattern);
  }
  
  /**
   * Return if a candidate String is {@link LocalDate}.
   *
   * @param candidate Candidate value.
   * @param pattern   Pattern.
   *
   * @return True if is a valid date, false otherwise.
   */
  public static boolean isDate(String candidate, String pattern) {
    try {
      return parseDate(candidate, pattern) != null;
    } catch (Exception ignored) {
      // Do nothing.
    }
    return false;
  }
  
}