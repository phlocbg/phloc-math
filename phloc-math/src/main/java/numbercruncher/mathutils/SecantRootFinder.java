/**
 * Copyright (C) 2006-2014 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package numbercruncher.mathutils;

import com.phloc.commons.equals.EqualsUtils;

/**
 * The root finder class that implements the secant algorithm.
 */
public class SecantRootFinder extends RootFinder
{
  private static final int MAX_ITERS = 50;
  private static final float TOLERANCE = 100 * Epsilon.floatValue ();

  /** x[n-1] value */
  private float xnm1;
  /** x[n] value */
  private float xn;
  /** x[n+1] value */
  private float xnp1 = Float.NaN;
  /** previous value of x[n+1] */
  private float prevXnp1;
  /** f(x[n-1]) */
  private float fnm1;
  /** f([n]) */
  private float fn;
  /** f(x[n+1]) */
  private float fnp1;

  /**
   * Constructor.
   * 
   * @param function
   *        the functions whose roots to find
   * @param x0
   *        the first initial x-value
   * @param x1
   *        the second initial x-value
   */
  public SecantRootFinder (final Function function, final float x0, final float x1)
  {
    super (function, MAX_ITERS);

    // Initialize x[n-1], x[n], f(x[n-1]), and f(x[n]).
    xnm1 = x0;
    fnm1 = function.at (xnm1);
    xn = x1;
    fn = function.at (xn);
  }

  // ---------//
  // Getters //
  // ---------//

  /**
   * Return the current value of x[n-1].
   * 
   * @return the value
   */
  public float getXnm1 ()
  {
    return xnm1;
  }

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
   * Return the current value of f(x[n-1]).
   * 
   * @return the value
   */
  public float getFnm1 ()
  {
    return fnm1;
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

  // -----------------------------//
  // RootFinder method overrides //
  // -----------------------------//

  /**
   * Do the secant iteration procedure.
   * 
   * @param n
   *        the iteration count
   */
  @Override
  protected void doIterationProcedure (final int n)
  {
    if (n == 1)
      return; // already initialized

    // Use the latest two points.
    xnm1 = xn; // x[n-1] = x[n]
    xn = xnp1; // x[n] = x[n+1]
    fnm1 = fn; // f(x[n-1]) = f(x[n])
    fn = fnp1; // f(x[n]) = f(x[n+1])
  }

  /**
   * Compute the next position of x[n+1].
   */
  @Override
  protected void computeNextPosition ()
  {
    prevXnp1 = xnp1;
    xnp1 = xn - fn * (xnm1 - xn) / (fnm1 - fn);
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
