package numbercruncher.mathutils;

import com.phloc.commons.equals.EqualsUtils;

/**
 * The root finder class that implements Newton's algorithm.
 */
public class NewtonsRootFinder extends RootFinder
{
  private static final int MAX_ITERS = 50;
  private static final float TOLERANCE = 100 * Epsilon.floatValue ();

  /** x[n] value */
  private float xn;
  /** x[n+1] value */
  private float xnp1;
  /** previous x[n+1] value */
  private float prevXnp1;
  /** f(x[n]) */
  private float fn;
  /** f(x[n+1]) */
  private float fnp1;
  /** f'(x[n]) */
  private float fpn;

  /**
   * Constructor.
   * 
   * @param function
   *        the functions whose roots to find
   */
  public NewtonsRootFinder (final Function function)
  {
    super (function, MAX_ITERS);
  }

  /**
   * Reset.
   * 
   * @param x0
   *        the initial x-value
   */
  public void reset (final float x0)
  {
    super.reset ();

    xnp1 = x0;
    fnp1 = m_aFunction.at (xnp1);
  }

  // ---------//
  // Getters //
  // ---------//

  /**
   * Return the current value of x[n].
   * 
   * @return the value
   */
  public float getXn ()
  {
    return xn;
  }

  /**
   * Return the current value of x[n+1].
   * 
   * @return the value
   */
  public float getXnp1 ()
  {
    return xnp1;
  }

  /**
   * Return the current value of f(x[n]).
   * 
   * @return the value
   */
  public float getFn ()
  {
    return fn;
  }

  /**
   * Return the current value of f(x[n+1]).
   * 
   * @return the value
   */
  public float getFnp1 ()
  {
    return fnp1;
  }

  /**
   * Return the current value of f'(x[n]).
   * 
   * @return the value
   */
  public float getFpn ()
  {
    return fpn;
  }

  // -----------------------------//
  // RootFinder method overrides //
  // -----------------------------//

  /**
   * Do Newton's iteration procedure.
   * 
   * @param n
   *        the iteration count
   */
  @Override
  protected void doIterationProcedure (final int n)
  {
    xn = xnp1;
  }

  /**
   * Compute the next position of x[n+1].
   */
  @Override
  protected void computeNextPosition ()
  {
    fn = fnp1;
    fpn = m_aFunction.derivativeAt (xn);

    // Compute the value of x[n+1].
    prevXnp1 = xnp1;
    xnp1 = xn - fn / fpn;

    fnp1 = m_aFunction.at (xnp1);
  }

  /**
   * Check the position of x[n+1].
   * 
   * @throws PositionUnchangedException
   */
  @Override
  protected void checkPosition () throws RootFinder.PositionUnchangedException
  {
    if (EqualsUtils.equals (xnp1, prevXnp1))
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
    return Math.abs (fnp1) < TOLERANCE;
  }
}
