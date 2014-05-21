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
package numbercruncher.pointutils;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import numbercruncher.graphutils.GraphPanel;
import numbercruncher.graphutils.PlotProperties;

/**
 * The base panel for all demo panels that allow the user to set data points
 * with the mouse.
 */
public abstract class UserPointPanel extends GraphPanel
{
  private static final float INIT_X_MIN = -0.5f;
  private static final float INIT_X_MAX = 11.5f;
  private static final float INIT_Y_MIN = -0.75f;
  private static final float INIT_Y_MAX = 9.5f;

  /** control panel */
  protected Panel controlPanel = new Panel ();
  /** n label */
  protected Label nLabel = new Label ();
  /** n text */
  protected Label nText = new Label ();
  /** action button 1 */
  protected Button actionButton1 = new Button ();
  /** action button 2 */
  protected Button actionButton2 = new Button ();

  /** count of data points */
  protected int n;

  /** array of data point coordinates */
  private final Point data[];
  /** maximum number of data points */
  private final int m_nMaxPoints;
  /** true if OK to set data points */
  private boolean dotOK = true;

  /** initial plot properties */
  protected static PlotProperties plotProps = new PlotProperties (INIT_X_MIN, INIT_X_MAX, INIT_Y_MIN, INIT_Y_MAX);

  /**
   * Constructor.
   * 
   * @param maxPoints
   *        the maximum number of data points
   * @param nLabelText
   *        the text of the n label
   * @param actionButton1Label
   *        the label for action button 1
   * @param actionButton2Label
   *        the label for action button 2
   */
  protected UserPointPanel (final int maxPoints,
                            final String nLabelText,
                            final String actionButton1Label,
                            final String actionButton2Label)
  {
    super (" ", plotProps);
    this.m_nMaxPoints = maxPoints;

    final Font labelFont = getLabelFont ();
    final Font textFont = getTextFont ();

    // Controls.
    nLabel.setFont (labelFont);
    nLabel.setAlignment (Label.RIGHT);
    nLabel.setText (nLabelText);
    nText.setFont (textFont);
    nText.setAlignment (Label.LEFT);
    nText.setText (" ");
    actionButton1.setLabel (actionButton1Label);
    actionButton2.setLabel (actionButton2Label);

    // Action button 1 handler.
    actionButton1.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        doButton1Action ();
      }
    });

    // Action button 2 handler.
    actionButton2.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        doButton2Action ();
      }
    });

    // Data points.
    data = new Point [maxPoints];
  }

  /**
   * Perform the dot action. (Do nothing here.)
   * 
   * @param r
   *        the dot's row
   * @param c
   *        the dot's column
   */
  protected void doDotAction (final int r, final int c)
  {}

  /**
   * Perform the button 1 action 1. (Do nothing here.)
   */
  protected void doButton1Action ()
  {}

  /**
   * Perform the button 2 action 2. (Do nothing here.)
   */
  protected void doButton2Action ()
  {}

  /**
   * Mouse pressed on the plot: Add an interpolation point.
   * 
   * @param ev
   *        the mouse event
   */
  @Override
  public void mousePressedOnPlot (final MouseEvent ev)
  {
    if (!dotOK ())
      return;
    if (n == m_nMaxPoints)
      return;

    final int c = ev.getX ();
    final int r = ev.getY ();

    // Record the new data point.
    data[n] = new Point (c, r);

    ++n;
    drawDots ();
    nText.setText (Integer.toString (n));
    doDotAction (r, c);
  }

  /**
   * Return true if it is OK to add another data point.
   * 
   * @return true or false
   */
  public boolean dotOK ()
  {
    return dotOK;
  }

  /**
   * Draw the data points.
   */
  protected void drawDots ()
  {
    if (n > 0)
      plotDots (data, n, 8, Color.red);
  }

  /**
   * Reset.
   */
  protected void reset ()
  {
    n = 0;
    dotOK = true;

    // Initialize n in the control panel.
    nText.setText (Integer.toString (n));
  }

  // ------------------//
  // Method overrides //
  // ------------------//

  /**
   * Notification that the plot bounds changed. Redraw the panel.
   */
  @Override
  public void plotBoundsChanged ()
  {
    reset ();
    draw ();
  }

  /**
   * Notification that the panel was resized. (Callback from DemoFrame.)
   */
  @Override
  public void panelResized ()
  {
    reset ();
    draw ();
  }

  /**
   * Notification that a user input error occurred. Disable the action1 button
   * and enable the action2 button.
   */
  @Override
  protected void userErrorOccurred ()
  {
    dotOK = false;
    actionButton1.setEnabled (false);
    actionButton2.setEnabled (true);
  }

  /**
   * Redraw the dots.
   */
  @Override
  public void draw ()
  {
    super.draw ();
    drawDots ();

    // Set the focus to an action button to avoid spurious
    // repaints caused by the text losing focus later.
    actionButton2.requestFocus ();
  }
}
