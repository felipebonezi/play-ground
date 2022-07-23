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

package core.injections;

import static com.google.common.base.Strings.isNullOrEmpty;
import static core.utils.DateUtil.format;
import static java.lang.Integer.parseInt;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import play.data.format.Formatters;
import play.data.format.Formatters.SimpleFormatter;
import play.i18n.MessagesApi;

/** Default date formatter provider using `YYYY-MM-dd` as pattern. */
@Singleton
public class DateFormattersProvider implements Provider<Formatters> {
  
  private static final String ERROR_INVALID_DATE = "Invalid Date";
  
  private final MessagesApi messagesApi;
  
  @Inject
  public DateFormattersProvider(MessagesApi messagesApi) {
    this.messagesApi = messagesApi;
  }
  
  @Override
  public Formatters get() {
    Formatters formatters = new Formatters(this.messagesApi);
    registerLocalDateTime(formatters);
    registerLocalDate(formatters);
    registerLocalTime(formatters);
    return formatters;
  }
  
  private void registerLocalDateTime(Formatters formatters) {
    formatters.register(LocalDateTime.class, new SimpleFormatter<>() {
      
      private final Pattern pattern =
          Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T?(\\d{2}):(\\d{2}):?(\\d{2})?");
      
      @Override
      public LocalDateTime parse(String text, Locale locale) throws ParseException {
        Matcher matcher = this.pattern.matcher(text);
        if (!matcher.find()) {
          throw new ParseException("Invalid Time", 0);
        }
        
        int year   = parseInt(matcher.group(1));
        int month  = parseInt(matcher.group(2));
        int day    = parseInt(matcher.group(3));
        int hour   = parseInt(matcher.group(4));
        int minute = parseInt(matcher.group(5));
        
        String group3 = matcher.group(6);
        int    second = isNullOrEmpty(group3) ? 0 : parseInt(group3);
        
        return LocalDateTime.of(year, month, day, hour, minute, second);
      }
      
      @Override
      public String print(LocalDateTime localDateTime, Locale locale) {
        return format(localDateTime);
      }
    });
  }
  
  private void registerLocalDate(Formatters formatters) {
    formatters.register(LocalDate.class, new SimpleFormatter<>() {
      
      private final Pattern pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
      
      @Override
      public LocalDate parse(String text, Locale l) throws ParseException {
        try {
          Matcher matcher = this.pattern.matcher(text);
          if (!matcher.find()) {
            throw new ParseException(ERROR_INVALID_DATE, 0);
          }
          int year  = parseInt(matcher.group(1));
          int month = parseInt(matcher.group(2));
          int day   = parseInt(matcher.group(3));
          return LocalDate.of(year, month, day);
        } catch (Exception e) {
          throw new ParseException("unknonw error: " + e.getMessage(), 0);
        }
        
      }
      
      @Override
      public String print(LocalDate localDate, Locale l) {
        return format(localDate);
      }
      
    });
  }
  
  private void registerLocalTime(Formatters formatters) {
    formatters.register(LocalTime.class, new SimpleFormatter<>() {
      
      private final Pattern pattern = Pattern.compile("(\\d{2}):(\\d{2}):?(\\d{2})?");
      
      @Override
      public LocalTime parse(String text, Locale l) throws ParseException {
        Matcher matcher = this.pattern.matcher(text);
        if (!matcher.find()) {
          throw new ParseException("Invalid Time", 0);
        }
        int hour   = parseInt(matcher.group(1));
        int minute = parseInt(matcher.group(2));
        
        String group3 = matcher.group(3);
        int    second = isNullOrEmpty(group3) ? 0 : parseInt(group3);
        
        return LocalTime.of(hour, minute, second);
      }
      
      @Override
      public String print(LocalTime localTime, Locale l) {
        return format(localTime);
      }
      
    });
  }
  
}