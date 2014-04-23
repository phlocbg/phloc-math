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
package numbercruncher.program5_5;

import java.awt.Frame;

import numbercruncher.graphutils.DemoFrame;

/**
 * PROGRAM 5-5d: Newton's Algorithm (Interactive Standalone Demo) Interactively
 * demonstrate Newton's Algorithm on various functions.
 */
public class NewtonsDemo extends DemoFrame
{
  private static final String TITLE = "Newton's Demo";

  /**
   * Constructor.
   */
  private NewtonsDemo ()
  {
    super (TITLE, new NewtonsPanel ());
  }

  /**
   * Main.
   */
  public static void main (final String args[])
  {
    final Frame frame = new NewtonsDemo ();
    frame.setVisible (true);
  }
}
