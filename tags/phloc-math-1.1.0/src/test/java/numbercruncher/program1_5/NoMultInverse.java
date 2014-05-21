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
package numbercruncher.program1_5;

import java.util.Random;

/**
 * PROGRAM 1-5: No Multiplicative Inverse Figure out what percentage of
 * floating-point multiplicative inverses fail.
 */
public class NoMultInverse
{
  private static final int TRIALS = 1000000; // one million

  public static void main (final String args[])
  {
    final Random random = new Random ();

    int failCount = 0;

    // Loop to perform trials.
    for (int i = 0; i < TRIALS; ++i)
    {

      // Generate a random floating-point value.
      final float a = random.nextFloat ();

      // Multiply both ways.
      final float p1 = a * (1 / a);
      final float p2 = (1 / a) * a;

      // Compare products and count the failures.
      if ((p1 != 1) || (p2 != 1))
        ++failCount;
    }

    System.out.println ((100 * failCount) / TRIALS + "% failures of the " + "multiplicative inverse law.");
  }
}
/*
 * Output: 15% failures of the multiplicative inverse law.
 */
