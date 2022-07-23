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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** View helper class. */
public final class ViewUtil {
  
  private ViewUtil() {
  }
  
  private static final Locale               DEFAULT_LOCALE = new Locale("pt-BR", "BR");
  private static final DecimalFormatSymbols DEFAULT_SYMBOL =
      new DecimalFormatSymbols(DEFAULT_LOCALE);
  
  public static final java.text.DecimalFormat DEFAULT_DECIMAL_FORMAT;
  public static final java.text.DecimalFormat DEFAULT_PERCENT_FORMAT =
      new java.text.DecimalFormat("#,##0.00%");
  
  static {
    DEFAULT_SYMBOL.setCurrencySymbol("R$");
    DEFAULT_DECIMAL_FORMAT = new java.text.DecimalFormat("¤ ###,###,##0.00", DEFAULT_SYMBOL);
  }
  
  /**
   * Format value in cents to monetary string.
   *
   * @param valueInCents Value in cents.
   *
   * @return Money.
   */
  public static String formatMoney(BigInteger valueInCents) {
    return formatMoney(MathUtil.divide(new BigDecimal(valueInCents), MathUtil.HUNDRED));
  }
  
  /**
   * Format value in cents to monetary string.
   *
   * @param value Value as decimal.
   *
   * @return Money.
   */
  public static String formatMoney(BigDecimal value) {
    if (value != null) {
      return DEFAULT_DECIMAL_FORMAT.format(value);
    }
    return "R$ -";
  }
  
  /**
   * Format to percentage.
   *
   * @param value Value in decimal.
   *
   * @return Percentage.
   */
  public static String formatPercent(BigDecimal value) {
    if (value != null) {
      return DEFAULT_PERCENT_FORMAT.format(MathUtil.divide(value, MathUtil.HUNDRED));
    }
    return "--";
  }
  
}
