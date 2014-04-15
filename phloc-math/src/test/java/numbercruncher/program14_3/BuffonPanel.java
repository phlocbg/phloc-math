package numbercruncher.program14_3;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import numbercruncher.graphutils.GraphPanel;
import numbercruncher.graphutils.PlotProperties;

/**
 * The demo panel for the Random Normal program and applet.
 */
public class BuffonPanel extends GraphPanel
{
  private static final String RUN = "Run";
  private static final String PAUSE = "Pause";
  private static final String RESET = "Reset";

  private static final PlotProperties INIT_PLOT_PROPS = new PlotProperties (-0.5f, 15.5f, -0.75f, 10.5f);

  /**
   * control panel
   */
  private final Panel controlPanel = new Panel ();
  /**
   * needles label
   */
  private final Label needlesLabel = new Label ("Needles:");
  /** needles text */
  private final Label needlesText = new Label ("0");
  /**
   * crossings label
   */
  private final Label crossingsLabel = new Label ("Crossings:");
  /** crossings text */
  private final Label crossingsText = new Label ("0");
  /** pi label */
  private final Label piLabel = new Label ("Pi:");
  /** pi text */
  private final Label piText = new Label (" ");
  /** error label */
  private final Label errorLabel = new Label ("Error:");
  /** error text */
  private final Label errorText = new Label (" ");
  /** run button */
  private final Button runButton = new Button (RUN);
  /** reset button */
  private final Button resetButton = new Button (RESET);

  /** run thread */
  private RunThread runThread;
  /** true if pause button pressed */
  private boolean paused = false;

  /** plot properties */
  private PlotProperties plotProps;
  /** plot width */
  private int w;
  /** min plot x */
  private float xMin;
  /** max plot x */
  private float xMax;
  /** min plot y */
  private float yMin;
  /** max plot y */
  private float yMax;
  /** plot x delta */
  private float xDelta;
  /** plot y delta */
  private float yDelta;

  private final Needles needles;

  /**
   * Constructor.
   */
  public BuffonPanel ()
  {
    super (INIT_PLOT_PROPS);
    updatePlotProperties ();

    needles = new Needles (xMin, xMax, yMin, yMax);

    final Font labelFont = getLabelFont ();
    final Font textFont = getTextFont ();

    // Controls.
    needlesLabel.setFont (labelFont);
    needlesLabel.setAlignment (Label.RIGHT);
    needlesText.setFont (textFont);
    needlesText.setAlignment (Label.LEFT);
    crossingsLabel.setFont (labelFont);
    crossingsLabel.setAlignment (Label.RIGHT);
    crossingsText.setFont (textFont);
    piLabel.setFont (labelFont);
    piLabel.setAlignment (Label.RIGHT);
    piText.setFont (textFont);
    errorLabel.setFont (labelFont);
    errorLabel.setAlignment (Label.RIGHT);
    errorText.setFont (textFont);

    // Control panel.
    controlPanel.setLayout (new GridLayout (0, 5, 5, 2));
    controlPanel.add (needlesLabel);
    controlPanel.add (needlesText);
    controlPanel.add (piLabel);
    controlPanel.add (piText);
    controlPanel.add (runButton);
    controlPanel.add (crossingsLabel);
    controlPanel.add (crossingsText);
    controlPanel.add (errorLabel);
    controlPanel.add (errorText);
    controlPanel.add (resetButton);
    addDemoControls (controlPanel);

    runButton.setEnabled (true);
    resetButton.setEnabled (false);

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
        needles.init ();
        needlesText.setText ("0");

        draw ();
      }
    });
  }

  private void run ()
  {
    runButton.setLabel (PAUSE);
    resetButton.setEnabled (false);

    paused = false;

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
    needlesText.setText ("0");
    crossingsText.setText ("0");
    piText.setText (" ");
    errorText.setText (" ");

    runButton.setEnabled (true);
    resetButton.setEnabled (false);

    updatePlotProperties ();
    if (needles.getCount () == 0)
      drawLines ();

    needles.init ();
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
      while (!paused)
      {
        needles.dropNext ();

        needlesText.setText (Integer.toString (needles.getCount ()));
        crossingsText.setText (Integer.toString (needles.getCrossings ()));
        piText.setText (Float.toString (needles.getPi ()));
        errorText.setText (Float.toString (needles.getError ()));

        drawNeedle ();

        // Give the text fields a chance to update.
        try
        {
          sleep (10);
        }
        catch (final InterruptedException ex)
        {}
      }

      if (!paused)
      {
        runButton.setLabel (RUN);
        runButton.setEnabled (false);
        resetButton.setEnabled (true);
      }
    }
  }

  private void updatePlotProperties ()
  {
    plotProps = getPlotProperties ();

    w = plotProps.getWidth ();
    xMin = plotProps.getXMin ();
    xMax = plotProps.getXMax ();
    yMin = plotProps.getYMin ();
    yMax = plotProps.getYMax ();
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

  private void drawLines ()
  {
    final int yFirst = (int) Math.floor (yMin);
    final int yLast = (int) Math.floor (yMax);

    for (int y = yFirst; y <= yMax; ++y)
    {
      final int r = Math.round ((yMax - y) / yDelta);
      plotLine (0, r, w, r, Color.blue);
    }
  }

  private void drawNeedle ()
  {
    final int c1 = Math.round ((needles.getX1 () - xMin) / xDelta);
    final int c2 = Math.round ((needles.getX2 () - xMin) / xDelta);
    final int r1 = Math.round ((yMax - needles.getY1 ()) / yDelta);
    final int r2 = Math.round ((yMax - needles.getY2 ()) / yDelta);

    plotLine (c1, r1, c2, r2, Color.red);
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
