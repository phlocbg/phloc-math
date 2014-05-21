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
package numbercruncher.mathutils;

import java.util.Random;

/**
 * Utility class that generates exponentially-distributed random values using
 * several algorithms.
 */
public class RandomExponential
{
  private float m_fMean;

  /** generator of uniformly-distributed random values */
  private static Random gen = new Random ();

  /**
   * Set the mean.
   * 
   * @param mean
   *        the mean
   */
  public void setParameters (final float mean)
  {
    this.m_fMean = mean;
  }

  /**
   * Compute the next randomn value using the logarithm algorithm. Requires a
   * uniformly-distributed random value in [0, 1).
   */
  public float nextLog ()
  {
    // Generate a non-zero uniformly-distributed random value.
    float u;
    while ((u = gen.nextFloat ()) == 0)
    {
      // try again if 0
    }

    return (float) (-m_fMean * Math.log (u));
  }

  /**
   * Compute the next randomn value using the von Neumann algorithm. Requires
   * sequences of uniformly-distributed random values in [0, 1).
   */
  public float nextVonNeumann ()
  {
    int n;
    int k = 0;
    float u1;

    // Loop to try sequences of uniformly-distributed
    // random values.
    for (;;)
    {
      n = 1;
      u1 = gen.nextFloat ();

      float u = u1;
      float uPrev = Float.NaN;

      // Loop to generate a sequence of ramdom values
      // as long as they are decreasing.
      for (;;)
      {
        uPrev = u;
        u = gen.nextFloat ();

        // No longer decreasing?
        if (u > uPrev)
        {

          // n is even.
          if ((n & 1) == 0)
          {
            return u1 + k; // return a random value
          }

          // n is odd.
          ++k;
          break; // try another sequence
        }

        ++n;
      }
    }
  }
}
