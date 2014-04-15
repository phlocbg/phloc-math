package numbercruncher.rootutils;

import java.awt.Button;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import numbercruncher.graphutils.FunctionFrame;
import numbercruncher.graphutils.GraphPanel;

/**
 * The base panel for all root finder demo panels.
 */
public abstract class RootFinderPanel extends GraphPanel
{
  /** control panel */
  protected Panel controlPanel = new Panel ();
  /** n label */
  protected Label nLabel = new Label ();
  /** n text */
  protected Label nText = new Label ();
  /** run button */
  protected Button runButton = new Button ("Run");
  /** step button */
  protected Button stepButton = new Button ("Step");

  /** function image file name */
  private final String m_sFunctionImageFileName;
  /** function frame */
  private FunctionFrame m_aFunctionFrame;
  /** function frame title */
  private final String m_sFunctionFrameTitle;

  /**
   * array of functions to find roots for
   */
  private final PlotFunction m_aPlotFunctions[];
  /** selected function */
  private PlotFunction plotFunction;

  /** thread for automatic stepping */
  private Thread runThread = null;

  /** true if pause button was pressed */
  private boolean paused = false;
  /** true if algorithm converged */
  private boolean m_bConverged = false;

  /**
   * Constructor.
   * 
   * @param plotFunctions
   *        the array of functions to plot
   * @param functionImageFileName
   *        the function image file name
   * @param functionFrameTitle
   *        the function frame title
   */
  protected RootFinderPanel (final PlotFunction plotFunctions[],
                             final String functionImageFileName,
                             final String functionFrameTitle)
  {
    this (plotFunctions, true, false, functionImageFileName, functionFrameTitle);
  }

  /**
   * Constructor.
   * 
   * @param plotFunctions
   *        the array of functions to plot
   * @param xorMode
   *        true to set XORMode
   * @param functionImageFileName
   *        the function image file name
   * @param functionFrameTitle
   *        the function frame title
   */
  protected RootFinderPanel (final PlotFunction plotFunctions[],
                             final boolean xorMode,
                             final String functionImageFileName,
                             final String functionFrameTitle)
  {
    this (plotFunctions, xorMode, false, functionImageFileName, functionFrameTitle);
  }

  /**
   * Constructor.
   * 
   * @param plotFunctions
   *        the array of functions to plot
   * @param xorMode
   *        true to set XORMode
   * @param drawXequalsY
   *        true to draw X=Y line
   * @param functionImageFileName
   *        the function image file name
   * @param functionFrameTitle
   *        the function frame title
   */
  protected RootFinderPanel (final PlotFunction plotFunctions[],
                             final boolean xorMode,
                             final boolean drawXequalsY,
                             final String functionImageFileName,
                             final String functionFrameTitle)
  {
    super (plotFunctions, plotFunctions[0].getPlotProperties (), xorMode, drawXequalsY);

    this.m_aPlotFunctions = plotFunctions;
    this.plotFunction = plotFunctions[0];
    this.m_sFunctionImageFileName = functionImageFileName;
    this.m_sFunctionFrameTitle = functionFrameTitle;

    final Font labelFont = getLabelFont ();
    final Font textFont = getTextFont ();

    nLabel.setFont (labelFont);
    nLabel.setAlignment (Label.RIGHT);
    nLabel.setText ("n:");
    nText.setFont (textFont);
    nText.setAlignment (Label.LEFT);
    nText.setText (" ");

    runButton.setEnabled (true);
    stepButton.setEnabled (true);

    // Step button handler.
    stepButton.addActionListener (new ActionListener ()
    {
      // If the run thread is inactive, call step().
      // If the thread is active, stop it and revert the
      // button label to "Step".
      public void actionPerformed (final ActionEvent ev)
      {
        if ((runThread != null) && (runThread.isAlive ()))
        {
          paused = true;
          runButton.setEnabled (true);
          stepButton.setLabel ("Step");
        }
        else
        {
          step ();
        }
      }
    });

    // Run button handler.
    runButton.addActionListener (new ActionListener ()
    {
      // Start the run thread and change the step button
      // label to "Pause".
      public void actionPerformed (final ActionEvent ev)
      {
        runButton.setEnabled (false);
        stepButton.setLabel ("Pause");
        paused = false;

        runThread = new RunThread ();
        runThread.start ();
      }
    });
  }

