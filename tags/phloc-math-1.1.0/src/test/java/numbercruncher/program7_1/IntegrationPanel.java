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
package numbercruncher.program7_1;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import numbercruncher.graphutils.PlotProperties;
import numbercruncher.mathutils.DataPoint;
import numbercruncher.mathutils.Epsilon;
import numbercruncher.mathutils.IIntegrator;
import numbercruncher.mathutils.InterpolationPolynomial;
import numbercruncher.mathutils.SimpsonsIntegrator;
import numbercruncher.mathutils.TrapezoidalIntegrator;
import numbercruncher.pointutils.UserPointPanel;

/**
 * The demo panel for the numerical integration demo and applet.
 */
public class IntegrationPanel extends UserPointPanel
{
  private static final int MAX_POINTS = 10;
  private static final int MAX_INTERVALS = 512;
  private static final float TOLERANCE = 100 * Epsilon.floatValue ();

  private static final float FROM_LIMIT = 2.0f;
  private static final float TO_LIMIT = 10.0f;

  private static final int TRAPEZOIDAL = 0;
  @SuppressWarnings ("unused")
  private static final int SIMPSONS = 1;

  private static final String ALGORITHMS[] = { "the Trapezoidal", "Simpson's" };

  private static final Color COLOR_0 = new Color (173, 216, 230);
  private static final Color COLOR_1 = new Color (195, 236, 255);

  /** area label */
  private final Label areaLabel = new Label ("Area:");
  /** area value */
  private final Label areaText = new Label (" ");
  /**
   * algorithm label
   */
  private final Label integratorLabel = new Label ("Algorithm:");
  /**
   * algorithm choice
   */
  private final Choice integratorChoice = new Choice ();

  /** true if OK to plot function */
  private boolean plotOK = false;

  /** integrand (interpolation polynomnial function) */
  InterpolationPolynomial integrand = new InterpolationPolynomial (MAX_POINTS);

  /** integrator */
  IIntegrator integrator;

  /** number of intervals */
  private int intervals = 1;
  /** true if function plotted */
  private boolean isPlotted = false;

  /** chosen algorithm */
  private int algorithm;
  /** computed area */
  private float area;

  /**
   * Constructor.
   */
  IntegrationPanel ()
  {
    super (MAX_POINTS, "# points", "Plot integrand", "Reset");

    final Font labelFont = getLabelFont ();
    final Font textFont = getTextFont ();

    // Controls.
    areaLabel.setFont (labelFont);
    areaLabel.setAlignment (Label.RIGHT);
    areaLabel.setForeground (Color.blue);
    areaText.setFont (textFont);
    areaText.setAlignment (Label.LEFT);
    integratorLabel.setFont (labelFont);

    integratorChoice.add ("Trapezoidal");
    integratorChoice.add ("Simpson's");

    // Control panel.
    controlPanel.setLayout (new GridLayout (0, 4, 5, 2));
    controlPanel.add (nLabel);
    controlPanel.add (nText);
    controlPanel.add (integratorLabel);
    controlPanel.add (actionButton1);
    controlPanel.add (areaLabel);
    controlPanel.add (areaText);
    controlPanel.add (integratorChoice);
    controlPanel.add (actionButton2);
    addDemoControls (controlPanel);

    // Algorithm choice handler.
    integratorChoice.addItemListener (new ItemListener ()
    {
      public void itemStateChanged (final ItemEvent ev)
      {
        algorithm = integratorChoice.getSelectedIndex ();
        if (isPlotted)
          plot ();
      }
    });
  }

  /**
   * Plot the function.
   */
  private void plot ()
  {
    clear ();
    plotOK = true;
    plotFunction ();
    isPlotted = true;

    actionButton1.setLabel ("Step");
    actionButton1.setEnabled (true);
    actionButton2.setEnabled (true);

    // Check the function.
    try
    {
      checkFunctionInBounds (FROM_LIMIT, TO_LIMIT);
    }
    catch (final Exception ex)
    {
      processUserError (ex.getMessage ());
      return;
    }

    setHeaderLabel ("Integration by " +
                    ALGORITHMS[algorithm] +
                    " Algorithm over the interval [" +
                    FROM_LIMIT +
                    ", " +
                    TO_LIMIT +
                    "]");

    nLabel.setText ("# intervals");
    nText.setText ("0");
    areaText.setText (" ");

    n = 0;
    intervals = 1;
    area = 0;

    integrator = (algorithm == TRAPEZOIDAL) ? (IIntegrator) new TrapezoidalIntegrator (integrand)
                                           : (IIntegrator) new SimpsonsIntegrator (integrand);
  }

  /**
   * Single step an area computation with a higher number of intervals.
   */
  private void step ()
  {
    // Draw the function plot.
    clear ();
    plotFunction ();
    nText.setText (Integer.toString (intervals));

    final float h = (TO_LIMIT - FROM_LIMIT) / intervals; // interval width
    final float prevArea = area;
    area = integrator.integrate (FROM_LIMIT, TO_LIMIT, intervals);

    // Show the trapezoidal regions.
    for (int i = 0; i < intervals; ++i)
    {
      showRegion (i, h);
    }

    intervals *= 2;
    areaText.setText (Float.toString (area));

    // Stop if exceeded the maximum number of intervals
    // or if the computed area is no longer changing.
    final float ratio = Math.abs ((area - prevArea) / area);
    if ((intervals > MAX_INTERVALS) || (ratio < TOLERANCE))
    {
      actionButton1.setEnabled (false);
    }

    // Redraw the function plot.
    plotFunction ();
  }

