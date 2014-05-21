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

/**
 * The root finder class that implements the fixed-point iteration algorithm.
 */
public class FixedPointRootFinder extends RootFinder
{
  private static final int MAX_ITERS = 50;
  private static final float TOLERANCE = 100 * Epsilon.floatValue ();

  /** x[n] value */
  private float xn = Float.NaN;
  /** previous x[n] value */
  private float prevXn;
  /** g(x[n]) */
  private float gn;

  /**
   * Constructor.
   * 
   * @param function
   *        the functions whose roots to find
   */
  public FixedPointRootFinder (final Function function)
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
    gn = x0;
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
   * Return the current value of g(x[n]).
   * 
   * @return the value
   */
  public float getGn ()
  {
    return gn;
  }

  // -----------------------------//
  // RootFinder method overrides //
  // -----------------------------//

  /**
   * Do the fixed point iteration procedure. (Nothing to do!)
   * 
   * @param n
   *        the iteration count
   */
  @Override
  protected void doIterationProcedure (final int n)
  {}

  /**
   * Compute the next position of xn.
   */
  @Override
  protected void computeNextPosition ()
  {
    prevXn = xn;
    xn = gn;
    gn = m_aFunction.at (xn);
  }

  /**
   * Check the position of xn.
   * 
   * @throws PositionUnchangedException
   */
  @Override
  protected void checkPosition () throws RootFinder.PositionUnchangedException
  {
    if (xn == prevXn)
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
    return Math.abs ((gn - xn) / xn) < TOLERANCE;
  }
}
