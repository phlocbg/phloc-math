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
