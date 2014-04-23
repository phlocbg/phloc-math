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
package numbercruncher.program2_3;

import numbercruncher.mathutils.IntPower;

/**
 * PROGRAM 2-3: Test Class IntPower Test the IntPower class.
 */
public class TestIntPower
{
  public static void main (final String args[])
  {
    System.out.println (IntPower.raise (2, 5) + " " + Math.pow (2, 5));
    System.out.println (IntPower.raise (2, -5) + " " + Math.pow (2, -5));
    System.out.println (IntPower.raise (2, 0) + " " + Math.pow (2, 0));
    System.out.println (IntPower.raise (2.5, 5) + " " + Math.pow (2.5, 5));
    System.out.println ();
    System.out.println (IntPower.raise (-2, 5) + " " + Math.pow (-2, 5));
    System.out.println (IntPower.raise (-2, -5) + " " + Math.pow (-2, -5));
    System.out.println (IntPower.raise (-2, 0) + " " + Math.pow (-2, 0));
    System.out.println (IntPower.raise (-2.5, 5) + " " + Math.pow (-2.5, 5));
  }
}
/*
 * Output: 32.0 32.0 0.03125 0.03125 1.0 1.0 97.65625 97.65625 -32.0 -32.0
 * -0.03125 -0.03125 1.0 1.0 -97.65625 -97.65625
 */
