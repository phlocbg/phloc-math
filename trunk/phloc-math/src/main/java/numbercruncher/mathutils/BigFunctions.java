package numbercruncher.mathutils;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;

/**
 * Several useful BigDecimal mathematical functions.
 */
public final class BigFunctions
{
  /**
   * Compute x^exponent to a given scale. Uses the same algorithm as class
   * numbercruncher.mathutils.IntPower.
   * 
   * @param px
   *        the value x
   * @param pexponent
   *        the exponent value
   * @param scale
   *        the desired scale of the result
   * @return the result value
   */
  public static BigDecimal intPower (@Nonnull final BigDecimal px, final long pexponent, final int scale)
  {
    BigDecimal x = px;
    long exponent = pexponent;

    // If the exponent is negative, compute 1/(x^-exponent).
    if (exponent < 0)
    {
      return BigDecimal.ONE.divide (intPower (x, -exponent, scale), scale, BigDecimal.ROUND_HALF_EVEN);
    }

    BigDecimal power = BigDecimal.ONE;

    // Loop to compute value^exponent.
    while (exponent > 0)
    {

      // Is the rightmost bit a 1?
      if ((exponent & 1) == 1)
      {
        power = power.multiply (x).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      }

      // Square x and shift exponent 1 bit to the right.
      x = x.multiply (x).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      exponent >>= 1;

      Thread.yield ();
    }

    return power;
  }

  /**
   * Compute the integral root of x to a given scale, x >= 0. Use Newton's
   * algorithm.
   * 
   * @param px
   *        the value of x
   * @param index
   *        the integral root value
   * @param scale
   *        the desired scale of the result
   * @return the result value
   */
  public static BigDecimal intRoot (@Nonnull final BigDecimal px, final long index, final int scale)
  {
    BigDecimal x = px;

    // Check that x >= 0.
    if (x.signum () < 0)
    {
      throw new IllegalArgumentException ("x < 0: " + x);
    }

    final int sp1 = scale + 1;
    final BigDecimal n = x;
    final BigDecimal i = BigDecimal.valueOf (index);
    final BigDecimal im1 = BigDecimal.valueOf (index - 1);
    final BigDecimal tolerance = BigDecimal.valueOf (5).movePointLeft (sp1);
    BigDecimal xPrev;

    // The initial approximation is x/index.
    x = x.divide (i, scale, BigDecimal.ROUND_HALF_EVEN);

    // Loop until the approximations converge
    // (two successive approximations are equal after rounding).
    do
    {
      // x^(index-1)
      final BigDecimal xToIm1 = intPower (x, index - 1, sp1);

      // x^index
      final BigDecimal xToI = x.multiply (xToIm1).setScale (sp1, BigDecimal.ROUND_HALF_EVEN);

      // n + (index-1)*(x^index)
      final BigDecimal numerator = n.add (im1.multiply (xToI)).setScale (sp1, BigDecimal.ROUND_HALF_EVEN);

      // (index*(x^(index-1))
      final BigDecimal denominator = i.multiply (xToIm1).setScale (sp1, BigDecimal.ROUND_HALF_EVEN);

      // x = (n + (index-1)*(x^index)) / (index*(x^(index-1)))
      xPrev = x;
      x = numerator.divide (denominator, sp1, BigDecimal.ROUND_DOWN);

      Thread.yield ();
    } while (x.subtract (xPrev).abs ().compareTo (tolerance) > 0);

    return x;
  }

  /**
   * Compute e^x to a given scale. Break x into its whole and fraction parts and
   * compute (e^(1 + fraction/whole))^whole using Taylor's formula.
   * 
   * @param x
   *        the value of x
   * @param scale
   *        the desired scale of the result
   * @return the result value
   */
  public static BigDecimal exp (final BigDecimal x, final int scale)
  {
    // e^0 = 1
    if (x.signum () == 0)
    {
      return BigDecimal.ONE;
    }

    // If x is negative, return 1/(e^-x).
    if (x.signum () == -1)
    {
      return BigDecimal.ONE.divide (exp (x.negate (), scale), scale, BigDecimal.ROUND_HALF_EVEN);
    }

    // Compute the whole part of x.
    BigDecimal xWhole = x.setScale (0, BigDecimal.ROUND_DOWN);

    // If there isn't a whole part, compute and return e^x.
    if (xWhole.signum () == 0)
      return expTaylor (x, scale);

    // Compute the fraction part of x.
    final BigDecimal xFraction = x.subtract (xWhole);

    // z = 1 + fraction/whole
    final BigDecimal z = BigDecimal.ONE.add (xFraction.divide (xWhole, scale, BigDecimal.ROUND_HALF_EVEN));

    // t = e^z
    final BigDecimal t = expTaylor (z, scale);

    final BigDecimal maxLong = BigDecimal.valueOf (Long.MAX_VALUE);
    BigDecimal result = BigDecimal.ONE;

    // Compute and return t^whole using intPower().
    // If whole > Long.MAX_VALUE, then first compute products
    // of e^Long.MAX_VALUE.
    while (xWhole.compareTo (maxLong) >= 0)
    {
      result = result.multiply (intPower (t, Long.MAX_VALUE, scale)).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      xWhole = xWhole.subtract (maxLong);

      Thread.yield ();
    }
    return result.multiply (intPower (t, xWhole.longValue (), scale)).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
  }

