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
package numbercruncher.program14_1;

import java.awt.Frame;

import numbercruncher.graphutils.DemoFrame;

/**
 * PROGRAM 14-1d: Normally-Distributed Random Numbers (Standalone Demo)
 * Demonstrate algorithms for generating normally-distributed random numbers.
 */
public class GenerateRandomNormalDemo extends DemoFrame
{
  private static final String TITLE = "Normally-Distributed Random Numbers";

  /**
   * Constructor.
   */
  private GenerateRandomNormalDemo ()
  {
    super (TITLE, new RandomNormalPanel ());
  }

  /**
   * Main.
   */
  public static void main (final String args[])
  {
    final Frame frame = new GenerateRandomNormalDemo ();
    frame.setVisible (true);
  }
}
