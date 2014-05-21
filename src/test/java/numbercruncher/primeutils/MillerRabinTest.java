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
package numbercruncher.primeutils;

import java.util.Random;

import numbercruncher.mathutils.ModuloArithmetic;

/**
 * An implemention of the the Miller-Rabin test for primality.
 */
public class MillerRabinTest
{
  private static MillerRabinStatus status = new MillerRabinStatus ();

  /** number to test for primality */
  private final int m_nP;
  /** number of times to run the test */
  private final int m_nIterations;

  /** caller of the test */
  private final IMillerRabinCaller m_aCaller;

  /**
   * Constructor.
   * 
   * @param p
   *        the number to test for primality
   * @param iterations
   *        the number of times to run the test
   * @param caller
   *        the caller of the test
   */
  public MillerRabinTest (final int p, final int iterations, final IMillerRabinCaller caller)
  {
    this.m_nP = p;
    this.m_nIterations = iterations;
    this.m_aCaller = caller;
  }

  /**
   * Perform the Miller-Rabin test.
   * 
   * @return true if p is probably prime, false if p is composite
   */
  public boolean test ()
  {
    final Random random = new Random (0);
    int k = m_nP - 1;
    int s = 0;

    // Shift k to the right s bits to make it odd.
    while ((k & 1) == 0)
    {
      k >>= 1;
      ++s;
    }

    status.k = k;
    status.s = s;

    // Run the test with different random base values.
    for (int i = 0; i < m_nIterations; ++i)
    {

      // Generate a random integer base b.
      int b;
      while ((b = random.nextInt (m_nP)) <= 1)
      {
        // want 1 < b < p
      }

      status.b = b;

      // Composite?
      if (!test (k, s, b))
        return false; // definitely composite
    }

    return true; // most likely prime
  }

  /**
   * Perform the Miller-Rabin test.
   * 
   * @param k
   *        the value of p-1 shifted right until it is odd
   * @param s
   *        the number of right shifts
   * @return true if p is probably prime, false if p is composite
   */
  private boolean test (final int k, final int s, final int b)
  {
    final int pm1 = m_nP - 1;
    final int sm1 = s - 1;

    status.i = 0;
    status.code = MillerRabinStatus.DONT_KNOW_YET;

    int r = ModuloArithmetic.raise (b, k, m_nP); // b^k (mod p)
    status.r = r;

    if (r == 1)
    {
      status.code = MillerRabinStatus.PROBABLY_PRIME;
      reportStatus ();

      return true; // probably prime
    }

    // Loop at most s-1 times.
    int i = 0;
    while (r != pm1)
    {
      reportStatus ();
      status.i = ++i;

      if (i > sm1)
      {
        status.code = MillerRabinStatus.DEFINITELY_COMPOSITE;
        return false; // definitely composite
      }

      r = ModuloArithmetic.raise (r, 2, m_nP); // r^2 (mod p)
      status.r = r;

      if (r == 1)
      {
        status.code = MillerRabinStatus.DEFINITELY_COMPOSITE;
        reportStatus ();

        return false; // definitely composite
      }
    }

    status.code = MillerRabinStatus.PROBABLY_PRIME;
    reportStatus ();

    return true; // probably prime
  }

  /**
   * Report the test status back to the caller.
   * 
   * @param status
   *        the test status
   */
  private void reportStatus ()
  {
    if (m_aCaller != null)
      m_aCaller.reportStatus (status);
  }
}
