package core.utils;

import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;

/**
 * Classe utilitária para ações relacionadas a entidades de data.
 */
public final class DateUtil {

    /**
     * Formato padrão de armazenamento de data no banco de dados.
     */
    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
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
     * Formato padrão de exibição para utilizasa no JavaScript
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
    public static final String HOUR_PATTERN_BR = "HH:mm:ss";
    public static final String MINIFIED_HOUR_PATTERN_BR = "HH";

    /**
     * Padrão de data/hora brasileira.
     */
    public static final String DATE_HOUR_PATTERN_BR = "dd/MM/yyyy HH:mm:ss";

    /**
     * Padrão de data/hora brasileira, sem os segundos.
     */
    public static final String DATE_HOUR_WITHOUT_SECONDS_PATTERN_BR = "dd/MM/yyyy HH:mm";
    public static final String DATE_HOUR_WITHOUT_SECONDS_PATTERN_BR_SHORT = "dd/MM/yy HH";
    public static final String DATE_HOUR_SHORT_WITHOUT_SECONDS_PATTERN_BR = "dd/MM/yy HH:mm";
    public static final String PATTERN_EEEEE = "EEEEE";
    public static final String DEFAULT_TIME_PATTERN = "HH:mm";

    /**
     * Responsável por transformar uma string em padrão para uma entidade Date.
     * @param dateHour
     * @return
     */
    public static DateTime parse(String dateHour, String pattern) {
        if (dateHour == null || dateHour.isEmpty()) {
            return null;
        }

        if (pattern == null || pattern.isEmpty()) {
            pattern = DEFAULT_DATE_TIME_PATTERN;
        }

        return DateTime.parse(dateHour, DateTimeFormat.forPattern(pattern));
    }

    public static LocalDate parseDate(String date, String pattern) {
        if (date == null || date.isEmpty()) {
            return null;
        }

        if (pattern == null || pattern.isEmpty()) {
            pattern = DEFAULT_DATE_PATTERN;
        }

        return LocalDate.parse(date, DateTimeFormat.forPattern(pattern));
    }

    /**
     * Método responsável por ler uma classe Date e transforma-la em String no formato passado.
     * @param dateHour
     * @return formattedDate
     */
    public static String format(DateTime dateHour) {
        return format(dateHour, DEFAULT_DATE_PATTERN);
    }

    /**
     * Método responsável por ler uma classe Date e transforma-la em String no formato passado.
     * @param dateHour
     * @param pattern
     * @return formattedDate
     */
    public static String format(DateTime dateHour, String pattern) {
        if (dateHour == null) {
            return null;
        }

        if (pattern == null || pattern.isEmpty()) {
            pattern = DATE_HOUR_PATTERN_BR;
        }

        return dateHour.toString(pattern);
    }

    public static String formatTime(LocalTime time) {
        return formatTime(time, HOUR_PATTERN_BR);
    }

    public static String formatTime(LocalTime time, String pattern) {
        if (time == null) {
            return null;
        }

        if (pattern == null || pattern.isEmpty()) {
            pattern = HOUR_PATTERN_BR;
        }

        return time.toString(pattern);
    }

    /**
     * Responsável por transformar uma string em padrão para uma entidade Date.
     * @param dateHour
     * @return
     * @throws ParseException
     */
    public static LocalDate parseLocalDate(String dateHour, String pattern) throws ParseException {
        if (dateHour == null || dateHour.isEmpty()) {
            return null;
        }

        if (pattern == null || pattern.isEmpty()) {
            pattern = DEFAULT_DATE_TIME_PATTERN;
        }

        return LocalDate.parse(dateHour, DateTimeFormat.forPattern(pattern));
    }

    /**
     * Método responsável por ler uma classe Date e transforma-la em String no formato passado.
     * @param dateHour
     * @param pattern
     * @return formattedDate
     */
    public static String format(LocalDate dateHour, String pattern) {
        if (dateHour == null) {
            return null;
        }

        if (pattern == null || pattern.isEmpty()) {
            pattern = DATE_PATTERN_BR;
        }

        return dateHour.toString(pattern);
    }

    public static String format(LocalTime time, String pattern) {
        if (time == null) {
            return null;
        }

        if (pattern == null || pattern.isEmpty()) {
            pattern = HOUR_PATTERN_BR;
        }

        return time.toString(pattern);
    }

    public static LocalTime parseLocalTime(String time, String pattern) {
        if (Strings.isNullOrEmpty(time)) {
            return null;
        }

        if (pattern == null || pattern.isEmpty()) {
            pattern = DEFAULT_TIME_PATTERN;
        }

        return LocalTime.parse(time, DateTimeFormat.forPattern(pattern));
    }

    public static boolean isDate(String candidate, String pattern) {
        try {
            return parseDate(candidate, pattern) != null;
        } catch (Exception ignored) {}
        return false;
    }
}