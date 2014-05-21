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
package numbercruncher.program14_1;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import numbercruncher.graphutils.GraphPanel;
import numbercruncher.graphutils.PlotProperties;
import numbercruncher.mathutils.RandomNormal;
import numbercruncher.randomutils.Buckets;

/**
 * The demo panel for the Random Normal program and applet.
 */
public class RandomNormalPanel extends GraphPanel
{
  private static final String RUN = "Run";
  private static final String PAUSE = "Pause";
  private static final String RESET = "Reset";

  private static final String HEADER_LABEL = "Normally-Distributed Random Numbers";

  private static final String ALGORITHM_LABELS[] = { "Central Limit Theorem", "Polar Algorithm", "Ratio Algorithm", };

  private static final PlotProperties INIT_PLOT_PROPS = new PlotProperties (-0.5f, 12.5f, -0.75f, 7.5f);

  private static final float INIT_MEAN = 6;
  private static final float INIT_STDDEV = 2;

  private static final int BUCKET_COUNT = 100;
  private static final int GROUP_SIZE = 100;
  private static final int BAR_FACTOR = 10;

  private static final int CENTRAL = 0;
  private static final int POLAR = 1;
  private static final int RATIO = 2;

  /**
   * control panel
   */
  private final Panel controlPanel = new Panel ();
  /** mean label */
  private final Label meanLabel = new Label ("Mean:");
  /** mean text */
  private final TextField meanText = new TextField (Float.toString (INIT_MEAN));
  /**
   * standard deviation label
   */
  private final Label stddevLabel = new Label ("Std dev:");
  /**
   * standard deviation text
   */
  private final TextField stddevText = new TextField (Float.toString (INIT_STDDEV));
  /** values label */
  private final Label valuesLabel = new Label ("# Values:");
  /** values text */
  private final Label valuesText = new Label ("0");
  /**
   * algorithm label
   */
  private final Label algorithmLabel = new Label ("Algorithm:");
  /**
   * algorithm choice
   */
  private final Choice algorithmChoice = new Choice ();
  /** run button */
  private final Button runButton = new Button (RUN);
  /** reset button */
  private final Button resetButton = new Button (RESET);

  /** run thread */
  private RunThread runThread;
  /** true if pause button pressed */
  private boolean paused = false;
  /** true if done */
  private boolean done = false;

  /** plot properties */
  private PlotProperties plotProps;
  /** plot width */
  private int w;
  /** bar width */
  private int bWidth;
  /** x-axis row */
  private int xAxisRow;
  /** min plot x */
  private float xMin;
  /** max plot x */
  private float xMax;
  /** max plot y */
  private float m_fYMax;
  /** plot x delta */
  private float xDelta;
  /** plot y delta */
  private float yDelta;

  /** mean */
  private float mean = INIT_MEAN;
  /** standard deviation */
  private float stddev = INIT_STDDEV;

  /** number of values */
  private int n = 0;
  /** algorithm index */
  private int xAlgorithm;

  /** normal random numbers */
  private final RandomNormal normal = new RandomNormal ();

  /** value buckets */
  private final Buckets buckets = new Buckets (BUCKET_COUNT);

