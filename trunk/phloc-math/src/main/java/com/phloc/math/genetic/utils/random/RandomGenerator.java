/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.math.genetic.utils.random;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.random.VerySecureRandom;

/**
 * The central class for creating random values.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class RandomGenerator
{
  private static IRandomGenerator s_aRG = new RandomGeneratorRandom (VerySecureRandom.getInstance ());

  private RandomGenerator ()
  {}

  public static void setRandomGenerator (@Nonnull final IRandomGenerator aRG)
  {
    if (aRG == null)
      throw new NullPointerException ("randomGenerator");
    s_aRG = aRG;
  }

  /**
   * @return A random double value between 0 and 1
   */
  @Nonnegative
  public static double getDouble ()
  {
    return s_aRG.getDouble ();
  }

  /**
   * Get a single random int value in the range from [0 - max_value[
   * 
   * @param nMaxValueExcl
   *        The maximum value (exclusive) to be created
   * @return The random value
   */
  @Nonnegative
  public static int getIntInRange (@Nonnegative final int nMaxValueExcl)
  {
    return s_aRG.getIntInRange (nMaxValueExcl);
  }

  /**
   * Get multiple unique random int values in a range from [0 - max_value[
   * 
   * @param nCount
   *        Number of random values to create
   * @param nMaxValueExcl
   *        The maximum value (exclusive) to be created
   * @return The int array, in value ascending order. Never <code>null</code>.
   */
  @Nonnull
  @Nonempty
  public static int [] getMultipleUniqueIntsInRange (@Nonnegative final int nCount, @Nonnegative final int nMaxValueExcl)
  {
    return s_aRG.getMultipleUniqueIntsInRange (nCount, nMaxValueExcl);
  }
}
