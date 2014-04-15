package numbercruncher.program13_3;

import java.math.BigDecimal;

import numbercruncher.mathutils.BigFunctions;

/**
 * Implement the Borwein algorithm for pi.
 */
class PiBorweinAlgorithm
{
  /** number of pi digits */
  private final int m_nDigits;
  /** computation scale */
  private final int scale;
  /** iteration counter */
  private int iterations;

  /** value of pi */
  private BigDecimal pi;
  /** parent object */
  private final IPiBorweinParent m_aParent;

  /**
   * Constructor.
   * 
   * @param digits
   *        the number of digits to compute
   * @param parent
   *        the parent object
   */
  PiBorweinAlgorithm (final int digits, final IPiBorweinParent parent)
  {
    this.m_nDigits = digits;
    this.m_aParent = parent;
    this.scale = Math.max (((int) (1.005f * digits)), (digits + 5));
  }

  /**
   * Get the number of iterations.
   * 
   * @return the number of iterations
   */
  int getIterations ()
  {
    return iterations;
  }

  /**
   * Get the value of pi as a string.
   * 
   * @return the string
   */
  String getPi ()
  {
    return pi.toString ();
  }

  /**
   * Compute the digits of pi. Notify the parent of each phase and task.
   */
  void compute ()
  {
    m_aParent.notifyScale (scale);

    // Initialization phase.
    m_aParent.notifyPhase (PiBorweinConstants.INITIALIZING);
    m_aParent.notifyTask ("constants");

    final BigDecimal big1 = BigDecimal.valueOf (1);
    final BigDecimal big2 = BigDecimal.valueOf (2);
    final BigDecimal big4 = BigDecimal.valueOf (4);
    final BigDecimal big6 = BigDecimal.valueOf (6);
    BigDecimal power2 = big2;

    m_aParent.notifyTask ("sqrt2");
    final BigDecimal sqrt2 = BigFunctions.sqrt (big2, scale);

    m_aParent.notifyTask ("y");
    BigDecimal y = sqrt2.subtract (BigDecimal.valueOf (1));

    m_aParent.notifyTask ("a");
    BigDecimal a = big6.subtract (big4.multiply (sqrt2).setScale (scale, BigDecimal.ROUND_HALF_EVEN));

    BigDecimal y2, y4, yRoot4, yNumerator, yDenominator;
    BigDecimal aTerm4, aTerm;

    // Loop once per iteration.
    for (;;)
    {
      m_aParent.notifyPhase (++iterations);

      m_aParent.notifyTask ("y4");
      y4 = y.multiply (y).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      y4 = y4.multiply (y4).setScale (scale, BigDecimal.ROUND_HALF_EVEN);

      m_aParent.notifyTask ("yRoot4");
      yRoot4 = BigFunctions.sqrt (big1.subtract (y4), scale);
      yRoot4 = BigFunctions.sqrt (yRoot4, scale);

      m_aParent.notifyTask ("y");
      yNumerator = big1.subtract (yRoot4);
      yDenominator = big1.add (yRoot4);
      y = yNumerator.divide (yDenominator, scale, BigDecimal.ROUND_HALF_EVEN);

      if (y.signum () == 0)
        break;

      m_aParent.notifyTask ("aTerm");
      aTerm4 = big1.add (y);
      aTerm4 = aTerm4.multiply (aTerm4).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      aTerm4 = aTerm4.multiply (aTerm4).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      a = a.multiply (aTerm4).setScale (scale, BigDecimal.ROUND_HALF_EVEN);

      m_aParent.notifyTask ("power2");
      power2 = power2.multiply (big4).setScale (scale, BigDecimal.ROUND_HALF_EVEN);

      m_aParent.notifyTask ("y2");
      y2 = y.multiply (y).setScale (scale, BigDecimal.ROUND_HALF_EVEN);

      m_aParent.notifyTask ("a");
      aTerm = big1.add (y).add (y2);
      aTerm = power2.multiply (y).multiply (aTerm).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      a = a.subtract (aTerm).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
    }

    // Inversion phase.
    m_aParent.notifyPhase (PiBorweinConstants.INVERTING);
    pi = big1.divide (a, m_nDigits, BigDecimal.ROUND_DOWN);

    m_aParent.notifyPhase (PiBorweinConstants.DONE);
  }
}
