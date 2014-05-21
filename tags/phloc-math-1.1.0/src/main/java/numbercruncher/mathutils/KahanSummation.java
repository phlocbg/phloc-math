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

/**
 * Implement Kahan's Summation Algorithm for the float type.
 */
public class KahanSummation
{
  /** the current running sum */
  private float sum;
  /** the current correction */
  private float correction;
  /** the current corrected added */
  private float correctedAddend;

  /**
   * Constructor.
   */
  public KahanSummation ()
  {}

  /**
   * Return the current corrected value of the running sum.
   * 
   * @return the running sum's value
   */
  public float value ()
  {
    return sum + correction;
  }

  /**
   * Return the corrected value of the current addend.
   * 
   * @return the corrected addend value
   */
  public float correctedAddend ()
  {
    return correctedAddend;
  }

  /**
   * Add the value of an addend to the running sum.
   * 
   * @param addend
   *        the value
   */
  public void add (final float addend)
  {
    // Correct the addend value and add it to the running sum.
    correctedAddend = addend + correction;
    final float tempSum = sum + correctedAddend;

    // Compute the next correction and set the running sum.
    // The parentheses are necessary to compute the high-order
    // bits of the addend.
    correction = correctedAddend - (tempSum - sum);
    sum = tempSum;
  }

  /**
   * Clear the running sum and the correction.
   */
  public void clear ()
  {
    sum = 0;
    correction = 0;
  }
}
