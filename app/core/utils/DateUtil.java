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

import static java.time.format.DateTimeFormatter.ofPattern;

import com.google.common.base.Strings;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitária para ações relacionadas a entidades de data.
 */
public final class DateUtil {
  
  private DateUtil() {
  }
  
  public static final String DEFAULT_DATE_TIME_PATTERN    = "yyyy-MM-dd HH:mm:ss";
  public static final String DEFAULT_DATE_TIME_PATTERN_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  public static final String DEFAULT_DATE_PATTERN         = "yyyy-MM-dd";
  public static final String PATTERN_EEE_DD_MM            = "EEE - dd/MM";
  public static final String HOUR_PATTERN                 = "HH:mm:ss";
  public static final String MINIFIED_HOUR_PATTERN        = "HH";
  public static final String PATTERN_EEEEE                = "EEEEE";
  public static final String DEFAULT_TIME_PATTERN         = "HH:mm";
  
  /**
   * Parse value to {@link LocalDateTime}.
   *
   * @param value   Value to be parsed.
   * @param pattern Date time pattern.
   *
   * @return Date time.
   */
  public static LocalDateTime parse(String value, String pattern) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    
    if (pattern == null || pattern.isEmpty()) {
      pattern = DEFAULT_DATE_TIME_PATTERN;
    }
    
    return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern));
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
    
    return LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern));
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
    
    return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
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
    
    return LocalTime.parse(time, DateTimeFormatter.ofPattern(pattern));
  }
  
  /**
   * Format {@link LocalDateTime} as text.
   *
   * @param dateTime Date time.
   *
   * @return Formatted date with default pattern (i.e. `YYYY-MM-dd`)
   */
  public static String format(LocalDateTime dateTime) {
    return format(dateTime, DEFAULT_DATE_PATTERN);
  }
  
  /**
   * Format {@link LocalDateTime} as text.
   *
   * @param dateTime Date time.
   * @param pattern  Pattern.
   *
   * @return Formatted date with pattern.
   */
  public static String format(LocalDateTime dateTime, String pattern) {
    if (dateTime == null) {
      return null;
    }
    
    if (pattern == null || pattern.isEmpty()) {
      pattern = DEFAULT_DATE_TIME_PATTERN;
    }
    
    return dateTime.format(ofPattern(pattern));
  }
  
  public static String format(LocalTime time) {
    return format(time, HOUR_PATTERN);
  }
  
  /**
   * Format {@link LocalDate} to String.
   *
   * @param date Date.
   *
   * @return Formatted date with pattern.
   */
  public static String format(LocalDate date) {
    return format(date, DEFAULT_DATE_PATTERN);
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
      pattern = DEFAULT_DATE_PATTERN;
    }
    
    return date.format(ofPattern(pattern));
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
      pattern = HOUR_PATTERN;
    }
    
    return time.format(ofPattern(pattern));
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