package core.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;

/** Math helper methods. */
public final class MathUtil {
  
  private MathUtil() {
  }
  
  /** Hundred. */
  public static final BigDecimal HUNDRED = new BigDecimal(BigInteger.valueOf(100), 0);
  
  /** Default scale. */
  public static final int SCALE_DEFAULT = 4;
  
  /** Default rounding type. */
  public static final RoundingMode ROUND_TYPE_DEFAULT = RoundingMode.HALF_DOWN;
  
  private static final Random RANDOM = new Random();
  
  /**
   * Divide.
   *
   * @param dividend Dividend.
   * @param divisor  Divisor.
   *
   * @return Divided value as {@link BigDecimal}.
   */
  public static BigDecimal divide(BigDecimal dividend, int divisor) {
    return divide(dividend, new BigDecimal(BigInteger.valueOf(divisor)));
  }
  
  /**
   * Divide.
   *
   * @param dividend Dividend.
   * @param divisor  Divisor.
   *
   * @return Divided value as {@link BigDecimal}.
   */
  public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
    return dividend.divide(divisor, SCALE_DEFAULT, ROUND_TYPE_DEFAULT);
  }
  
  /**
   * Divide.
   *
   * @param dividend Dividend.
   * @param divisor  Divisor.
   *
   * @return Divided value as {@link BigDecimal}.
   */
  public static BigDecimal divide(BigInteger dividend, BigDecimal divisor) {
    return new BigDecimal(dividend).divide(divisor, SCALE_DEFAULT, ROUND_TYPE_DEFAULT);
  }
  
  /**
   * Divide.
   *
   * @param dividend Dividend.
   * @param divisor  Divisor.
   *
   * @return Divided value as {@link BigDecimal}.
   */
  public static BigDecimal divide(BigInteger dividend, int divisor) {
    return divide(divide(new BigDecimal(dividend), HUNDRED), divisor);
  }
  
  /**
   * Return a random number between range.
   *
   * @param start Start range.
   * @param end   End range.
   *
   * @return Random number.
   */
  public static int randomNumber(int start, int end) {
    long range = (long) end - (long) start + 1;
    // compute a fraction of the range, 0 <= frac < range
    long fraction = (long) (range * RANDOM.nextDouble());
    return (int) (fraction + start);
  }
  
}