package core.utils;

import com.google.common.base.Strings;

import java.text.Normalizer;

public final class StringUtil {

    public static final String EMPTY = "";
    public static final String COMMA = ",";
    public static final String SPACE = " ";
    public static final String DASH = "-";

    public static String toOnlyNumbers(String text) {
        if (Strings.isNullOrEmpty(text))
            return text;

        return text.replaceAll("[\\D]", "");
    }

    public static String toASCII(String text) {
        if (Strings.isNullOrEmpty(text))
            return "";

        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

}
