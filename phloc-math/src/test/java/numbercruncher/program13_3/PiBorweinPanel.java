package numbercruncher.program13_3;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import numbercruncher.graphutils.IDemoPanel;

/**
 * The demo panel for the Borwein pi algorithm program and applet.
 */
public class PiBorweinPanel extends Panel implements IDemoPanel, IPiBorweinParent
{
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat ("00");

  private static final String PI_LABEL = "  pi =";
  private static final String ZERO_TIME = "00:00:00";
  private static final String DEFAULT_DIGITS = "700";
  private static final String RUN_LABEL = "Run";
  private static final String STOP_LABEL = "Stop";

  /** start time */
  long startTime;
  /** phase mark time */
  long markTime;

  private static final Color MAROON = new Color (128, 0, 0);

  /**
   * pi label
   */
  private final Label piLabel = new Label (PI_LABEL);
  /**
   * pi text area
   */
  private final TextArea piText = new TextArea ();
  /**
   * control panel
   */
  private final Panel controlPanel = new Panel ();
  /**
   * digits label
   */
  private final Label digitsLabel = new Label ("# Digits:");
  /**
   * digits text
   */
  private final TextField digitsText = new TextField (DEFAULT_DIGITS);
  /**
   * scale label
   */
  private final Label scaleLabel = new Label ("Scale:");
  /**
   * scale text
   */
  private final Label scaleText = new Label (" ");
  /**
   * phase label
   */
  private final Label phaseLabel = new Label ("Phase:");
  /**
   * phase text
   */
  private final Label phaseText = new Label (" ");
  /**
   * task label
   */
  private final Label taskLabel = new Label ("Task:");
  /**
   * task text
   */
  private final Label taskText = new Label (" ");
  /**
   * phase time label
   */
  private final Label phaseTimeLabel = new Label ("Phase time:");
  /**
   * phase time text
   */
  private final Label phaseTimeText = new Label (ZERO_TIME);
  /**
   * total time label
   */
  private final Label totalTimeLabel = new Label ("Total time:");
  /**
   * total time text
   */
  private final Label totalTimeText = new Label (ZERO_TIME);
  /**
   * run button
   */
  private final Button runButton = new Button (RUN_LABEL);
  /**
   * stop button
   */
  private final Button stopButton = new Button (STOP_LABEL);

  /** Borwein pi algorithm */
  private PiBorweinAlgorithm algorithm;
  /** timer thread */
  private TimerThread timerThread;
  /** compute thread */
  private ComputeThread computeThread;

  /**
   * Constructor.
   */
  PiBorweinPanel ()
  {
    final Font labelFont = new Font ("Dialog", 1, 12);
    final Font textFont = new Font ("Dialog", 0, 12);
    final Font piFont = new Font ("Monospaced", 0, 12);

    // Pi controls.
    piLabel.setBackground (Color.lightGray);
    piLabel.setForeground (Color.blue);
    piLabel.setFont (labelFont);
    piText.setFont (piFont);
    piText.setEditable (false);
    digitsLabel.setFont (labelFont);
    digitsLabel.setAlignment (Label.RIGHT);
    digitsText.setFont (textFont);
    scaleLabel.setFont (labelFont);
    scaleLabel.setAlignment (Label.RIGHT);
    scaleText.setFont (textFont);
    phaseLabel.setFont (labelFont);
    phaseLabel.setAlignment (Label.RIGHT);
    phaseText.setFont (textFont);
    taskLabel.setFont (labelFont);
    taskLabel.setAlignment (Label.RIGHT);
    taskText.setFont (textFont);
    phaseTimeLabel.setFont (labelFont);
    phaseTimeLabel.setAlignment (Label.RIGHT);
    phaseTimeText.setFont (textFont);
    totalTimeLabel.setFont (labelFont);
    totalTimeLabel.setAlignment (Label.RIGHT);
    totalTimeText.setFont (textFont);

    // Pi control panel.
    controlPanel.setBackground (Color.lightGray);
    controlPanel.setLayout (new GridLayout (2, 7, 2, 2));
    controlPanel.add (digitsLabel);
    controlPanel.add (digitsText);
    controlPanel.add (phaseLabel);
    controlPanel.add (phaseText);
    controlPanel.add (phaseTimeLabel);
    controlPanel.add (phaseTimeText);
    controlPanel.add (runButton);
    controlPanel.add (scaleLabel);
    controlPanel.add (scaleText);
    controlPanel.add (taskLabel);
    controlPanel.add (taskText);
    controlPanel.add (totalTimeLabel);
    controlPanel.add (totalTimeText);
    controlPanel.add (stopButton);

    // Pi panel.
    setLayout (new BorderLayout ());
    add (piLabel, BorderLayout.NORTH);
    add (piText, BorderLayout.CENTER);
    add (controlPanel, BorderLayout.SOUTH);

    stopButton.setEnabled (false);

    // Run button handler.
    runButton.addActionListener (new ActionListener ()
    {
      // Start and stop the algorithm.
      public void actionPerformed (final ActionEvent ev)
      {
        run ();
      }
    });

    // Stop button handler.
    stopButton.addActionListener (new ActionListener ()
    {
      // Start and stop the algorithm.
      public void actionPerformed (final ActionEvent ev)
      {
        stop ();
      }
    });
  }

