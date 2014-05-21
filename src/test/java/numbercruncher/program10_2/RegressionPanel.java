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
package numbercruncher.program10_2;

import numbercruncher.graphutils.PlotProperties;
import numbercruncher.mathutils.DataPoint;
import numbercruncher.mathutils.RegressionPolynomial;
import numbercruncher.pointutils.InterRegressPanel;

/**
 * The demo panel for the Polynomial Regression program and applet.
 */
public class RegressionPanel extends InterRegressPanel
{
  private static final int MAX_POINTS = 100;

  /** regression polynomial degree */
  int m_nDegree = 1;
  /** regression polynomial function */
  RegressionPolynomial poly;

  /**
   * Constructor.
   */
  RegressionPanel ()
  {
    super (MAX_POINTS, "Regression poly", "Reset", true);
    poly = new RegressionPolynomial (m_nDegree, MAX_POINTS);
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
    if (n > m_nDegree)
      actionButton1.setEnabled (true);

    final PlotProperties props = getPlotProperties ();

    final float x = props.getXMin () + c * props.getXDelta ();
    final float y = props.getYMax () - r * props.getYDelta ();

    poly.addDataPoint (new DataPoint (x, y));
  }

  /**
   * Button 1 action: Construct and plot the regression polynomial.
   */
  @Override
  protected void doButton1Action ()
  {
    drawDots ();
    plotOK = true;

    try
    {
      poly.computeCoefficients ();
      plotFunction ();

      String label = "Regression polynomial of degree " + m_nDegree;
      final String message = poly.getWarningMessage ();
      if (message != null)
        label += "  (WARNING: " + message;

      setHeaderLabel (label);
    }
    catch (final Exception ex)
    {
      setHeaderLabel ("Could not generate polynomial:  " + ex.getMessage (), MAROON);
    }
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
   * Return the value of the regression poly function at x.
   * 
   * @param x
   *        the value of x
   * @return the value of the function
   */
  @Override
  public float valueAt (final float x)
  {
    return poly.at (x);
  }

  /**
   * Notification that the plot bounds changed. Redraw the panel.
   */
  @Override
  public void plotBoundsChanged ()
  {
    n = 0;
    draw ();
  }

  /**
   * The degree has changed.
   * 
   * @param degree
   *        the new degree
   */
  @Override
  protected void degreeChanged (final int degree)
  {
    this.m_nDegree = degree;
    plotOK = false;

    final DataPoint data[] = poly.getDataPoints ();

    poly = new RegressionPolynomial (degree, MAX_POINTS);
    for (int i = 0; i < n; ++i)
      poly.addDataPoint (data[i]);

    actionButton1.setEnabled (n > degree);
  }

  /**
   * Reset.
   */
  @Override
  protected void reset ()
  {
    super.reset ();
    plotOK = false;
    poly.reset ();
  }
}
