package numbercruncher.program3_1;

import java.applet.Applet;
import java.awt.BorderLayout;

/**
 * PROGRAM 3-1a: IEEE 754 Standard (Interactive Applet) Interactive decompose
 * and recompose floating-point numbers according to the IEEE 754 standard.
 */
public class FPFormatsApplet extends Applet
{
  // Initialize the applet
  @Override
  public void init ()
  {
    // Add the demo panel.
    setLayout (new BorderLayout ());
    add (new FPFormatsPanel (), BorderLayout.CENTER);
  }
}
