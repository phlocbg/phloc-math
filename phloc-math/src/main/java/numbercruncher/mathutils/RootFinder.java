package numbercruncher.mathutils;

/**
 * Abstract base class for the root finder classes.
 */
public abstract class RootFinder
{
  /** the function whose roots to find */
  protected Function m_aFunction;
  /** iteration counter */
  private int m_nIndex;
  /** maximum number of iterations */
  private final int m_nMaxIters;

  /**
   * Constructor.
   * 
   * @param function
   *        the function whose roots to find
   * @param maxIters
   *        the maximum number of iterations
   */
  public RootFinder (final Function function, final int maxIters)
  {
    this.m_aFunction = function;
    this.m_nMaxIters = maxIters;
  }

  /**
   * Check the interval.
   * 
   * @param x1
   *        x-coordinate of the left of the interval
   * @param x2
   *        x-coordinate of the right end of the interval
   * @throws InvalidIntervalException
   */
  public void checkInterval (final float x1, final float x2) throws InvalidIntervalException
  {
    final float y1 = m_aFunction.at (x1);
    final float y2 = m_aFunction.at (x2);

    // The interval is invalid if y1 and y2 have the same signs.
    if (y1 * y2 > 0)
      throw new InvalidIntervalException ();
  }

  /**
   * Return the iteration count.
   * 
   * @return the count
   */
  public int getIterationCount ()
  {
    return m_nIndex;
  }

  /**
   * Perform one iteration step.
   * 
   * @return true if the algorithm converged, else false
   * @throws IterationCountExceededException
   * @throws PositionUnchangedException
   */
  public boolean step () throws IterationCountExceededException, PositionUnchangedException
  {
    checkIterationCount ();
    doIterationProcedure (m_nIndex);

    computeNextPosition ();
    checkPosition ();

    return hasConverged ();
  }

  /**
   * Check the iteration count to see if it has exeeded the maximum number of
   * iterations.
   * 
   * @throws IterationCountExceededException
   */
  protected void checkIterationCount () throws IterationCountExceededException
  {
    if (++m_nIndex > m_nMaxIters)
    {
      throw new IterationCountExceededException ();
    }
  }

  /**
   * Reset.
   */
  protected void reset ()
  {
    m_nIndex = 0;
  }

  // ------------------//
  // Subclass methods //
  // ------------------//

  /**
   * Do the iteration procedure.
   * 
   * @param n
   *        the iteration count
   */
  protected abstract void doIterationProcedure (int n);

  /**
   * Compute the next position of x.
   */
  protected abstract void computeNextPosition ();

  /**
   * Check the position of x.
   * 
   * @throws PositionUnchangedException
   */
  protected abstract void checkPosition () throws PositionUnchangedException;

  /**
   * Indicate whether or not the algorithm has converged.
   * 
   * @return true if converged, else false
   */
  protected abstract boolean hasConverged ();

  // ------------------------//
  // Root finder exceptions //
  // ------------------------//

  /**
   * Invalid interval exception.
   */
  public static class InvalidIntervalException extends Exception
  {}

  /**
   * Iteration count exceeded exception.
   */
  public static class IterationCountExceededException extends Exception
  {}

  /**
   * Position unchanged exception.
   */
  public static class PositionUnchangedException extends Exception
  {}
}
