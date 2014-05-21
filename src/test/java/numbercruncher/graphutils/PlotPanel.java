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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * The panel that draws a set of axes, and plots points and lines.
 */
class PlotPanel extends Panel
{
  private static final int TICK_SIZE = 5;

  /** image buffer */
  private Image buffer;
  /** buffer graphics context */
  private Graphics bg;
  /** font metrics */
  private FontMetrics fontMetrics;

  /** label font */
  private final Font labelFont = new Font ("Dialog", 1, 10);

  /** width */
  private int m_nW;
  /** height */
  private int m_nH;
  /** x-axis row */
  private int xAxisRow;
  /** y-axis column */
  private int yAxisCol;
  /** minimum x value */
  private float xMin;
  /** maximum x value */
  private float xMax;
  /** minimum y value */
  private float yMin;
  /** maximum y value */
  private float yMax;
  /** x delta per pixel */
  private float xDelta;
  /** y delta per pixel */
  private float yDelta;

  /** array of plot endpoint columns */
  private int cs[];
  /** array of plot endpoint rows */
  private int rs[];
  /** endpoint array index */
  private int m_nK;

  /** parent graph panel */
  private final GraphPanel m_aGraphPanel;

  /**
   * Constructor.
   * 
   * @param graphPanel
   *        the parent graph panel
   */
  PlotPanel (final GraphPanel graphPanel)
  {
    this.m_aGraphPanel = graphPanel;
    setBackground (Color.white);

    // Plot mouse event handlers.
    addMouseListener (new MouseAdapter ()
    {
      @Override
      public void mouseClicked (final MouseEvent ev)
      {
        PlotPanel.this.m_aGraphPanel.mouseClickedOnPlot (ev); // callback
      }

      @Override
      public void mousePressed (final MouseEvent ev)
      {
        PlotPanel.this.m_aGraphPanel.mousePressedOnPlot (ev); // callback
      }

      @Override
      public void mouseReleased (final MouseEvent ev)
      {
        PlotPanel.this.m_aGraphPanel.mouseReleasedOnPlot (ev); // callback
      }
    });
    addMouseMotionListener (new MouseMotionAdapter ()
    {
      @Override
      public void mouseDragged (final MouseEvent ev)
      {
        PlotPanel.this.m_aGraphPanel.mouseDraggedOnPlot (ev); // callback
      }
    });
  }

  /**
   * Initialize the plot with its properties.
   * 
   * @param plotProps
   *        the plot properties
   */
  void initPlot (final PlotProperties plotProps)
  {
    if (plotProps == null)
      return;

    // Compute the plot properties.
    final Dimension size = getSize ();
    plotProps.compute (size);

    // Extract the plot properties.
    m_nW = plotProps.getWidth ();
    m_nH = plotProps.getHeight ();
    xAxisRow = plotProps.getXAxisRow ();
    yAxisCol = plotProps.getYAxisColumn ();
    xMin = plotProps.getXMin ();
    xMax = plotProps.getXMax ();
    yMin = plotProps.getYMin ();
    yMax = plotProps.getYMax ();
    xDelta = plotProps.getXDelta ();
    yDelta = plotProps.getYDelta ();

    // Create the image buffer and get its graphics context.
    buffer = createImage (m_nW, m_nH);
    bg = buffer.getGraphics ();

    bg.setFont (labelFont);
    fontMetrics = bg.getFontMetrics ();
  }

  /**
   * Set paint mode.
   */
  void setPainMode ()
  {
    bg.setPaintMode ();
  }

  /**
   * Set XOR mode.
   */
  void setXORMode ()
  {
    bg.setXORMode (Color.white);
  }

  /**
   * Draw the axes onto the image buffer.
   */
  void drawAxes ()
  {
    if (bg == null)
      return;

    bg.setPaintMode ();
    bg.setColor (Color.black);

    // X axis.
    if ((xAxisRow >= 0) && (xAxisRow < m_nH))
    {
      bg.drawLine (0, xAxisRow, m_nW, xAxisRow);
    }

    // X axis ticks.
    for (int i = Math.round (xMin); i <= Math.round (xMax); ++i)
    {
      final int c = Math.round ((i - xMin) / xDelta);
      bg.drawLine (c, xAxisRow - TICK_SIZE, c, xAxisRow + TICK_SIZE);

      if (i != 0)
      {
        final String str = Integer.toString (i);
        final int w = fontMetrics.stringWidth (str);
        final int x = c - w / 2;
        final int y = xAxisRow + TICK_SIZE + fontMetrics.getAscent ();
        bg.drawString (str, x, y);
      }
    }

    // Y axis.
    if ((yAxisCol >= 0) && (yAxisCol < m_nW))
    {
      bg.drawLine (yAxisCol, 0, yAxisCol, m_nH);
    }

    // Y axis ticks.
    for (int i = Math.round (yMin); i <= Math.round (yMax); ++i)
    {
      final int r = Math.round ((yMax - i) / yDelta);
      bg.drawLine (yAxisCol - TICK_SIZE, r, yAxisCol + TICK_SIZE, r);

      if (i != 0)
      {
        final String str = Integer.toString (i);
        final int w = fontMetrics.stringWidth (str);
        final int x = yAxisCol - TICK_SIZE - w;
        final int y = r + fontMetrics.getAscent () / 2;
        bg.drawString (str, x, y);
      }
    }

    repaint ();
  }

