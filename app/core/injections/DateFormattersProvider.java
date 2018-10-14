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
        formatters.register(LocalDate.class, new SimpleFormatter<LocalDate>() {

            private Pattern timePattern = Pattern.compile(
                    "(\\d{2})/(\\d{2})/(\\d{4})?"
            );

            @Override
            public LocalDate parse(String input, Locale l) throws ParseException {
                Matcher m = timePattern.matcher(input);
                if (!m.find()) throw new ParseException("Invalid Date", 0);
                int day = Integer.valueOf(m.group(1));
                int month = Integer.valueOf(m.group(2));
                int year = Integer.valueOf(m.group(3));
                return LocalDate.of(year, month, day);
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

        return formatters;
    }
}