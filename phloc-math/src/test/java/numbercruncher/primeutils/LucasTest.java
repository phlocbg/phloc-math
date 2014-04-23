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

import numbercruncher.mathutils.ModuloArithmetic;
import numbercruncher.mathutils.PrimeFactors;

/**
 * An implemention of the the Lucas test for primality.
 */
public class LucasTest
{
  private static LucasStatus status = new LucasStatus ();

  /** number to test for primality */
  int m_nP;
  /** prime factors of p-1 */
  int q[];
  /** caller of the test */
  ILucasCaller m_aCaller;

  /**
   * Constructor.
   * 
   * @param p
   *        the number to test for primality
   * @param caller
   *        the caller of the test
   */
  public LucasTest (final int p, final ILucasCaller caller)
  {
    this.m_nP = p;
    this.m_aCaller = caller;

    q = PrimeFactors.factorsOf (p - 1);
  }

  /**
   * Perform the Lucas test.
   * 
   * @return true if p is prime, false if p is composite
   */
  public boolean test ()
  {
    // Try integers a from 2 through p.
    for (int a = 2; a <= m_nP; ++a)
    {
      if (passPart1 (a) && passPart2 (a))
        return true; // prime
    }

    return false; // composite
  }

  /**
   * Test if integer a passes the first part of the test.
   * 
   * @param a
   *        the value of a
   * @return true if [a^(p-1)]%p == 1, else false
   */
  private boolean passPart1 (final int a)
  {
    final int exponent = m_nP - 1;
    final int value = ModuloArithmetic.raise (a, exponent, m_nP);

    // Report status back to the caller.
    if (m_aCaller != null)
    {
      status.a = a;
      status.q = 1;
      status.exponent = exponent;
      status.value = value;
      status.pass = (value == 1);

      m_aCaller.reportStatus (status);
    }

    return (value == 1); // pass if it's 1
  }

  /**
   * Test if integer a passes the second part of the test.
   * 
   * @param a
   *        the value of a
   * @return true if [a^(p-1)/q]%p != 1 for all prime factors q, else false
   */
  private boolean passPart2 (final int a)
  {
    final int pm1 = m_nP - 1;

    // Loop to try each prime factor.
    for (final int element : q)
    {
      final int exponent = pm1 / element;
      final int value = ModuloArithmetic.raise (a, exponent, m_nP);

      // Report status back to the caller.
      if (m_aCaller != null)
      {
        status.a = a;
        status.q = element;
        status.exponent = exponent;
        status.value = value;
        status.pass = (value != 1);

        m_aCaller.reportStatus (status);
      }

      if (value == 1)
        return false; // fail
    }

    return true; // pass
  }
}
