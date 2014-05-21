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

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * The panel that contains the x-min, x-max, y-min, and y-max text controls for
 * the function plot bounds.
 */
class PlotBoundsPanel extends Panel
{
  /**
   * x-min label
   */
  private final Label xMinLabel = new Label ("Min x:");
  /**
   * y-min label
   */
  private final Label xMaxLabel = new Label ("Max x:");
  /**
   * x-max label
   */
  private final Label yMinLabel = new Label ("Min y:");
  /**
   * y-max label
   */
  private final Label yMaxLabel = new Label ("Max y:");
  /**
   * x-min textfield
   */
  private final BoundsTextField xMinText = new BoundsTextField ();
  /**
   * x-max textfield
   */
  private final BoundsTextField xMaxText = new BoundsTextField ();
  /**
   * y-min textfield
   */
  private final BoundsTextField yMinText = new BoundsTextField ();
  /**
   * y-max textfield
   */
  private final BoundsTextField yMaxText = new BoundsTextField ();

  /** label font */
  private final Font labelFont = new Font ("Dialog", 1, 12);

  /** parent graph panel */
  GraphPanel m_aGraphPanel;

  /**
   * Constructor.
   * 
   * @param graphPanel
   *        the parent graph panel
   */
  PlotBoundsPanel (final GraphPanel graphPanel)
  {
    this.m_aGraphPanel = graphPanel;

    // Bounds controls.
    xMinLabel.setFont (labelFont);
    xMinLabel.setAlignment (Label.RIGHT);
    xMaxLabel.setFont (labelFont);
    xMaxLabel.setAlignment (Label.RIGHT);
    yMinLabel.setFont (labelFont);
    yMinLabel.setAlignment (Label.RIGHT);
    yMaxLabel.setFont (labelFont);
    yMaxLabel.setAlignment (Label.RIGHT);

    // Bounds panel.
    setBackground (Color.lightGray);
    setLayout (new GridLayout (0, 4, 5, 2));
    add (xMinLabel);
    add (xMinText);
    add (yMinLabel);
    add (yMinText);
    add (xMaxLabel);
    add (xMaxText);
    add (yMaxLabel);
    add (yMaxText);
  }

  /**
   * Set the text fields from the plot properties.
   * 
   * @param plotProps
   *        the plot properties
   */
  void setTextFields (final PlotProperties plotProps)
  {
    xMinText.setText (Float.toString (plotProps.getXMin ()));
    xMaxText.setText (Float.toString (plotProps.getXMax ()));
    yMinText.setText (Float.toString (plotProps.getYMin ()));
    yMaxText.setText (Float.toString (plotProps.getYMax ()));
  }

  /**
   * Update the plot properties from the text fields.
   * 
   * @param plotProps
   *        the plot properties
   */
  void updatePlotProperties (final PlotProperties plotProps)
  {
    if (!boundsOK ())
      return;

    plotProps.update (Float.valueOf (xMinText.getText ()).floatValue (),
                      Float.valueOf (xMaxText.getText ()).floatValue (),
                      Float.valueOf (yMinText.getText ()).floatValue (),
                      Float.valueOf (yMaxText.getText ()).floatValue ());
  }

  /**
   * Check the bounds.
   * 
   * @return true if bounds are OK, else return false
   */
  private boolean boundsOK ()
  {
    final BoundsTextField fields[] = { xMinText, xMaxText, yMinText, yMaxText };
    final float values[] = new float [4];

    for (int i = 0; i < 4; ++i)
    {
      try
      {
        values[i] = Float.valueOf (fields[i].getText ()).floatValue ();
        fields[i].setForeground (Color.black);
      }
      catch (final Exception ex)
      {
        fields[i].setForeground (Color.red);
        m_aGraphPanel.processUserError ("Invalid number format.");
        return false;
      }
    }

    if (values[1] > values[0])
    {
      fields[1].setForeground (Color.black);
    }
    else
    {
      fields[1].setForeground (Color.red);
      m_aGraphPanel.processUserError ("Min x > Max x");
      return false;
    }

    if (values[3] > values[2])
    {
      fields[3].setForeground (Color.black);
    }
    else
    {
      fields[3].setForeground (Color.red);
      m_aGraphPanel.processUserError ("Min y > Max y");
      return false;
    }

    return true;
  }

  /**
   * The bounds text field class.
   */
  private class BoundsTextField extends TextField
  {
    /**
     * Constructor.
     */
    private BoundsTextField ()
    {
      super (3);

      // Action (return key) handler.
      addActionListener (new ActionListener ()
      {
        public void actionPerformed (final ActionEvent ev)
        {
          if (boundsOK ())
          {
            m_aGraphPanel.plotBoundsChanged (); // callback
          }
        }
      });

      // Focus (tab key) handler.
      addFocusListener (new FocusAdapter ()
      {
        @Override
        public void focusLost (final FocusEvent ev)
        {
          if (boundsOK ())
          {
            m_aGraphPanel.plotBoundsChanged (); // callback
          }
        }
      });
    }
  }
}
