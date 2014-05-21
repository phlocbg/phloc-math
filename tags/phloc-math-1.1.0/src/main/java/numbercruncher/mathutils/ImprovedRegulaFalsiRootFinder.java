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
 * The root finder class that implements the improved regula falsi algorithm.
 */
public class ImprovedRegulaFalsiRootFinder extends RegulaFalsiRootFinder
{
  /** previous f(xFalse) value */
  private float prevFFalse;

  private boolean decreasePos = false;
  private boolean decreaseNeg = false;

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
  public ImprovedRegulaFalsiRootFinder (final Function function, final float xMin, final float xMax) throws RootFinder.InvalidIntervalException
  {
    super (function, xMin, xMax);
  }

  // ----------------------------------------//
  // Override RegulaFalsiRootFinder methods //
  // ----------------------------------------//

  /**
   * Do the improved regula falsi iteration procedure.
   * 
   * @param n
   *        the iteration count
   */
  @Override
  protected void doIterationProcedure (final int n)
  {
    super.doIterationProcedure (n);

    // Decrease the slope of the secant?
    if (decreasePos)
      fPos /= 2;
    if (decreaseNeg)
      fNeg /= 2;
  }

  /**
   * Compute the next position of xFalse.
   */
  @Override
  protected void computeNextPosition ()
  {
    prevXFalse = xFalse;
    prevFFalse = fFalse;
    xFalse = xPos - fPos * (xNeg - xPos) / (fNeg - fPos);
    fFalse = m_aFunction.at (xFalse);

    decreasePos = decreaseNeg = false;

    // If there was no sign change in f(xFalse),
    // or if this is the first iteration step,
    // then decrease the slope of the secant.
    if (Float.isNaN (prevFFalse) || (prevFFalse * fFalse > 0))
    {
      if (fFalse < 0)
        decreasePos = true;
      else
        decreaseNeg = true;
    }
  }
}
