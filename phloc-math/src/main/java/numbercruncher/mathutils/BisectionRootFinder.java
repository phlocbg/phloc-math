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
 * The root finder class that implements the bisection algorithm.
 */
public class BisectionRootFinder extends RootFinder
{
  private static final int MAX_ITERS = 50;
  private static final float TOLERANCE = 100 * Epsilon.floatValue ();

  /** x-negative value */
  private float xNeg;
  /** x-middle value */
  private float xMid = Float.NaN;
  /** x-positive value */
  private float xPos;
  /** previous x-middle value */
  private float prevXMid;
  /** f(xNeg) */
  private float fNeg;
  /** f(xMid) */
  private float fMid;
  /** f(xPos) */
  private float fPos;

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
  public BisectionRootFinder (final Function function, final float xMin, final float xMax) throws RootFinder.InvalidIntervalException
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
   * Return the current value of x-middle.
   * 
   * @return the value
   */
  public float getXMid ()
  {
    return xMid;
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
   * Return the current value of f(x-middle).
   * 
   * @return the value
   */
  public float getFMid ()
  {
    return fMid;
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
   * Do the bisection iteration procedure.
   * 
   * @param n
   *        the iteration count
   */
  @Override
  protected void doIterationProcedure (final int n)
  {
    if (n == 1)
      return; // already initialized

    if (fMid < 0)
    {
      xNeg = xMid; // the root is in the xPos half
      fNeg = fMid;
    }
    else
    {
      xPos = xMid; // the root is in the xNeg half
      fPos = fMid;
    }
  }

  /**
   * Compute the next position of xMid.
   */
  @Override
  protected void computeNextPosition ()
  {
    prevXMid = xMid;
    xMid = (xNeg + xPos) / 2;
    fMid = m_aFunction.at (xMid);
  }

  /**
   * Check the position of xMid.
   * 
   * @throws PositionUnchangedException
   */
  @Override
  protected void checkPosition () throws RootFinder.PositionUnchangedException
  {
    if (EqualsUtils.equals (xMid, prevXMid))
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
    return Math.abs (fMid) < TOLERANCE;
  }
}
