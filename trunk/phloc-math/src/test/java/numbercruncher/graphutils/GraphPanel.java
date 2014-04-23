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
package numbercruncher.graphutils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * The base panel for all graph demo panels.
 */
public abstract class GraphPanel extends Panel implements IDemoPanel
{
  public static final Color MAROON = new Color (128, 0, 0);

  /** header panel */
  private HeaderPanel headerPanel;
  /** plot panel */
  private final PlotPanel plotPanel = new PlotPanel (this);
  /** control panel */
  private final Panel controlPanel = new Panel ();
  /** bounds panel */
  private PlotBoundsPanel boundsPanel;

  /** true to set XOR mode */
  private final boolean m_bXorMode;
  /** true to draw axes */
  private final boolean m_bDrawAxes;
  /** true to draw X=Y line */
  private final boolean m_bDrawXequalsY;

  /** plot properties */
  private PlotProperties m_aPlotProps;

  /** label font */
  private final Font labelFont = new Font ("Dialog", 1, 12);
  /** text font */
  private final Font textFont = new Font ("Dialog", 0, 12);

  /**
   * Constructor.
   * 
   * @param functions
   *        the array of plottable functions
   * @param plotProps
   *        the plot properties
   * @param xorMode
   *        if true, set XOR mode
   * @param drawXequalsY
   *        if true, draw the X=Y line
   */
  protected GraphPanel (final IPlottable functions[],
                        final PlotProperties plotProps,
                        final boolean xorMode,
                        final boolean drawXequalsY)
  {
    this.m_aPlotProps = plotProps;
    this.m_bXorMode = xorMode;
    this.m_bDrawXequalsY = drawXequalsY;
    this.m_bDrawAxes = true;

    headerPanel = new HeaderPanel (functions, this);
    boundsPanel = new PlotBoundsPanel (this);

    init ();
  }

  /**
   * Constructor.
   * 
   * @param headerText
   *        the text to display in the header panel
   * @param plotProps
   *        the plot properties
   * @param drawAxes
   *        if true, draw axes
   */
  protected GraphPanel (final String headerText, final PlotProperties plotProps, final boolean drawAxes)
  {
    this.m_aPlotProps = plotProps;
    this.m_bXorMode = false;
    this.m_bDrawXequalsY = false;
    this.m_bDrawAxes = drawAxes;

    headerPanel = new HeaderPanel (headerText);
    init ();
  }

  /**
   * Constructor.
   * 
   * @param headerText
   *        the text to display in the header panel
   * @param plotProps
   *        the plot properties
   */
  protected GraphPanel (final String headerText, final PlotProperties plotProps)
  {
    this.m_aPlotProps = plotProps;
    this.m_bXorMode = false;
    this.m_bDrawXequalsY = false;
    this.m_bDrawAxes = true;

    headerPanel = new HeaderPanel (headerText);
    boundsPanel = new PlotBoundsPanel (this);
    init ();
  }

  /**
   * Constructor.
   * 
   * @param plotProps
   *        the plot properties
   */
  protected GraphPanel (final PlotProperties plotProps)
  {
    this.m_aPlotProps = plotProps;
    this.m_bXorMode = false;
    this.m_bDrawXequalsY = false;
    this.m_bDrawAxes = false;

    init ();
  }

  /**
   * Initialize the graph panel.
   */
  private void init ()
  {
    if (boundsPanel != null)
      boundsPanel.setTextFields (m_aPlotProps);

    // Control panel.
    controlPanel.setBackground (Color.lightGray);
    controlPanel.setLayout (new BorderLayout (5, 0));
    if (boundsPanel != null)
      controlPanel.add (boundsPanel, BorderLayout.WEST);

    // Graph panel.
    setLayout (new BorderLayout ());
    if (headerPanel != null)
      add (headerPanel, BorderLayout.NORTH);
    add (plotPanel, BorderLayout.CENTER);
    add (controlPanel, BorderLayout.SOUTH);
  }

