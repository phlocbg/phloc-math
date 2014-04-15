package numbercruncher.program13_3;

import java.math.BigDecimal;

import numbercruncher.mathutils.BigFunctions;

/**
 * Implement the multithreaded Borwein algorithm for pi.
 */
class PiMTAlgorithm
{
  private static final int MAX_ITERS = 20;

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

  private static final String M = "Main thread: ";
  private static final String Y = "Y thread: ";
  private static final String A = "A thread: ";
  private static final String X = "X thread: ";

  private static final String STARTED = " STARTED";
  private static final String FINISHED = " FINISHED";

  private BigDecimal big1, big2, big4, big6;
  private BigDecimal power2;
  private BigDecimal y, a;

  private final BigDecimal ys[] = new BigDecimal [MAX_ITERS];
  private final BigDecimal as[] = new BigDecimal [MAX_ITERS];

  private final YThread yThread = new YThread ();
  private final AThread aThread = new AThread ();
  private final XThread xThread = new XThread ();

  /**
   * Constructor.
   * 
   * @param digits
   *        the number of digits to compute
   * @param parent
   *        the parent object
   */
  PiMTAlgorithm (final int digits, final IPiBorweinParent parent)
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

    m_aParent.notifyTask (M + "constants" + STARTED);
    big1 = BigDecimal.valueOf (1);
    big2 = BigDecimal.valueOf (2);
    big4 = BigDecimal.valueOf (4);
    big6 = BigDecimal.valueOf (6);
    power2 = big2;
    m_aParent.notifyTask (M + "constants" + FINISHED);

    m_aParent.notifyTask (M + "sqrt2" + STARTED);
    final BigDecimal sqrt2 = BigFunctions.sqrt (big2, scale);
    m_aParent.notifyTask (M + "sqrt2" + FINISHED);

    m_aParent.notifyTask (M + "y" + STARTED);
    y = sqrt2.subtract (BigDecimal.valueOf (1));
    m_aParent.notifyTask (M + "y" + FINISHED);

    m_aParent.notifyTask (M + "a" + STARTED);
    a = big6.subtract (big4.multiply (sqrt2).setScale (scale, BigDecimal.ROUND_HALF_EVEN));
    m_aParent.notifyTask (M + "a" + FINISHED);

    yThread.start ();
    aThread.start ();
    xThread.start ();

    try
    {
      xThread.join ();
    }
    catch (final InterruptedException ex)
    {}

    m_aParent.notifyPhase (PiBorweinConstants.DONE);
  }

  private class YThread extends Thread
  {
    private BigDecimal y2, y4, yRoot4, yNumerator, yDenominator;
    private int i = 0;

    @Override
    public void run ()
    {
      for (;;)
      {
        ++i;
        compute ();

        if (y.signum () == 0)
          break;
        ys[i] = y;

        aThread.resume ();
        yield ();
      }

      aThread.resume ();
      iterations = i;
    }

    private void compute ()
    {
      m_aParent.notifyTask (Y + "y4" + "[" + i + "]" + STARTED);
      y4 = y.multiply (y).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      y4 = y4.multiply (y4).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      m_aParent.notifyTask (Y + "y4" + "[" + i + "]" + FINISHED);

      m_aParent.notifyTask (Y + "yRoot4" + "[" + i + "]" + STARTED);
      yRoot4 = BigFunctions.sqrt (big1.subtract (y4), scale);
      yRoot4 = BigFunctions.sqrt (yRoot4, scale);
      m_aParent.notifyTask (Y + "yRoot4" + "[" + i + "]" + FINISHED);

      m_aParent.notifyTask (Y + "y" + "[" + i + "]" + STARTED);
      yNumerator = big1.subtract (yRoot4);
      yDenominator = big1.add (yRoot4);
      y = yNumerator.divide (yDenominator, scale, BigDecimal.ROUND_HALF_EVEN);
      m_aParent.notifyTask (Y + "y" + "[" + i + "]" + FINISHED);
    }
  }

  private class AThread extends Thread
  {
    private BigDecimal aTerm4, aTerm, y2;
    private int i = 0;

    @Override
    public void run ()
    {
      for (;;)
      {
        if (ys[++i] == null)
        {
          if (yThread.isAlive ())
            suspend ();
          if (ys[i] == null)
            break;
        }

        compute ();

        xThread.resume ();
        yield ();
      }

      xThread.resume ();
    }

    private void compute ()
    {
      final BigDecimal y = ys[i];

      m_aParent.notifyTask (A + "aTerm" + "[" + i + "]" + STARTED);
      aTerm4 = big1.add (y);
      aTerm4 = aTerm4.multiply (aTerm4).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      aTerm4 = aTerm4.multiply (aTerm4).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      a = a.multiply (aTerm4).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      m_aParent.notifyTask (A + "aTerm" + "[" + i + "]" + FINISHED);

      m_aParent.notifyTask (A + "power2" + "[" + i + "]" + STARTED);
      power2 = power2.multiply (big4).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      m_aParent.notifyTask (A + "power2" + "[" + i + "]" + FINISHED);

      m_aParent.notifyTask (A + "y2" + "[" + i + "]" + STARTED);
      y2 = y.multiply (y).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      m_aParent.notifyTask (A + "y2" + "[" + i + "]" + FINISHED);

      m_aParent.notifyTask (A + "a" + "[" + i + "]" + STARTED);
      aTerm = big1.add (y).add (y2);
      aTerm = power2.multiply (y).multiply (aTerm).setScale (scale, BigDecimal.ROUND_HALF_EVEN);
      a = a.subtract (aTerm).setScale (scale, BigDecimal.ROUND_HALF_EVEN);

      as[i] = a;
      m_aParent.notifyTask (A + "a" + "[" + i + "]" + FINISHED);
    }
  }

  private class XThread extends Thread
  {
    private int i = 0;

    @Override
    public void run ()
    {
      for (;;)
      {
        if (as[++i] == null)
        {
          if (aThread.isAlive ())
            suspend ();
          if (as[i] == null)
            break;
        }

        compute ();
        yield ();
      }
    }

    private void compute ()
    {
      final BigDecimal a = as[i];

      m_aParent.notifyTask (X + "Inverting a" + "[" + i + "]" + STARTED);
      pi = big1.divide (a, m_nDigits, BigDecimal.ROUND_DOWN);
      m_aParent.notifyTask (X + "Inverting a" + "[" + i + "]" + FINISHED);
    }
  }
}
