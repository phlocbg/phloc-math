package numbercruncher.program16_4;

import java.awt.Frame;

import numbercruncher.graphutils.DemoFrame;

/**
 * PROGRAM 16-4: Mandelbrot Set Fractal (Standalone Demo) Graph the Mandelbrot
 * set fractal. You can zoom into any rectangular region of the graph by using
 * the mouse.
 */
public class MandelbrotSetDemo extends DemoFrame
{
  private static final String TITLE = "Mandelbrot Set Demo";

  /**
   * Constructor.
   */
  private MandelbrotSetDemo ()
  {
    super (TITLE, new MandelbrotSetPanel (), 450, 525);
  }

  /**
   * Main.
   */
  public static void main (final String args[])
  {
    final Frame frame = new MandelbrotSetDemo ();
    frame.setVisible (true);
  }
}