  /**
   * Return the label font.
   * 
   * @return the font
   */
  protected Font getLabelFont ()
  {
    return labelFont;
  }

  /**
   * Return the text font.
   * 
   * @return the font
   */
  protected Font getTextFont ()
  {
    return textFont;
  }

  /**
   * Return the plot properties.
   * 
   * @return the plot properties
   */
  protected PlotProperties getPlotProperties ()
  {
    return m_aPlotProps;
  }

  /**
   * Set the plot properties.
   * 
   * @param plotProps
   *        the plot properties
   */
  protected void setPlotProperties (final PlotProperties plotProps)
  {
    this.m_aPlotProps = plotProps;
    boundsPanel.setTextFields (plotProps);
  }

  /**
   * Set the header image.
   * 
   * @param image
   *        the header image
   */
  protected void setHeaderImage (final Image image)
  {
    headerPanel.setImage (image);
  }

  /**
   * Set the header label text in the default black color.
   * 
   * @param text
   *        the header label text
   */
  protected void setHeaderLabel (final String text)
  {
    headerPanel.setLabel (text);
  }

  /**
   * Set the header label text in color.
   * 
   * @param text
   *        the header label text
   * @param color
   *        the color
   */
  protected void setHeaderLabel (final String text, final Color color)
  {
    headerPanel.setLabel (text, color);
  }

  /**
   * Set the function to plot.
   * 
   * @param function
   *        the function to set
   */
  protected void setFunction (final IPlottable function)
  {
    setPlotProperties (function.getPlotProperties ());
    headerPanel.setFunction (function);
  }

  /**
   * Set the header to display the function.
   * 
   * @param function
   *        the function to display
   */
  protected void setHeaderDisplay (final IPlottable function)
  {
    headerPanel.setFunction (function);
  }

  /**
   * Add a demo's controls to the control panel.
   * 
   * @param demoControlPanel
   *        the demo's control subpanel
   */
  protected void addDemoControls (final Panel demoControlPanel)
  {
    controlPanel.add (demoControlPanel, BorderLayout.CENTER);
  }

  /**
   * Process a user input error.
   * 
   * @param message
   *        the error message
   */
  protected void processUserError (final String message)
  {
    headerPanel.displayError (message);
    userErrorOccurred ();
  }

  // ------------------//
  // Plotting methods //
  // ------------------//

  /**
   * Clear the contents of the plot panel.
   */
  public void clear ()
  {
    // Get the plot bounds.
    if (boundsPanel != null)
    {
      boundsPanel.updatePlotProperties (m_aPlotProps);
    }

    // Draw the axes.
    plotPanel.initPlot (m_aPlotProps);
    if (m_bDrawAxes)
      plotPanel.drawAxes ();
    if (m_bDrawXequalsY)
      plotPanel.drawXequalsY ();
  }

  /**
   * Plot a function.
   */
  protected void plotFunction ()
  {
    startPlot (Color.blue);

    // Plot properties.
    final int w = m_aPlotProps.getWidth ();
    final float xMin = m_aPlotProps.getXMin ();
    final float deltaX = m_aPlotProps.getXDelta ();

    // Loop over each pixel of the x axis to plot the function value.
    for (int c = 0; c < w; ++c)
    {
      final float x = xMin + c * deltaX;
      final float y = valueAt (x);

      plot (c, y);
    }

    endPlot ();
  }

  /**
   * Plot the function.
   * 
   * @param plotFunction
   *        the function to plot
   */
  protected void plotFunction (final IPlottable plotFunction, final Color color)
  {
    startPlot (color);

    // Plot properties.
    final int w = m_aPlotProps.getWidth ();
    final float xMin = m_aPlotProps.getXMin ();
    final float deltaX = m_aPlotProps.getXDelta ();

    // Loop over each pixel of the x axis to plot the function value.
    for (int c = 0; c < w; ++c)
    {
      final float x = xMin + c * deltaX;
      final float y = plotFunction.at (x);

      plot (c, y);
    }

    endPlot ();
  }

