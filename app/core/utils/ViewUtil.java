package core.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class ViewUtil {

    private static final Locale DEFAULT_LOCALE = new Locale("pt-BR", "BR");
    private static final DecimalFormatSymbols DEFAULT_SYMBOL = new DecimalFormatSymbols(DEFAULT_LOCALE);

    public static final java.text.DecimalFormat DEFAULT_DECIMAL_FORMAT;
    public static final java.text.DecimalFormat DEFAULT_PERCENT_FORMAT = new java.text.DecimalFormat("#,##0.00%");

    static {
        DEFAULT_SYMBOL.setCurrencySymbol("R$");
        DEFAULT_DECIMAL_FORMAT = new java.text.DecimalFormat("¤ ###,###,##0.00", DEFAULT_SYMBOL);
    }

    /**
     * Formata um valor financeiro para exibição na view
     * @param valueInCents Valor em centavos
     * @return
     */
    public static String formatMoney(BigInteger valueInCents) {
        return formatMoney(MathUtil.divide(new BigDecimal(valueInCents), MathUtil.HUNDRED));
    }

    public static String formatMoney(BigDecimal value) {
        if (value != null) {
            return DEFAULT_DECIMAL_FORMAT.format(value);
        }
        return "R$ -";
    }

    /**
     * Formata um valor percentual para exibição na view
     *
     * @param value
     * @return
     */
    public static String formatPercent(BigDecimal value) {
        if (value != null) {
            return DEFAULT_PERCENT_FORMAT.format(MathUtil.divide(value, MathUtil.HUNDRED));
        }
        return "--";
    }

}
