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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The base frame for all standalone demo programs.
 */
public abstract class DemoFrame extends Frame
{
  private final String m_sTitle; // window title
  private final IDemoPanel m_aDemoPanel; // demo panel

  /**
   * Constructor.
   * 
   * @param title
   *        the window title
   * @param demoPanel
   *        the demo panel
   */
  protected DemoFrame (final String title, final IDemoPanel demoPanel)
  {
    this (title, demoPanel, 600, 500);
  }

  /**
   * Constructor.
   * 
   * @param title
   *        the window title
   * @param demoPanel
   *        the demo panel
   * @param width
   *        the frame width
   * @param height
   *        the frame height
   */
  protected DemoFrame (final String title, final IDemoPanel demoPanel, final int width, final int height)
  {
    this.m_sTitle = title;
    this.m_aDemoPanel = demoPanel;
    setTitle (m_sTitle);
    initFrame (width, height);
  }

  /**
   * Initialize the frame.
   * 
   * @param width
   *        the frame width
   * @param height
   *        the frame height
   */
  private void initFrame (final int width, final int height)
  {
    // Center the demo frame.
    final Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
    setSize (width, height);
    setLocation ((screenSize.width - width) / 2, (screenSize.height - height) / 2);

    // Add the demo panel.
    setLayout (new BorderLayout ());
    add ((Panel) m_aDemoPanel, BorderLayout.CENTER);

    // Initialize the demo.
    m_aDemoPanel.initializeDemo ();

    // Window event handlers.
    addWindowListener (new WindowAdapter ()
    {
      @Override
      public void windowOpened (final WindowEvent ev)
      {
        repaint ();
      }

      @Override
      public void windowClosing (final WindowEvent ev)
      {
        System.exit (0);
      }
    });

    // Resize event handler.
    addComponentListener (new ComponentAdapter ()
    {
      @Override
      public void componentResized (final ComponentEvent ev)
      {
        resized ();
      }
    });
  }

  /**
   * The frame was resized.
   */
  private void resized ()
  {
    m_aDemoPanel.panelResized ();
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
    m_aDemoPanel.draw ();
  }
}