  /**
   * Compute e^x to a given scale by the Taylor series.
   * 
   * @param x
   *        the value of x
   * @param scale
   *        the desired scale of the result
   * @return the result value
   */
  private static BigDecimal expTaylor (final BigDecimal x, final int scale)
  {
    BigDecimal factorial = BigDecimal.ONE;
    BigDecimal xPower = x;
    BigDecimal sumPrev;

    // 1 + x
    BigDecimal sum = x.add (BigDecimal.ONE);

    // Loop until the sums converge
    // (two successive sums are equal after rounding).
    int i = 2;
    do
    {
      // x^i
      xPower = xPower.multiply (x).setScale (scale, BigDecimal.ROUND_HALF_EVEN);

      // i!
      factorial = factorial.multiply (BigDecimal.valueOf (i));

      // x^i/i!
      final BigDecimal term = xPower.divide (factorial, scale, BigDecimal.ROUND_HALF_EVEN);

      // sum = sum + x^i/i!
      sumPrev = sum;
      sum = sum.add (term);

      ++i;
      Thread.yield ();
    } while (sum.compareTo (sumPrev) != 0);

    return sum;
  }

  /**
   * Compute the natural logarithm of x to a given scale, x > 0.
   */
  public static BigDecimal ln (@Nonnull final BigDecimal x, final int scale)
  {
    // Check that x > 0.
    if (x.signum () <= 0)
    {
      throw new IllegalArgumentException ("x <= 0: " + x);
    }

    // The number of digits to the left of the decimal point.
    final int magnitude = x.toString ().length () - x.scale () - 1;

    if (magnitude < 3)
    {
      return lnNewton (x, scale);
    }

    // Compute magnitude*ln(x^(1/magnitude)).

    // x^(1/magnitude)
    final BigDecimal root = intRoot (x, magnitude, scale);

    // ln(x^(1/magnitude))
    final BigDecimal lnRoot = lnNewton (root, scale);

    // magnitude*ln(x^(1/magnitude))
    return BigDecimal.valueOf (magnitude).multiply (lnRoot).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
  }

  /**
   * Compute the natural logarithm of x to a given scale, x > 0. Use Newton's
   * algorithm.
   */
  private static BigDecimal lnNewton (@Nonnull final BigDecimal px, final int scale)
  {
    BigDecimal x = px;
    final int sp1 = scale + 1;
    final BigDecimal n = x;
    BigDecimal term;

    // Convergence tolerance = 5*(10^-(scale+1))
    final BigDecimal tolerance = BigDecimal.valueOf (5).movePointLeft (sp1);

    // Loop until the approximations converge
    // (two successive approximations are within the tolerance).
    do
    {

      // e^x
      final BigDecimal eToX = exp (x, sp1);

      // (e^x - n)/e^x
      term = eToX.subtract (n).divide (eToX, sp1, BigDecimal.ROUND_DOWN);

      // x - (e^x - n)/e^x
      x = x.subtract (term);

      Thread.yield ();
    } while (term.compareTo (tolerance) > 0);

    return x.setScale (scale, BigDecimal.ROUND_HALF_EVEN);
  }

  /**
   * Compute the arctangent of x to a given scale, |x| < 1
   * 
   * @param x
   *        the value of x
   * @param scale
   *        the desired scale of the result
   * @return the result value
   */
  public static BigDecimal arctan (@Nonnull final BigDecimal x, final int scale)
  {
    // Check that |x| < 1.
    if (x.abs ().compareTo (BigDecimal.ONE) >= 0)
    {
      throw new IllegalArgumentException ("|x| >= 1: " + x);
    }

    // If x is negative, return -arctan(-x).
    if (x.signum () == -1)
    {
      return arctan (x.negate (), scale).negate ();
    }
    return arctanTaylor (x, scale);
  }

  /**
   * Compute the arctangent of x to a given scale by the Taylor series, |x| < 1
   * 
   * @param x
   *        the value of x
   * @param scale
   *        the desired scale of the result
   * @return the result value
   */
  private static BigDecimal arctanTaylor (final BigDecimal x, final int scale)
  {
    final int sp1 = scale + 1;
    int i = 3;
    boolean addFlag = false;

    BigDecimal power = x;
    BigDecimal sum = x;
    BigDecimal term;

    // Convergence tolerance = 5*(10^-(scale+1))
    final BigDecimal tolerance = BigDecimal.valueOf (5).movePointLeft (sp1);

    // Loop until the approximations converge
    // (two successive approximations are within the tolerance).
    do
    {
      // x^i
      power = power.multiply (x).multiply (x).setScale (sp1, BigDecimal.ROUND_HALF_EVEN);

      // (x^i)/i
      term = power.divide (BigDecimal.valueOf (i), sp1, BigDecimal.ROUND_HALF_EVEN);

      // sum = sum +- (x^i)/i
      sum = addFlag ? sum.add (term) : sum.subtract (term);

      i += 2;
      addFlag = !addFlag;

      Thread.yield ();
    } while (term.compareTo (tolerance) > 0);

    return sum;
  }

  /**
   * Compute the square root of x to a given scale, x >= 0. Use Newton's
   * algorithm.
   * 
   * @param x
   *        the value of x
   * @param scale
   *        the desired scale of the result
   * @return the result value
   */
  public static BigDecimal sqrt (@Nonnull final BigDecimal x, final int scale)
  {
    // Check that x >= 0.
    if (x.signum () < 0)
    {
      throw new IllegalArgumentException ("x < 0: " + x);
    }

    // n = x*(10^(2*scale))
    final BigInteger n = x.movePointRight (scale << 1).toBigInteger ();

    // The first approximation is the upper half of n.
    final int bits = (n.bitLength () + 1) >> 1;
    BigInteger ix = n.shiftRight (bits);
    BigInteger ixPrev;

    // Loop until the approximations converge
    // (two successive approximations are equal after rounding).
    do
    {
      ixPrev = ix;

      // x = (x + n/x)/2
      ix = ix.add (n.divide (ix)).shiftRight (1);

      Thread.yield ();
    } while (ix.compareTo (ixPrev) != 0);

    return new BigDecimal (ix, scale);
  }
}
