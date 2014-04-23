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
