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
package numbercruncher.program16_1;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.MouseEvent;

import numbercruncher.graphutils.GraphPanel;
import numbercruncher.graphutils.PlotProperties;

public class BifurcationPanel extends GraphPanel
{
  /** control panel */
  private final Panel fractalControlPanel = new Panel ();

  /** x-minimum label */
  private final Label xMinLabel = new Label ("Min x:");
  /** x-maximum label */
  private final Label xMaxLabel = new Label ("Max x:");
  /** y-minimum label */
  private final Label yMinLabel = new Label ("Min y:");
  /** y-maximum label */
  private final Label yMaxLabel = new Label ("Max y:");

  /** x-minimum text */
  private final Label xMinText = new Label ();
  /** x-maximum text */
  private final Label xMaxText = new Label ();
  /** y-minimum text */
  private final Label yMinText = new Label ();
  /** y-maximum text */
  private final Label yMaxText = new Label ();

  /** iteration counter */
  private int n = 0;
  /** fractal plot thread */
  private PlotThread plotThread = null;

  /** panel width */
  private int w;
  /** panel height */
  private int h;
  /** minimum x value */
  private float xMin;
  /** maximum x value */
  private float xMax;
  /** minimum y value */
  private float yMin;
  /** maximum y value */
  private float yMax;
  /** delta per pixel */
  private float delta;
  /** previous minimum x */
  private float oldXMin;
  /** previous maximun y */
  private float oldYMax;

  /** zoom rectangle upper left row */
  private int r1;
  /** zoom rectangle upper left column */
  private int c1;
  /** zoom rectangle bottom right row */
  private int r2;
  /** zoom rectangle bottom right column */
  private int c2;

  /** true to stop run thread */
  private boolean stopFlag = false;

  /** initial plot properties */
  private static PlotProperties plotProps = new PlotProperties (0, 0, 0, 0);

  /**
   * Constructor.
   */
  BifurcationPanel ()
  {
    super ("Birfucation Diagram of x^2 + c and the Orbits of x=0", plotProps, false);

    final Font labelFont = getLabelFont ();
    final Font textFont = getTextFont ();

    // Fractal controls.
    xMinLabel.setFont (labelFont);
    xMinLabel.setAlignment (Label.RIGHT);
    xMinText.setFont (textFont);
    xMaxLabel.setFont (labelFont);
    xMaxLabel.setAlignment (Label.RIGHT);
    xMaxText.setFont (textFont);
    yMinLabel.setFont (labelFont);
    yMinLabel.setAlignment (Label.RIGHT);
    yMinText.setFont (textFont);
    yMaxLabel.setFont (labelFont);
    yMaxLabel.setAlignment (Label.RIGHT);
    yMaxText.setFont (textFont);

    // Fractal control panel.
    fractalControlPanel.setBackground (Color.lightGray);
    fractalControlPanel.setLayout (new GridLayout (0, 4, 5, 2));
    fractalControlPanel.add (xMinLabel);
    fractalControlPanel.add (xMinText);
    fractalControlPanel.add (yMinLabel);
    fractalControlPanel.add (yMinText);
    fractalControlPanel.add (xMaxLabel);
    fractalControlPanel.add (xMaxText);
    fractalControlPanel.add (yMaxLabel);
    fractalControlPanel.add (yMaxText);
    addDemoControls (fractalControlPanel);
  }

  /**
   * Get the plot properties.
   */
  private void getProperties ()
  {
    w = plotProps.getWidth ();
    h = plotProps.getHeight ();
    delta = plotProps.getXDelta ();
    xMin = plotProps.getXMin ();
    xMax = plotProps.getXMax ();
    yMin = yMax - h * delta;
    yMax = plotProps.getYMax ();
  }

  /**
   * Set the plot properties.
   */
  private void setProperties ()
  {
    w = plotProps.getWidth ();
    h = plotProps.getHeight ();
    delta = 0.01f;
    xMin = -(w / 2) * delta;
    yMin = -(h / 2) * delta;
    xMax = -xMin;
    yMax = -yMin;

    plotProps.update (xMin, xMax, yMin, yMax);
  }

  /**
   * Display the current plot bounds in the text controls.
   */
  private void displayBounds ()
  {
    xMinText.setText (Float.toString (xMin));
    xMaxText.setText (Float.toString (xMax));
    yMinText.setText (Float.toString (yMin));
    yMaxText.setText (Float.toString (yMax));
  }

