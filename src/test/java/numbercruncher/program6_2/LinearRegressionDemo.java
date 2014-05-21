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
package numbercruncher.program6_2;

import java.awt.Frame;

import numbercruncher.graphutils.DemoFrame;

/**
 * PROGRAM 6-2d: Linear Regression (Interactive Standalone Demo) Interactively
 * demonstrate linear regression.
 */
public class LinearRegressionDemo extends DemoFrame
{
  private static final String TITLE = "Linear Regression Demo";

  /**
   * Constructor.
   */
  private LinearRegressionDemo ()
  {
    super (TITLE, new LinearRegressionPanel ());
  }

  /**
   * Main.
   */
  public static void main (final String args[])
  {
    final Frame frame = new LinearRegressionDemo ();
    frame.setVisible (true);
  }
}
