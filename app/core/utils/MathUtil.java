package core.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;

/**
 * Classe para tratar operações financeiras.
 */
public final class MathUtil {
  
  private MathUtil() {
  }
  
  /**
   * BigDecimal para 100
   */
  public static final BigDecimal HUNDRED = new BigDecimal(BigInteger.valueOf(100), 0);
  
  /**
   * Escala padrão das operações
   */
  public static final int SCALE_DEFAULT = 4;
  
  /**
   * Tipo de arredondamento padrão
   */
  public static final RoundingMode ROUND_TYPE_DEFAULT = RoundingMode.HALF_DOWN;
  
  private static final Random RANDOM = new Random();
  
  /**
   * Efetua uma divisão entre um BigDecimal e um
   *
   * @param dividend
   * @param divisor
   *
   * @return
   */
  public static BigDecimal divide(BigDecimal dividend, int divisor) {
    return divide(dividend, new BigDecimal(BigInteger.valueOf(divisor)));
  }
  
  /**
   * Efetua uma divisão entre um BigDecimal e um
   *
   * @param dividend
   * @param divisor
   *
   * @return
   */
  public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
    return dividend.divide(divisor, SCALE_DEFAULT, ROUND_TYPE_DEFAULT);
  }
  
  /**
   * Efetua uma divisão entre um BigDecimal e um
   *
   * @param dividend
   * @param divisor
   *
   * @return
   */
  public static BigDecimal divide(BigInteger dividend, BigDecimal divisor) {
    return new BigDecimal(dividend).divide(divisor, SCALE_DEFAULT, ROUND_TYPE_DEFAULT);
  }
  
  /**
   * Efetua uma divisão entre um BigDecimal e um
   *
   * @param dividend
   * @param divisor
   *
   * @return
   */
  public static BigDecimal divide(BigInteger dividend, int divisor) {
    return divide(divide(new BigDecimal(dividend), HUNDRED), divisor);
  }
  
  public static int randomNumber(int start, int end) {
    long range = (long) end - (long) start + 1;
    // compute a fraction of the range, 0 <= frac < range
    long fraction = (long) (range * RANDOM.nextDouble());
    return (int) (fraction + start);
  }
  
  /**
   * Transforma uma String em um Integer.
   * Se der algum erro é retornado zero
   *
   * @param valueStr String do Valor
   *
   * @return Inteiro ou null
   */
  public static Integer convertIntegerStr(String valueStr) {
    try {
      return Integer.parseInt(valueStr);
    } catch (Exception e) {
      return null;
    }
  }
  
}