  // ------------------//
  // Method overrides //
  // ------------------//

  /**
   * Plot a function.
   */
  @Override
  protected void plotFunction ()
  {
    // First iteration or not?
    if (n == 0)
      setProperties ();
    else
      getProperties ();

    displayBounds ();

    // Stop the currently-running thread.
    if ((plotThread != null) && (plotThread.isAlive ()))
    {
      stopFlag = true;

      try
      {
        plotThread.join ();
      }
      catch (final Exception ex)
      {}
    }

    // Start a new plot thread.
    stopFlag = false;
    plotThread = new PlotThread ();
    plotThread.start ();

    ++n;
  }

  /**
   * Mouse pressed on the plot: Start the zoom rectangle.
   */
  @Override
  public void mousePressedOnPlot (final MouseEvent ev)
  {
    if ((plotThread != null) && (plotThread.isAlive ()))
      return;

    oldXMin = xMin;
    oldYMax = yMax;

    // Starting corner.
    c1 = ev.getX ();
    r1 = ev.getY ();

    // Ending corner.
    c2 = -1;
    r2 = -1;

    setXORMode ();
  }

  /**
   * Mouse dragged on the plot: Track the mouse to draw the zoom rectangle.
   */
  @Override
  public void mouseDraggedOnPlot (final MouseEvent ev)
  {
    if ((plotThread != null) && (plotThread.isAlive ()))
      return;

    // Erase the previous rectangle.
    if ((c2 != -1) && (r2 != -1))
    {
      plotRectangle (Math.min (c1, c2), Math.min (r1, r2), Math.abs (c1 - c2), Math.abs (r1 - r2), Color.black);
    }

    // Current ending corner.
    c2 = ev.getX ();
    r2 = ev.getY ();

    // Calculate and display new zoom area bounds.
    xMin = oldXMin + delta * Math.min (c1, c2);
    xMax = oldXMin + delta * Math.max (c1, c2);
    yMin = oldYMax - delta * Math.max (r1, r2);
    yMax = oldYMax - delta * Math.min (r1, r2);
    displayBounds ();

    // Draw the new rectangle.
    plotRectangle (Math.min (c1, c2), Math.min (r1, r2), Math.abs (c1 - c2), Math.abs (r1 - r2), Color.black);
  }

  /**
   * Mouse released on the plot: End the zoom rectangle and plot the zoomed
   * area.
   */
  @Override
  public void mouseReleasedOnPlot (final MouseEvent ev)
  {
    if ((plotThread != null) && (plotThread.isAlive ()))
      return;

    // Draw the rectangle.
    if ((c2 != -1) && (r2 != -1))
    {
      plotRectangle (Math.min (c1, c2), Math.min (r1, r2), Math.abs (c1 - c2), Math.abs (r1 - r2), Color.black);
    }

    // Plot the area in the rectangle.
    plotProps.update (xMin, xMax, yMin, yMax);
    draw ();
  }

  // -----------//
  // Animation //
  // -----------//

  private static final Color LIGHT_GRAY = new Color (240, 240, 240);

  private static final int SKIP_ITERS = 50;
  private static final int MAX_ITERS = 200;

  /**
   * Graph thread class that creates a bifurcation diagram of the function f(x)
   * = x^2 + c. For each value of c, it plots the values of the orbit of x=0.
   */
  private class PlotThread extends Thread
  {
    @Override
    public void run ()
    {
      // Loop over the horizontal axis.
      for (int col = 0; col < w; ++col)
      {
        final float c = xMin + col * delta;
        float x = 0;

        // Light gray backgound;
        for (int row = 0; row < h; ++row)
        {
          if (stopFlag)
            return;
          plotPoint (col, row, LIGHT_GRAY);
        }

        for (int i = 0; i < MAX_ITERS; ++i)
        {
          if (stopFlag)
            return;
          x = x * x + c;

          if (i >= SKIP_ITERS)
          {
            final float y = x;
            final int row = Math.round ((yMax - y) / delta);
            plotPoint (col, row, Color.blue);
          }
        }

        // Draw a row of the graph.
        drawPlot ();
        yield ();
      }
    }
  }
}
