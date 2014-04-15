package numbercruncher.program10_2;

import java.awt.Frame;

import numbercruncher.graphutils.DemoFrame;

/**
 * PROGRAM 10-2d: Polynomial Regression (Interactive Standalone Demo)
 * Interactively demonstrate polynomial regression.
 */
public class RegressionDemo extends DemoFrame
{
  private static final String TITLE = "Polynomial Regression Demo";

  /**
   * Constructor.
   */
  private RegressionDemo ()
  {
    super (TITLE, new RegressionPanel ());
  }

  /**
   * Main.
   */
  public static void main (final String args[])
  {
    final Frame frame = new RegressionDemo ();
    frame.setVisible (true);
  }
}
