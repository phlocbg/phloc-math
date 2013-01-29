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
package com.phloc.math.genetic.utils.decisionmaker;

import com.phloc.math.genetic.utils.random.RandomGenerator;

public abstract class AbstractDecisionMakerRandom implements IDecisionMaker
{
  /**
   * Determine whether we should do it based on a certain random number.
   * 
   * @param dRandom
   *        A random number in the range of 0.0 and 100.0
   * @return <code>true</code> to do it, <code>false</code> to not do it.
   */
  public abstract boolean useRandomNumber (double dRandom);

  public final int getNextRandom ()
  {
    return RandomGenerator.getIntInRange (10000 + 1);
  }

  public final boolean shouldDoIt ()
  {
    final int nRandom = getNextRandom ();
    return useRandomNumber (nRandom / 100.0);
  }
}
