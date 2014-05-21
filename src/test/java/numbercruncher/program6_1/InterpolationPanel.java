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
package numbercruncher.program6_1;

import numbercruncher.graphutils.PlotProperties;
import numbercruncher.mathutils.DataPoint;
import numbercruncher.mathutils.InterpolationPolynomial;
import numbercruncher.pointutils.InterRegressPanel;

/**
 * The demo panel for the Polynomial Interpolation demo and applet.
 */
public class InterpolationPanel extends InterRegressPanel
{
  private static final int MAX_POINTS = 10;

  /** interpolation polynomnial function */
  InterpolationPolynomial p = new InterpolationPolynomial (MAX_POINTS);

  /**
   * Constructor.
   */
  InterpolationPanel ()
  {
    super (MAX_POINTS, "Interpolate", "Reset");
  }

  /**
   * The user has added a data point.
   * 
   * @param r
   *        the dot's row
   * @param c
   *        the dot's column
   */
  @Override
  protected void doDotAction (final int r, final int c)
  {
    if (n > 1)
      actionButton1.setEnabled (true);

    final PlotProperties props = getPlotProperties ();

    final float x = props.getXMin () + c * props.getXDelta ();
    final float y = props.getYMax () - r * props.getYDelta ();

    p.addDataPoint (new DataPoint (x, y));
  }

  /**
   * Button 1 action: Construct and plot the interpolation function.
   */
  @Override
  protected void doButton1Action ()
  {
    drawDots ();
    plotOK = true;
    plotFunction ();

    setHeaderLabel ("Polynomial interpolation function of degree " + (n - 1));
  }

  /**
   * Button 2 action: Reset.
   */
  @Override
  protected void doButton2Action ()
  {
    reset ();
    draw ();

    setHeaderLabel ("");
    actionButton1.setEnabled (false);
  }

  // ------------------//
  // Method overrides //
  // ------------------//

  /**
   * Return the value of the polynomial interpolation function at x.
   * 
   * @param x
   *        the value of x
   * @return the value of the function
   */
  @Override
  public float valueAt (final float x)
  {
    return p.at (x);
  }

  /**
   * Reset.
   */
  @Override
  protected void reset ()
  {
    super.reset ();
    plotOK = false;
    p.reset ();
  }
}