  /**
   * Display the ith region by coloring it.
   * 
   * @param i
   *        the value of i
   * @param h
   *        the width of the region
   */
  private void showRegion (final int i, final float h)
  {
    if (algorithm == TRAPEZOIDAL)
    {
      showTrapezoidalRegion (i, h);
    }
    else
    {
      showParabolicRegion (i, h);
    }
  }

  /**
   * Display the ith trapezoidal region by coloring it.
   * 
   * @param i
   *        the value of i
   * @param h
   *        the width of the region
   */
  private void showTrapezoidalRegion (final int i, final float h)
  {
    final float x1 = FROM_LIMIT + i * h; // left bound
    final float x2 = x1 + h; // right bound
    final float y1 = integrand.at (x1); // integrand value at left bound
    final float y2 = integrand.at (x2); // integrand value at right bound

    // Plot properties.
    final float xMin = plotProps.getXMin ();
    final float yMax = plotProps.getYMax ();
    final float xDelta = plotProps.getXDelta ();
    final float yDelta = plotProps.getYDelta ();

    // Convert bounds to rows and columns.
    final int c1 = Math.round ((x1 - xMin) / xDelta);
    final int c2 = Math.round ((x2 - xMin) / xDelta);
    final int r0 = Math.round (yMax / yDelta);
    final int r1 = Math.round ((yMax - y1) / yDelta);
    final int r2 = Math.round ((yMax - y2) / yDelta);

    final int cs[] = { c1, c1, c2, c2 };
    final int rs[] = { r0, r1, r2, r0 };

    // Alternate colors.
    final Color color = ((i & 1) == 0) ? COLOR_0 : COLOR_1;
    fillPolygon (cs, rs, 4, color);
  }

  /**
   * Display the ith parabolic region by coloring it.
   * 
   * @param pi
   *        the value of i
   * @param ph
   *        the width of the region
   */
  private void showParabolicRegion (final int pi, final float ph)
  {
    // Split the region in half.
    int i = pi;
    float h = ph;
    h /= 2;
    i *= 2;

    final float x1 = FROM_LIMIT + i * h; // left bound
    final float x2 = x1 + h; // middle
    final float x3 = x2 + h; // right bound
    final float y1 = integrand.at (x1); // integrand value at left bound
    final float y2 = integrand.at (x2); // integrand value at the middle
    final float y3 = integrand.at (x3); // integrand value at right bound

    // Interpolation parabola.
    final InterpolationPolynomial parabola = new InterpolationPolynomial (new DataPoint [] { new DataPoint (x1, y1),
                                                                                            new DataPoint (x2, y2),
                                                                                            new DataPoint (x3, y3) });

    // Plot properties.
    final float xMin = plotProps.getXMin ();
    final float yMax = plotProps.getYMax ();
    final float xDelta = plotProps.getXDelta ();
    final float yDelta = plotProps.getYDelta ();

    // Convert bounds to rows and columns.
    final int c1 = Math.round ((x1 - xMin) / xDelta);
    final int c3 = Math.round ((x3 - xMin) / xDelta);
    final int r0 = Math.round (yMax / yDelta);
    final int r1 = Math.round ((yMax - y1) / yDelta);
    final int r3 = Math.round ((yMax - y3) / yDelta);

    final int size = c3 - c1 + 3;
    final int cs[] = new int [size];
    final int rs[] = new int [size];

    cs[0] = cs[1] = c1;
    cs[size - 2] = cs[size - 1] = c3;
    rs[0] = rs[size - 1] = r0;
    rs[1] = r1;
    rs[size - 2] = r3;

    int j = 2;
    for (int c = c1 + 1; c <= c3; ++c)
    {
      final float x = c * xDelta + xMin;
      final float y = parabola.at (x);
      final int r = Math.round ((yMax - y) / yDelta);

      cs[j] = c;
      rs[j] = r;
      ++j;
    }

    // Alternate colors.
    final Color color = ((i & 3) == 0) ? COLOR_0 : COLOR_1;
    fillPolygon (cs, rs, size, color);
  }

  // ------------------//
  // Method overrides //
  // ------------------//

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

    integrand.addDataPoint (new DataPoint (x, y));
  }

  /**
   * Button 1 action: Plot the function or single step an area computation.
   */
  @Override
  protected void doButton1Action ()
  {
    if (isPlotted)
    {
      step ();
    }
    else
    {
      plot ();
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
  }

  /**
   * Return whether or not it is OK to add another data point.
   * 
   * @return true if OK, otherwise false
   */
  @Override
  public boolean dotOK ()
  {
    return !isPlotted;
  }

  /**
   * Return whether or not it's OK to plot the function.
   * 
   * @return true if OK, otherwise false
   */
  @Override
  protected boolean plotOK ()
  {
    return plotOK;
  }

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
    return integrand.at (x);
  }

  /**
   * Reset.
   */
  @Override
  protected void reset ()
  {
    super.reset ();

    intervals = 1;
    area = 0;
    isPlotted = false;
    plotOK = false;

    integrand.reset ();

    setHeaderLabel ("First plot two to ten points with mouse clicks, " + "then press the 'Plot integrand' button.",
                    Color.blue);

    nLabel.setText ("# points");
    areaText.setText (" ");

    actionButton1.setLabel ("Plot integrand");
    actionButton1.setEnabled (false);
  }
}