  /**
   * Constructor.
   */
  public RandomNormalPanel ()
  {
    super (HEADER_LABEL, INIT_PLOT_PROPS);
    updatePlotProperties ();

    final Font labelFont = getLabelFont ();
    final Font textFont = getTextFont ();

    // Controls.
    meanLabel.setFont (labelFont);
    meanLabel.setAlignment (Label.RIGHT);
    meanText.setFont (textFont);
    stddevLabel.setFont (labelFont);
    stddevLabel.setAlignment (Label.RIGHT);
    stddevText.setFont (textFont);
    valuesLabel.setFont (labelFont);
    valuesLabel.setAlignment (Label.RIGHT);
    valuesText.setFont (textFont);
    valuesText.setAlignment (Label.LEFT);
    algorithmLabel.setFont (labelFont);
    algorithmLabel.setAlignment (Label.RIGHT);

    algorithmChoice.add ("Central");
    algorithmChoice.add ("Polar");
    algorithmChoice.add ("Ratio");

    // Control panel.
    controlPanel.setLayout (new GridLayout (0, 5, 5, 2));
    controlPanel.add (meanLabel);
    controlPanel.add (meanText);
    controlPanel.add (valuesLabel);
    controlPanel.add (valuesText);
    controlPanel.add (runButton);
    controlPanel.add (stddevLabel);
    controlPanel.add (stddevText);
    controlPanel.add (algorithmLabel);
    controlPanel.add (algorithmChoice);
    controlPanel.add (resetButton);
    addDemoControls (controlPanel);

    runButton.setEnabled (true);
    resetButton.setEnabled (false);

    // Algorithm choice handler.
    algorithmChoice.addItemListener (new ItemListener ()
    {
      public void itemStateChanged (final ItemEvent ev)
      {
        draw ();
      }
    });

    // Run button handler.
    runButton.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        if (runButton.getLabel ().equals (RUN))
        {
          run ();
        }
        else
        {
          pause ();
        }
      }
    });

    // Reset button handler.
    resetButton.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        n = 0;
        valuesText.setText ("0");

        draw ();
      }
    });
  }

  private void run ()
  {
    String text = null;

    try
    {
      meanText.requestFocus ();
      text = meanText.getText ();
      mean = new Float (text).floatValue ();

      stddevText.requestFocus ();
      text = stddevText.getText ();
      stddev = (new Float (text)).floatValue ();

      if (stddev <= 0)
      {
        throw new Exception ("Standard deviation must be > 0");
      }
    }
    catch (final NumberFormatException ex)
    {
      processUserError ("Invalid value '" + text + "'");
      return;
    }
    catch (final Exception ex)
    {
      processUserError (ex.getMessage ());
      return;
    }

    normal.setParameters (mean, stddev);
    buckets.setLimits (xMin, xMax);
    xAlgorithm = algorithmChoice.getSelectedIndex ();

    setHeaderLabel (HEADER_LABEL + " by the " + ALGORITHM_LABELS[xAlgorithm]);

    if (n == 0)
      draw ();

    runButton.setLabel (PAUSE);
    resetButton.setEnabled (false);
    meanText.setEnabled (false);
    stddevText.setEnabled (false);
    algorithmChoice.setEnabled (false);

    paused = false;
    done = false;

    runThread = new RunThread ();
    runThread.start ();
  }

  private void pause ()
  {
    paused = true;

    runButton.setLabel (RUN);
    resetButton.setEnabled (true);
  }

  /**
   * Reset.
   */
  private void reset ()
  {
    runButton.setEnabled (true);
    resetButton.setEnabled (false);
    meanText.setEnabled (true);
    stddevText.setEnabled (true);
    algorithmChoice.setEnabled (true);

    updatePlotProperties ();
    plotFunction ();

    buckets.clear ();
  }

  // ---------------------//
  // Algorithm animation //
  // ---------------------//

  /**
   * The thread that automatically steps once per half second.
   */
  private class RunThread extends Thread
  {
    /**
     * Run the thread.
     */
    @Override
    public void run ()
    {
      while ((!paused) && (!done))
      {
        ++n;

        float x = 0;
        switch (xAlgorithm)
        {
          case CENTRAL:
            x = normal.nextCentral ();
            break;
          case POLAR:
            x = normal.nextPolar ();
            break;
          case RATIO:
            x = normal.nextRatio ();
            break;
        }

        buckets.put (x);

        if (n % GROUP_SIZE == 0)
        {
          yield ();
          valuesText.setText (Integer.toString (n));
          drawBars ();
        }
      }

      if (!paused)
      {
        runButton.setLabel (RUN);
        runButton.setEnabled (false);
        resetButton.setEnabled (true);
      }
    }
  }

  private void drawBars ()
  {
    float x = xMin;
    final float d = w * xDelta / (BUCKET_COUNT);

    final float left = mean - 3 * stddev;
    final float right = mean + 3 * stddev;

    int over = 0;
    int under = 0;

    for (int i = 0; i < BUCKET_COUNT; ++i)
    {
      final float y = buckets.get (i) / ((float) BAR_FACTOR);
      final int c = Math.round ((x - xMin) / xDelta);
      final int r = Math.round ((m_fYMax - y) / yDelta);
      final int h = xAxisRow - r;

      if (h > 0)
        plotRectangle (c, r, bWidth, h, Color.red);

      if ((x > left) && (x < right))
      {
        if (y <= valueAt (x))
          ++under;
        else
          ++over;
      }

      x += d;
    }

    done = over > under; // done if half of bars above plot
  }

  private void updatePlotProperties ()
  {
    plotProps = getPlotProperties ();

    w = plotProps.getWidth ();
    bWidth = Math.round (w / BUCKET_COUNT);
    xAxisRow = plotProps.getXAxisRow ();
    xMin = plotProps.getXMin ();
    xMax = plotProps.getXMax ();
    m_fYMax = plotProps.getYMax ();
    xDelta = plotProps.getXDelta ();
    yDelta = plotProps.getYDelta ();
  }

  // -----------------------------//
  // GraphPanel method overrides //
  // -----------------------------//

  /**
   * Draw the contents of the Euler's demo panel.
   */
  @Override
  public void draw ()
  {
    // Stop the run thread.
    paused = true;

    super.draw ();
    reset ();
  }

  /**
   * Return the value of the selected equation at x.
   * 
   * @param x
   *        the value of x
   * @return the value of the function
   */
  @Override
  public float valueAt (final float x)
  {
    final float yMax = plotProps.getYMax ();
    final float z = (x - mean) / stddev;

    return (float) (0.9f * yMax * Math.exp (-0.5 * z * z));
  }

  /**
   * Notification that the plot bounds changed. Redraw the panel.
   */
  @Override
  public void plotBoundsChanged ()
  {
    draw ();
  }

  // --------------------------//
  // DemoPanel implementation //
  // --------------------------//

  /**
   * Initialize the demo (callback from applet).
   */
  @Override
  public void initializeDemo ()
  {}

  /**
   * Close the demo (callback from applet).
   */
  @Override
  public void closeDemo ()
  {}
}
