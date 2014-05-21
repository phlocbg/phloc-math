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
package numbercruncher.program3_4;

import numbercruncher.mathutils.IEEE754;

/**
 * PROGRAM 3-4: One Third Investigate the floating-point representation of 1/3.
 */
public class OneThird
{
  public static void main (final String args[])
  {
    final float fThird = 1 / 3f;
    final double dConverted = fThird;
    final double dThird = 1 / 3d;

    System.out.println ("          Float 1/3 = " + fThird);
    System.out.println ("Converted to double = " + dConverted);
    System.out.println ("         Double 1/3 = " + dThird);

    final IEEE754 ieeeFThird = new IEEE754 (fThird);
    final IEEE754 ieeeDConverted = new IEEE754 (dConverted);
    final IEEE754 ieeeDThird = new IEEE754 (dThird);

    ieeeFThird.print ();
    ieeeDConverted.print ();
    ieeeDThird.print ();

    // Prepend the leading 0 bits of the converted 1/3.
    int unbiased = ieeeDConverted.unbiasedExponent ();
    String bits = "1" + ieeeDConverted.fractionBits ();
    while (++unbiased < 0)
      bits = "0" + bits;

    // Sum the indicated negative powers of 2.
    double sum = 0;
    double power = 0.5;
    for (int i = 0; i < bits.length (); ++i)
    {
      if (bits.charAt (i) == '1')
        sum += power;
      power /= 2;
    }

    System.out.println ();
    System.out.println ("Converted 1/3 by summation = " + sum);
  }
}
/*
 * Output: Float 1/3 = 0.33333334 Converted to double = 0.3333333432674408
 * Double 1/3 = 0.3333333333333333 ------------------------------ float value =
 * 0.33333334 sign=0, exponent=01111101 (biased=125, normalized, unbiased=-2)
 * significand=1.01010101010101010101011 ------------------------------ double
 * value = 0.3333333432674408 sign=0, exponent=01111111101 (biased=1021,
 * normalized, unbiased=-2)
 * significand=1.0101010101010101010101100000000000000000000000000000
 * ------------------------------ double value = 0.3333333333333333 sign=0,
 * exponent=01111111101 (biased=1021, normalized, unbiased=-2)
 * significand=1.0101010101010101010101010101010101010101010101010101 Converted
 * 1/3 by summation = 0.3333333432674408
 */
