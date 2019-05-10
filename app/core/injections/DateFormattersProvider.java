package core.injections;

import com.google.common.base.Strings;
import core.utils.DateUtil;
import play.data.format.Formatters;
import play.data.format.Formatters.SimpleFormatter;
import play.i18n.MessagesApi;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class DateFormattersProvider implements Provider<Formatters> {

    private final MessagesApi messagesApi;

    @Inject
    public DateFormattersProvider(MessagesApi messagesApi) {
        this.messagesApi = messagesApi;
    }

    @Override
    public Formatters get() {
        Formatters formatters = new Formatters(this.messagesApi);
        registerJavaTime(formatters);
        registerJodaTime(formatters);
        return formatters;
    }

    private void registerJodaTime(Formatters formatters) {
        formatters.register(org.joda.time.LocalDate.class, new SimpleFormatter<org.joda.time.LocalDate>() {

            private Pattern timePattern = Pattern.compile(
                    "(\\d{4})-(\\d{2})-(\\d{2})?"
            );

            @Override
            public org.joda.time.LocalDate parse(String input, Locale l) throws ParseException {
                try {
                    Matcher m = timePattern.matcher(input);
                    if (!m.find()) throw new ParseException("Invalid Date", 0);
                    int day = Integer.valueOf(m.group(3));
                    int month = Integer.valueOf(m.group(2));
                    int year = Integer.valueOf(m.group(1));
                    return new org.joda.time.LocalDate(year, month, day);
                } catch (Exception e) {
                    throw new ParseException(String.format("Unknonw error: %s", e.getMessage()), 0);
                }

            }

            @Override
            public String print(org.joda.time.LocalDate localDate, Locale l) {
                return DateUtil.format(localDate, DateUtil.DEFAULT_DATE_PATTERN);
            }

        });
        formatters.register(org.joda.time.DateTime.class, new SimpleFormatter<org.joda.time.DateTime>() {

            private Pattern timePattern = Pattern.compile(
                    "(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2})?"
            );

            @Override
            public org.joda.time.DateTime parse(String input, Locale l) throws ParseException {
                try {
                    Matcher m = timePattern.matcher(input);
                    if (!m.find()) throw new ParseException("Invalid Date", 0);
                    int day = Integer.valueOf(m.group(3));
                    int month = Integer.valueOf(m.group(2));
                    int year = Integer.valueOf(m.group(1));
                    int hour = Integer.valueOf(m.group(4));
                    int minutes = Integer.valueOf(m.group(5));
                    return new org.joda.time.DateTime(year, month, day, hour, minutes);
                } catch (Exception e) {
                    throw new ParseException(String.format("Unknonw error: %s", e.getMessage()), 0);
                }

            }

            @Override
            public String print(org.joda.time.DateTime dateTime, Locale l) {
                return DateUtil.format(dateTime, DateUtil.DEFAULT_DATE_TIME_PATTERN);
            }

        });
    }

    private void registerJavaTime(Formatters formatters) {
        formatters.register(LocalDate.class, new SimpleFormatter<LocalDate>() {

            private Pattern timePattern = Pattern.compile(
                    "(\\d{4})-(\\d{2})-(\\d{2})?"
            );

            @Override
            public LocalDate parse(String input, Locale l) throws ParseException {
                try {
                    Matcher m = timePattern.matcher(input);
                    if (!m.find()) throw new ParseException("Invalid Date", 0);
                    int day = Integer.valueOf(m.group(3));
                    int month = Integer.valueOf(m.group(2));
                    int year = Integer.valueOf(m.group(1));
                    return LocalDate.of(year, month, day);
                } catch (Exception e) {
                    throw new ParseException("unknonw error: "+e.getMessage(), 0);
                }

            }

            @Override
            public String print(LocalDate localTime, Locale l) {
                return localTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }

        });
        formatters.register(LocalTime.class, new SimpleFormatter<LocalTime>() {

            private Pattern timePattern = Pattern.compile(
                    "(\\d{2}):(\\d{2})[:]?(\\d{2})?"
            );

            @Override
            public LocalTime parse(String input, Locale l) throws ParseException {
                Matcher m = timePattern.matcher(input);
                if (!m.find()) throw new ParseException("Invalid Time", 0);
                int hour = Integer.valueOf(m.group(1));
                int minute = Integer.valueOf(m.group(2));

                String group3 = m.group(3);
                int second = Strings.isNullOrEmpty(group3) ? 0 : Integer.valueOf(group3);

                return LocalTime.of(hour, minute, second);
            }

            @Override
            public String print(LocalTime localTime, Locale l) {
                return localTime.format(DateTimeFormatter.ofPattern(DateUtil.HOUR_PATTERN_BR));
            }

        });
    }
}