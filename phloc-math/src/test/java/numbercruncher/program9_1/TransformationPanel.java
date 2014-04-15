package numbercruncher.program9_1;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import numbercruncher.graphutils.IDemoPanel;

/**
 * The demo panel for the graphic tranformations program and applet.
 */
public class TransformationPanel extends Panel implements IDemoPanel
{
  /** cube panel */
  private final CubePanel cubePanel;
  /** control panel */
  private final Panel controlPanel = new Panel ();
  /** matrix panel */
  private final Panel matrixPanel = new Panel ();
  /** button panel */
  private final Panel buttonPanel = new Panel ();
  /** top border */
  private final Panel topBorder = new Panel ();
  /** left border */
  private final Panel leftBorder = new Panel ();
  /** right border */
  private final Panel rightBorder = new Panel ();

  /** matrix elements */
  private final Label elmtTexts[][] = new Label [4] [4];
  /** run button */
  private final Button runButton = new Button ("Run");
  /** step button */
  private final Button stepButton = new Button ("Step");
  /** reset button */
  private final Button resetButton = new Button ("Reset");

  /** transformation */
  private final Transformation transformation = new Transformation ();

  /** thread for automatic stepping */
  private Thread runThread = null;
  /** true if pause button was pressed */
  private boolean paused = false;

  /**
   * Constructor.
   */
  TransformationPanel ()
  {
    matrixPanel.setLayout (new GridLayout (4, 0));

    // Initialize the text labels for the transformation matrix elements.
    for (int r = 0; r < 4; ++r)
    {
      for (int c = 0; c < 4; ++c)
      {
        final Label elmtText = new Label ();
        elmtText.setAlignment (Label.RIGHT);
        elmtTexts[r][c] = elmtText;
        matrixPanel.add (elmtText);
      }
    }

    buttonPanel.setLayout (new GridLayout (3, 0));
    buttonPanel.add (runButton);
    buttonPanel.add (stepButton);
    buttonPanel.add (resetButton);

    controlPanel.setLayout (new BorderLayout (25, 0));
    controlPanel.setBackground (Color.lightGray);
    controlPanel.add (matrixPanel, BorderLayout.CENTER);
    controlPanel.add (buttonPanel, BorderLayout.EAST);

    cubePanel = new CubePanel (transformation, this);

    topBorder.setBackground (Color.lightGray);
    leftBorder.setBackground (Color.lightGray);
    rightBorder.setBackground (Color.lightGray);
    topBorder.setSize (0, 5);
    leftBorder.setSize (5, 0);
    rightBorder.setSize (5, 0);

    setLayout (new BorderLayout ());
    add (topBorder, BorderLayout.NORTH);
    add (leftBorder, BorderLayout.WEST);
    add (rightBorder, BorderLayout.EAST);
    add (cubePanel, BorderLayout.CENTER);
    add (controlPanel, BorderLayout.SOUTH);

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

    // Reset button handler.
    resetButton.addActionListener (new ActionListener ()
    {
      public void actionPerformed (final ActionEvent ev)
      {
        reset ();
      }
    });

    // Resize event handler.
    addComponentListener (new ComponentAdapter ()
    {
      @Override
      public void componentResized (final ComponentEvent ev)
      {
        reset ();
      }
    });
  }

  /**
   * Initialize the demo.
   */
  public void initializeDemo ()
  {}

  /**
   * Close the demo.
   */
  public void closeDemo ()
  {}

  /**
   * Notification that the panel was resized. (Callback from DemoFrame.)
   */
  public void panelResized ()
  {
    reset ();
    draw ();
  }

  /**
   * Draw the contents of the panel.
   */
  public void draw ()
  {
    repaint ();
  }

  /**
   * Draw the cube after a single transformation.
   */
  private void step ()
  {
    cubePanel.draw ();
  }

  /**
   * Reset the cube to its initial state.
   */
  private void reset ()
  {
    paused = true;
    runButton.setEnabled (true);
    stepButton.setLabel ("Step");

    try
    {
      if (runThread != null)
        runThread.join (1000);
    }
    catch (final InterruptedException ex)
    {}

    transformation.reset ();
    cubePanel.reset ();
    step ();
  }

  /**
   * Update the displayed transformation matix elements. (Called from
   * CubePanel.)
   */
  public void updateMatrixDisplay ()
  {
    for (int r = 0; r < 4; ++r)
    {
      for (int c = 0; c < 4; ++c)
      {
        final String text = Float.toString (transformation.at (r, c));
        elmtTexts[r][c].setText (text);
      }
    }
  }

  /**
   * The thread that automatically steps the cube periodically.
   */
  private class RunThread extends Thread
  {
    /**
     * Run the thread.
     */
    @Override
    public void run ()
    {
      // Loop forever.
      while (!paused)
      {
        step ();

        try
        {
          sleep (20);
        }
        catch (final Exception ex)
        {}
      }
    }
  }
}