  /**
   * Draw the X=Y line.
   */
  void drawXequalsY ()
  {
    if (bg == null)
      return;

    bg.setPaintMode ();
    bg.setColor (Color.black);

    final float p1 = Math.max (xMin, yMin);
    final int c1 = Math.round ((p1 - xMin) / xDelta);
    final int r1 = Math.round ((yMax - p1) / yDelta);
    final float p2 = Math.min (xMax, yMax);
    final int c2 = Math.round ((p2 - xMin) / xDelta);
    final int r2 = Math.round ((yMax - p2) / yDelta);

    bg.drawLine (c1, r1, c2, r2);
    repaint ();
  }

  /**
   * Start a function plot.
   * 
   * @param color
   *        the plot color
   */
  void startPlot (final Color color)
  {
    if (bg == null)
      return;
    bg.setColor (color);

    cs = new int [m_nW];
    rs = new int [m_nW];
    m_nK = 0;
  }

  /**
   * Plot a function point at column c.
   * 
   * @param c
   *        the column
   * @param y
   *        the function value
   */
  void plot (final int c, final float y)
  {
    if (bg == null)
      return;

    // Plot y if it's within range.
    if ((y >= yMin) && (y <= yMax))
    {
      final int r = Math.round ((yMax - y) / yDelta);
      cs[m_nK] = c;
      rs[m_nK] = r;
      ++m_nK;
    }

    // Otherwise draw what we have so far.
    else
      if (m_nK > 0)
      {
        bg.drawPolyline (cs, rs, m_nK);
        m_nK = 0;
      }
  }

  /**
   * End a function plot.
   */
  void endPlot ()
  {
    if (bg == null)
      return;

    // Draw the rest of the plot.
    if (m_nK > 0)
      bg.drawPolyline (cs, rs, m_nK);
    drawPlot ();
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
  void plotPoint (final int x, final int y, final Color color)
  {
    if (bg == null)
      return;
    bg.setColor (color);

    bg.drawLine (x, y, x, y);
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
  void plotLine (final int x1, final int y1, final int x2, final int y2, final Color color)
  {
    if (bg == null)
      return;
    bg.setColor (color);

    bg.drawLine (x1, y1, x2, y2);
    drawPlot ();
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
  void plotLines (final int xs1[], final int ys1[], final int xs2[], final int ys2[], final int k, final Color color)
  {
    if (bg == null)
      return;
    bg.setColor (color);

    for (int i = 0; i < k; ++i)
    {
      bg.drawLine (xs1[i], ys1[i], xs2[i], ys2[i]);
    }

    drawPlot ();
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
  void plotRectangle (final int x, final int y, final int w, final int h, final Color color)
  {
    if (bg == null)
      return;
    bg.setColor (color);

    bg.drawRect (x, y, w, h);
    drawPlot ();
  }

  void plotDot (final int x, final int y, final int w, final Color color)
  {
    if (bg == null)
      return;
    bg.setColor (color);

    final int halfW = w / 2;
    bg.fillOval (x - halfW, y - halfW, w, w);

    drawPlot ();
  }

  void plotDots (final Point data[], final int k, final int w, final Color color)
  {
    if (bg == null)
      return;
    bg.setColor (color);

    final int halfW = w / 2;
    for (int i = 0; i < k; ++i)
    {
      bg.fillOval (data[i].x - halfW, data[i].y - halfW, w, w);
    }

    drawPlot ();
  }

  protected void fillPolygon (final int xs[], final int ys[], final int k, final Color color)
  {
    if (bg == null)
      return;
    bg.setColor (color);

    bg.fillPolygon (xs, ys, k);
    drawPlot ();
  }

  /**
   * Display the image buffer.
   */
  void drawPlot ()
  {
    if (buffer == null)
      return;

    final Graphics g = this.getGraphics ();
    if (g != null)
      g.drawImage (buffer, 0, 0, null);
  }

  /**
   * Update the display without repainting the background.
   * 
   * @param g
   *        the graphics context
   */
  @Override
  public void update (final Graphics g)
  {
    paint (g);
  }

  /**
   * Display the image buffer.
   * 
   * @param g
   *        the graphics context
   */
  @Override
  public void paint (final Graphics g)
  {
    drawPlot ();
  }
}