  /**
   * Set the converged flag.
   * 
   * @param converged
   *        true if algorithm converged
   */
  public void setConverged (final boolean converged)
  {
    this.m_bConverged = converged;
  }

  /**
   * Open the function frame.
   */
  private void openFunctionFrame ()
  {
    m_aFunctionFrame = new FunctionFrame (m_aPlotFunctions, m_sFunctionImageFileName, m_sFunctionFrameTitle, this);
    m_aFunctionFrame.setVisible (true);

    setHeaderImage (m_aFunctionFrame.getImage ());
    setFunction (m_aPlotFunctions[0]);
  }

  /**
   * Choose a function. (Callback from the function frame.)
   */
  @Override
  public void chooseFunction (final int index)
  {
    plotFunction = m_aPlotFunctions[index];
    setFunction (plotFunction);
    draw ();
  }

  /**
   * Return the selected function to find roots for.
   * 
   * @return the selected function
   */
  protected PlotFunction getSelectedPlotFunction ()
  {
    return plotFunction;
  }

  // -----------------------------//
  // GraphPanel method overrides //
  // -----------------------------//

  /**
   * Create the function frame or bring it to the front. (Callback from header
   * panel.)
   */
  @Override
  public void doHeaderAction ()
  {
    if (m_aFunctionFrame != null)
    {
      m_aFunctionFrame.toFront ();
    }
    else
    {
      openFunctionFrame ();
    }
  }

  /**
   * Return the value of the selected function at x.
   * 
   * @param x
   *        the value of x
   * @return the value of the function
   */
  @Override
  public float valueAt (final float x)
  {
    return plotFunction.at (x);
  }

  /**
   * Notification that the plot bounds changed. Redraw the panel.
   */
  @Override
  public void plotBoundsChanged ()
  {
    draw ();
  }

  /**
   * Notification that a user input error occurred. Disable the run and step
   * buttons.
   */
  @Override
  protected void userErrorOccurred ()
  {
    runButton.setEnabled (false);
    stepButton.setEnabled (false);
  }

  /**
   * Draw the contents the panel.
   */
  @Override
  public void draw ()
  {
    // Stop the run thread.
    paused = true;
    m_bConverged = false;

    // Reinitialize the run and step buttons.
    runButton.setEnabled (true);
    stepButton.setEnabled (true);
    stepButton.setLabel ("Step");

    setHeaderDisplay (plotFunction);
    super.draw ();
  }

  // --------------------------//
  // DemoPanel implementation //
  // --------------------------//

  /**
   * Initialize the demo. (Callback from applet.)
   */
  @Override
  public void initializeDemo ()
  {
    openFunctionFrame ();
  }

  /**
   * Close the demo. (Callback from applet.)
   */
  @Override
  public void closeDemo ()
  {
    if (m_aFunctionFrame != null)
    {
      m_aFunctionFrame.setVisible (false);
      m_aFunctionFrame.dispose ();
      m_aFunctionFrame = null;
    }
  }

  // ---------------------//
  // Algorithm animation //
  // ---------------------//

  /**
   * One iteration step. Do nothing here.
   */
  protected void step ()
  {}

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
      // Loop until the iteration count stops.
      while ((!paused) && (!m_bConverged))
      {
        step ();

        try
        {
          sleep (500); // half second
        }
        catch (final Exception ex)
        {}
      }

      // Unless it's only paused, disable this function.
      if (!paused)
      {
        runButton.setEnabled (false);
        stepButton.setEnabled (false);
        stepButton.setLabel ("Step");
      }
    }
  }

  /**
   * The algorithm successfully converged.
   */
  protected void successfullyConverged ()
  {
    m_bConverged = true;

    runButton.setEnabled (false);
    stepButton.setEnabled (false);
  }

  /**
   * The algorithm has exceeded the maximum number of iterations.
   * 
   * @param maxIters
   *        the maximum number of iterations
   * @param text
   *        the text to star out
   */
  protected void iterationLimitExceeded (final int maxIters, final Label text)
  {
    m_bConverged = false;
    paused = true; // stop the run thread

    nText.setText (">" + maxIters + " ITERS");
    text.setText ("***");

    runButton.setEnabled (false);
    stepButton.setEnabled (false);
  }
}
