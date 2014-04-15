package numbercruncher.program6_1;

import java.awt.Frame;

import numbercruncher.graphutils.DemoFrame;

/**
 * PROGRAM 6-1d: Polynomial Interpolation (Interactive Standalone Demo)
 * Interactively demonstrate polynomial interpolation.
 */
public class InterpolationDemo extends DemoFrame
{
  private static final String TITLE = "Polynomial Interpolation Demo";

  /**
   * Constructor.
   */
  private InterpolationDemo ()
  {
    super (TITLE, new InterpolationPanel ());
  }

  /**
   * Main.
   */
  public static void main (final String args[])
  {
    final Frame frame = new InterpolationDemo ();
    frame.setVisible (true);
  }
}