  private void run ()
  {
    int digits;

    try
    {
      digits = Integer.parseInt (digitsText.getText ());
    }
    catch (final NumberFormatException ex)
    {
      piLabel.setForeground (MAROON);
      piLabel.setText ("ERROR: Invalid digits format.");
      return;
    }

    if (digits <= 0)
    {
      piLabel.setForeground (MAROON);
      piLabel.setText ("ERROR: Must have digits > 0.");
      return;
    }

    piLabel.setForeground (Color.blue);
    piLabel.setText (PI_LABEL);
    piText.setText ("");

    phaseTimeText.setText (ZERO_TIME);
    totalTimeText.setText (ZERO_TIME);

    runButton.setEnabled (false);
    stopButton.setEnabled (true);

    algorithm = new PiBorweinAlgorithm (digits, this);
    startTime = markTime = System.currentTimeMillis ();

    timerThread = new TimerThread ();
    timerThread.start ();

    computeThread = new ComputeThread ();
    computeThread.start ();
  }

  private void stop ()
  {
    if (timerThread.isAlive ())
    {
      timerThread.stop ();
    }

    if (computeThread.isAlive ())
    {
      computeThread.stop ();
      taskText.setText ("STOPPED");
    }

    runButton.setEnabled (true);
    stopButton.setEnabled (false);
  }

  public void notifyScale (final int scale)
  {
    scaleText.setText (Integer.toString (scale));
  }

  public void notifyPhase (final int phase)
  {
    switch (phase)
    {

      case PiBorweinConstants.INITIALIZING:
      {
        phaseText.setText ("Initializing");
        taskText.setText (" ");
        break;
      }

      case PiBorweinConstants.INVERTING:
      {
        phaseText.setText ("Inverting");
        taskText.setText (" ");
        break;
      }

      case PiBorweinConstants.DONE:
      {
        timerThread.stop ();
        phaseText.setText (Integer.toString (algorithm.getIterations ()) + " iterations");
        taskText.setText ("DONE");
        break;
      }

      default:
      {
        phaseText.setText ("Iteration #" + Integer.toString (phase));
        taskText.setText (" ");
        break;
      }
    }

    phaseTimeText.setText (timestamp (markTime));
    markTime = System.currentTimeMillis ();

    if (phase == PiBorweinConstants.DONE)
    {
      showPi (algorithm.getPi ());

      runButton.setEnabled (true);
      stopButton.setEnabled (false);
    }
  }

  public void notifyTask (final String task)
  {
    taskText.setText (task);
  }

  private void showPi (final String piString)
  {
    int index = 2;
    int line = 0;
    int group = 0;
    final int length = piString.length ();

    final StringBuffer buffer = new StringBuffer ((int) (1.3f * length));

    buffer.append (piString.substring (0, 2));

    while (index + 5 < length)
    {
      buffer.append (piString.substring (index, index + 5) + " ");
      index += 5;

      if (++group == 10)
      {
        buffer.append ("\n");

        if (++line == 10)
        {
          buffer.append ("\n");
          line = 0;
        }

        buffer.append ("  ");
        group = 0;
      }
    }

    if (index < length)
      buffer.append (piString.substring (index));
    piText.append (buffer.toString ());
  }

  private String timestamp (final long time)
  {
    final long elapsed = (System.currentTimeMillis () - time + 500) / 1000;
    final long hours = elapsed / (60 * 60);
    final long minutes = (elapsed % (60 * 60)) / 60;
    final long seconds = elapsed % 60;

    return DECIMAL_FORMAT.format (hours) +
           ":" +
           DECIMAL_FORMAT.format (minutes) +
           ":" +
           DECIMAL_FORMAT.format (seconds);
  }

  /**
   * There is no function to plot.
   */
  protected boolean plotOK ()
  {
    return false;
  }

  /**
   * Initialize the demo.
   */
  public void initializeDemo ()
  {}

  /**
   * close the demo.
   */
  public void closeDemo ()
  {}

  /**
   * Draw the contents of the panel.
   */
  public void draw ()
  {}

  /**
   * Notification that the panel was resized.
   */
  public void panelResized ()
  {}

  /**
   * The thread that keeps track of the total elapsed time.
   */
  private class TimerThread extends Thread
  {
    /**
     * Run the thread.
     */
    @Override
    public void run ()
    {
      try
      {
        for (;;)
        {
          sleep (1000);
          totalTimeText.setText (timestamp (startTime));
        }
      }
      catch (final InterruptedException ex)
      {}
    }
  }

  /**
   * The thread that computes pi.
   */
  private class ComputeThread extends Thread
  {
    /**
     * Run the thread.
     */
    @Override
    public void run ()
    {
      try
      {
        algorithm.compute ();
      }
      catch (final Exception ex)
      {
        piLabel.setForeground (MAROON);
        piLabel.setText ("ERROR:  " + ex.getMessage ());
      }
    }
  }
}