  /**
   * Return whether or not it is OK to plot the function.
   * 
   * @return true or false
   */
  protected boolean plotOK ()
  {
    return true;
  }

  /**
   * Check to make sure the function is in bounds over the interval [a, b].
   * 
   * @param a
   *        the lower bound
   * @param b
   *        the upper bound
   * @throws Exception
   *         if it is not
   */
  protected void checkFunctionInBounds (final float a, final float b) throws Exception
  {
    // Plot properties.
    final float xMin = m_aPlotProps.getXMin ();
    final float yMin = m_aPlotProps.getYMin ();
    final float yMax = m_aPlotProps.getYMax ();
    final float deltaX = m_aPlotProps.getXDelta ();

    final int ca = Math.round ((a - xMin) / deltaX);
    final int cb = Math.round ((b - xMin) / deltaX);

    // Loop over each pixel of the x axis to check the function value.
    for (int c = ca; c < cb; ++c)
    {
      final float x = xMin + c * deltaX;
      final float y = valueAt (x);

      if ((y < yMin) || (y > yMax))
      {
        throw new Exception ("Function out of bounds over the " + "interval [" + a + ", " + b + "]");
      }
    }
  }

  /**
   * Return the value of the plottable function at x. (Return 0 here.)
   * 
   * @param x
   *        the value of x
   * @return the value of the function
   */
  protected float valueAt (final float x)
  {
    return 0;
  }

  /**
   * Start a function plot.
   * 
   * @param color
   *        the plot color
   */
  protected void startPlot (final Color color)
  {
    plotPanel.startPlot (color);
  }

  /**
   * Plot a function point at column c.
   * 
   * @param c
   *        the column
   * @param y
   *        the function value
   */
  protected void plot (final int c, final float y)
  {
    plotPanel.plot (c, y);
  }

  /**
   * End a function plot.
   */
  protected void endPlot ()
  {
    plotPanel.endPlot ();
  }

  /**
   * Plot a point.
   * 
   * @param x
   *        the x-coordinate of the point
   * @param y
   *        the y-coordinate of the point
   * @param color
   *        the point color
   */
  protected void plotPoint (final int x, final int y, final Color color)
  {
    plotPanel.plotPoint (x, y, color);
  }

  /**
   * Plot a line.
   * 
   * @param x1
   *        the x-coordinate of one end of the line
   * @param y1
   *        the y-coordinate of one end of the line
   * @param x2
   *        the x-coordinate of the other end of the line
   * @param y2
   *        the y-coordinate of the other end of the line
   * @param color
   *        the line color
   */
  protected void plotLine (final int x1, final int y1, final int x2, final int y2, final Color color)
  {
    plotPanel.plotLine (x1, y1, x2, y2, color);
  }

  /**
   * Plot multiple lines.
   * 
   * @param xs1
   *        the array of x-coordinates of one end of the lines
   * @param ys1
   *        the array of y-coordinates of one end of the lines
   * @param xs2
   *        the array of x-coordinates of the other end of the lines
   * @param ys2
   *        the array of y-coordinates of the other end of the lines
   * @param color
   *        the color of the lines
   */
  protected void plotLines (final int xs1[],
                            final int ys1[],
                            final int xs2[],
                            final int ys2[],
                            final int k,
                            final Color color)
  {
    plotPanel.plotLines (xs1, ys1, xs2, ys2, k, color);
  }

  /**
   * Plot a rectangle.
   * 
   * @param x
   *        the x-coordinate of the upper left corner
   * @param y
   *        the y-coordinate of the upper left corner
   * @param w
   *        the width
   * @param h
   *        the height
   * @param color
   *        the rectangle color
   */
  protected void plotRectangle (final int x, final int y, final int w, final int h, final Color color)
  {
    plotPanel.plotRectangle (x, y, w, h, color);
  }

