package numbercruncher.mathutils;

/**
 * The root finder class that implements the regula falsi algorithm.
 */
public class RegulaFalsiRootFinder extends RootFinder
{
  private static final int MAX_ITERS = 50;
  private static final float TOLERANCE = 100 * Epsilon.floatValue ();

  /** x-negative value */
  protected float xNeg;
  /** x-false value */
  protected float xFalse = Float.NaN;
  /** x-positive value */
  protected float xPos;
  /** previous x-false value */
  protected float prevXFalse;
  /** f(xNeg) */
  protected float fNeg;
  /** f(xFalse) */
  protected float fFalse = Float.NaN;
  /** f(xPos) */
  protected float fPos;

  /**
   * Constructor.
   * 
   * @param function
   *        the functions whose roots to find
   * @param xMin
   *        the initial x-value where the function is negative
   * @param xMax
   *        the initial x-value where the function is positive
   * @throws RootFinder.InvalidIntervalException
   */
  public RegulaFalsiRootFinder (final Function function, final float xMin, final float xMax) throws RootFinder.InvalidIntervalException
  {
    super (function, MAX_ITERS);
    checkInterval (xMin, xMax);

    final float yMin = function.at (xMin);
    final float yMax = function.at (xMax);

    // Initialize xNeg, fNeg, xPos, and fPos.
    if (yMin < 0)
    {
      xNeg = xMin;
      xPos = xMax;
      fNeg = yMin;
      fPos = yMax;
    }
    else
    {
      xNeg = xMax;
      xPos = xMin;
      fNeg = yMax;
      fPos = yMin;
    }
  }

  // ---------//
  // Getters //
  // ---------//

  /**
   * Return the current value of x-negative.
   * 
   * @return the value
   */
  public float getXNeg ()
  {
    return xNeg;
  }

  /**
   * Return the current value of x-false.
   * 
   * @return the value
   */
  public float getXFalse ()
  {
    return xFalse;
  }

  /**
   * Return the current value of x-positive.
   * 
   * @return the value
   */
  public float getXPos ()
  {
    return xPos;
  }

  /**
   * Return the current value of f(x-negative).
   * 
   * @return the value
   */
  public float getFNeg ()
  {
    return fNeg;
  }

  /**
   * Return the current value of f(x-false).
   * 
   * @return the value
   */
  public float getFFalse ()
  {
    return fFalse;
  }

  /**
   * Return the current value of f(x-positive).
   * 
   * @return the value
   */
  public float getFPos ()
  {
    return fPos;
  }

  // -----------------------------//
  // RootFinder method overrides //
  // -----------------------------//

  /**
   * Do the regula falsi iteration procedure.
   * 
   * @param n
   *        the iteration count
   */
  @Override
  protected void doIterationProcedure (final int n)
  {
    if (n == 1)
      return; // already initialized

    if (fFalse < 0)
    {
      xNeg = xFalse; // the root is in the xPos side
      fNeg = fFalse;
    }
    else
    {
      xPos = xFalse; // the root is in the xNeg side
      fPos = fFalse;
    }
  }

  /**
   * Compute the next position of x-false.
   */
  @Override
  protected void computeNextPosition ()
  {
    prevXFalse = xFalse;
    xFalse = xPos - fPos * (xNeg - xPos) / (fNeg - fPos);
    fFalse = m_aFunction.at (xFalse);
  }

  /**
   * Check the position of x-false.
   * 
   * @throws PositionUnchangedException
   */
  @Override
  protected void checkPosition () throws RootFinder.PositionUnchangedException
  {
    if (xFalse == prevXFalse)
    {
      throw new RootFinder.PositionUnchangedException ();
    }
  }

  /**
   * Indicate whether or not the algorithm has converged.
   * 
   * @return true if converged, else false
   */
  @Override
  protected boolean hasConverged ()
  {
    return Math.abs (fFalse) < TOLERANCE;
  }
}
