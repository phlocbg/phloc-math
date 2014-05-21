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
package numbercruncher.program16_4;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.MouseEvent;

import numbercruncher.graphutils.GraphPanel;
import numbercruncher.graphutils.PlotProperties;
import numbercruncher.mathutils.Complex;

public class MandelbrotSetPanel extends GraphPanel
{
  /** control panel */
  private final Panel fractalControlPanel = new Panel ();
  /** bounds panel */
  @SuppressWarnings ("unused")
  private final Panel boundsPanel = new Panel ();

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
  MandelbrotSetPanel ()
  {
    super ("Mandelbrot Set", plotProps, false);

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
    fractalControlPanel.setLayout (new GridLayout (0, 4, 2, 2));
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

  /**
   * Start the plot.
   */
  @SuppressWarnings ("unused")
  private void startPlot ()
  {
    stopPlot (); // stop currently running plot

    n = 0;
    draw ();
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
    // First time or not?
    if (n == 0)
    {
      setProperties ();
    }
    else
    {
      getProperties ();
    }
    displayBounds ();

    // Start a new plot thread.
    stopFlag = false;
    plotThread = new PlotThread ();
    plotThread.start ();

    ++n;
  }

  /**
   * Stop the plot.
   */
  private void stopPlot ()
  {
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

  private static final int MAX_ITERS = 32;
  private static final int ESCAPE_MODULUS = 2;

  /**
   * Graph thread class that iterates z^2 + c as c varies over each point in the
   * complex plane bounded by the rectangle xMin, xMax, yMin, yMax.
   */
  private class PlotThread extends Thread
  {
    @Override
    public void run ()
    {
      // Loop over each graph panel pixel.
      for (int row = 0; row < h; ++row)
      {
        final float y0 = yMax - row * delta; // row => y0

        for (int col = 0; col < w; ++col)
        {
          final float x0 = xMin + col * delta; // col => x0
          final Complex c = new Complex (x0, y0); // z = x0 + y0i
          Complex z = new Complex (0, 0);

          if (stopFlag)
            return;

          boolean escaped = false;
          int iters = 0;
          float modulus;

          // Iterate z^2 + c, keeping track of the
          // iteration count.
          do
          {
            z = z.multiply (z).add (c);
            modulus = z.modulus ();
            escaped = modulus > ESCAPE_MODULUS;
          } while ((++iters < MAX_ITERS) && (!escaped));

          // Escaped: Set the shade of gray based on the
          // number of iterations needed to escape. The
          // more iterations, the darker the shade.
          if (escaped)
          {
            int k = 255 - (255 * iters) / MAX_ITERS;
            k = Math.min (k, 240);
            plotPoint (col, row, new Color (k, k, k));
          }

          // No escape: Set the colors based on the modulus.
          else
          {
            final int m = ((int) (100 * modulus)) / ESCAPE_MODULUS + 1;
            final int r = (101 * m) & 255;
            final int g = (149 * m) & 255;
            final int b = (199 * m) & 255;
            plotPoint (col, row, new Color (r, g, b));
          }
        }

        // Draw a row of the graph.
        drawPlot ();
        yield ();
      }
    }
  }
}