  /**
   * Plot a dot.
   * 
   * @param x
   *        the x-coordinate of the dot center
   * @param y
   *        the y-coordinate of the dot center
   * @param w
   *        the dot width
   * @param color
   *        the dot color
   */
  protected void plotDot (final int x, final int y, final int w, final Color color)
  {
    plotPanel.plotDot (x, y, w, color);
  }

  /**
   * Plot multiple dots.
   * 
   * @param data
   *        the array of dot coordinates
   * @param k
   *        the number of dots
   * @param w
   *        the width
   * @param color
   *        the dot color
   */
  protected void plotDots (final Point data[], final int k, final int w, final Color color)
  {
    plotPanel.plotDots (data, k, w, color);
  }

  protected void fillPolygon (final int xs[], final int ys[], final int k, final Color color)
  {
    plotPanel.fillPolygon (xs, ys, k, color);
  }

  /**
   * Draw the plot.
   */
  protected void drawPlot ()
  {
    plotPanel.drawPlot ();
  }

  /**
   * @return the plot panel size
   */
  protected Dimension getPlotPanelSize ()
  {
    return plotPanel.getSize ();
  }

  /**
   * Set paint mode.
   */
  protected void setPaintMode ()
  {
    plotPanel.setPainMode ();
  }

  /**
   * Set XOR mode.
   */
  protected void setXORMode ()
  {
    plotPanel.setXORMode ();
  }

  // --------------------------//
  // DemoPanel implementation //
  // --------------------------//

  /**
   * Initialize the demo. (Callback from applet. Do nothing here.)
   */
  public void initializeDemo ()
  {}

  /**
   * Close down the demo. (Callback from applet. Do nothing here.)
   */
  public void closeDemo ()
  {}

  /**
   * Draw the contents of the plot panel.
   */
  public void draw ()
  {
    clear ();
    if (plotOK ())
      plotFunction ();

    // Set XOR paint mode if desired.
    if (m_bXorMode)
      plotPanel.setXORMode ();
  }

  /**
   * Notification that the panel was resized. (Callback from DemoFrame.)
   */
  public void panelResized ()
  {
    draw ();
  }

  // ----------------//
  // Event handlers //
  // ----------------//

  /**
   * Do the header action. (Callback from the header panel. Do nothing here.)
   */
  public void doHeaderAction ()
  {}

  /**
   * Mouse clicked event handler. (Callback from the plot panel. Do nothing
   * here.)
   * 
   * @param ev
   *        the mouse event
   */
  public void mouseClickedOnPlot (final MouseEvent ev)
  {}

  /**
   * Mouse pressed event handler. (Callback from the plot panel. Do nothing
   * here.)
   * 
   * @param ev
   *        the mouse event
   */
  public void mousePressedOnPlot (final MouseEvent ev)
  {}

  /**
   * Mouse released event handler. (Callback from the plot panel. Do nothing
   * here.)
   * 
   * @param ev
   *        the mouse event
   */
  public void mouseReleasedOnPlot (final MouseEvent ev)
  {}

  /**
   * Mouse dragged event handler. (Callback from the plot panel. Do nothing
   * here.)
   * 
   * @param ev
   *        the mouse event
   */
  public void mouseDraggedOnPlot (final MouseEvent ev)
  {}

  /**
   * Choose a function. (Callback from the function frame. Do nothing here.)
   * 
   * @param index
   */
  public void chooseFunction (final int index)
  {}

  /**
   * Notification that the plot bounds changed. (Callback from the plot bounds
   * panel. Do nothing here.)
   */
  public void plotBoundsChanged ()
  {}

  /**
   * Notification that a user input error occurred. (Do nothing here.)
   */
  protected void userErrorOccurred ()
  {}
}
