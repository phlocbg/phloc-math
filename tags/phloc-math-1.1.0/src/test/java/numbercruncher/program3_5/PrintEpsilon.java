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
package numbercruncher.program3_5;

import numbercruncher.mathutils.Epsilon;
import numbercruncher.mathutils.IEEE754;

/**
 * PROGRAM 3-5: Print Machine Epsilon Decompose and print the machine epsilon
 * for the float and double types.
 */
public class PrintEpsilon
{
  public static void main (final String args[])
  {
    (new IEEE754 (Epsilon.floatValue ())).print ();
    (new IEEE754 (Epsilon.doubleValue ())).print ();
  }
}
/*
 * Output: ------------------------------ float value = 5.9604645E-8 sign=0,
 * exponent=01100111 (biased=103, normalized, unbiased=-24)
 * significand=1.00000000000000000000000 ------------------------------ double
 * value = 1.1102230246251565E-16 sign=0, exponent=01111001010 (biased=970,
 * normalized, unbiased=-53)
 * significand=1.0000000000000000000000000000000000000000000000000000
 */
