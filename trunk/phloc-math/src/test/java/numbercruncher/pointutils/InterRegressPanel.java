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
package numbercruncher.pointutils;

import java.awt.Choice;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * The base panel for the interpolation and regression panels.
 */
public abstract class InterRegressPanel extends UserPointPanel
{
  /** true if OK to plot function */
  protected boolean plotOK = false;

  /**
   * Constructor.
   * 
   * @param maxPoints
   *        the maximum number of data points
   * @param actionButton1Label
   *        the label for action button 1
   * @param actionButton2Label
   *        the label for action button 2
   */
  protected InterRegressPanel (final int maxPoints, final String actionButton1Label, final String actionButton2Label)
  {
    this (maxPoints, actionButton1Label, actionButton2Label, false);
  }

  /**
   * Constructor.
   * 
   * @param maxPoints
   *        the maximum number of data points
   * @param actionButton1Label
   *        the label for action button 1
   * @param actionButton2Label
   *        the label for action button 2
   * @param showDegree
   *        true to enable the degree choice, false to disable
   */
  protected InterRegressPanel (final int maxPoints,
                               final String actionButton1Label,
                               final String actionButton2Label,
                               final boolean showDegree)
  {
    super (maxPoints, "# points:", actionButton1Label, actionButton2Label);

    final Label degreeLabel = new Label ();
    final Choice degreeChoice = new Choice ();

    // Control panel.
    controlPanel.setLayout (new GridLayout (0, 3, 5, 2));
    controlPanel.add (actionButton1);
    controlPanel.add (nLabel);
    controlPanel.add (nText);
    controlPanel.add (actionButton2);

    // Degree control.
    if (showDegree)
    {
      final Font labelFont = getLabelFont ();

      degreeLabel.setFont (labelFont);
      degreeLabel.setAlignment (Label.RIGHT);
      degreeLabel.setText ("Degree:");

      for (int i = 1; i <= 9; ++i)
        degreeChoice.add (Integer.toString (i));

      controlPanel.add (degreeLabel);
      controlPanel.add (degreeChoice);
    }
    addDemoControls (controlPanel);

    actionButton1.setEnabled (false);

    // Degree choice handler.
    degreeChoice.addItemListener (new ItemListener ()
    {
      public void itemStateChanged (final ItemEvent ev)
      {
        final Choice choice = (Choice) ev.getItemSelectable ();
        final int degree = choice.getSelectedIndex () + 1;
        degreeChanged (degree);
        draw ();
      }
    });
  }

  /**
   * Notify that the degree has changed. (Do nothing here.)
   * 
   * @param degree
   *        the new degree
   */
  protected void degreeChanged (final int degree)
  {}

  /**
   * Return whether or not it's OK to plot the function.
   * 
   * @return true if OK, otherwise false
   */
  @Override
  protected boolean plotOK ()
  {
    return plotOK;
  }
}
