package numbercruncher.graphutils;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Panel;

/**
 * The base applet for all demo applets.
 */
public abstract class DemoApplet extends Applet
{
  private final IDemoPanel m_aDemoPanel; // demo panel

  /**
   * Constructor.
   * 
   * @param demoPanel
   *        the demo panel
   */
  protected DemoApplet (final IDemoPanel demoPanel)
  {
    this.m_aDemoPanel = demoPanel;
  }

  /**
   * Applet initializer.
   */
  @Override
  public void init ()
  {
    // Add the demo panel.
    setLayout (new BorderLayout ());
    add ((Panel) m_aDemoPanel, BorderLayout.CENTER);

    // Initialize the demo.
    m_aDemoPanel.initializeDemo ();
  }

  /**
   * Applet starter.
   */
  @Override
  public void start ()
  {
    // repaint();
    paint (this.getGraphics ());
  }

  /**
   * Applet stopper.
   */
  @Override
  public void stop ()
  {
    m_aDemoPanel.closeDemo ();
  }

  /**
   * Update the display without repainting the background.
   * 
   * @param g
   *        the graphics context
   */
  @Override
  public void update (final Graphics g)
  {
    paint (g);
  }

  /**
   * Redraw the contents of the demo panel.
   * 
   * @param g
   *        the graphics context
   */
  @Override
  public void paint (final Graphics g)
  {
    m_aDemoPanel.draw ();
  }
}
