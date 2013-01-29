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
package com.phloc.math.genetic.continuation;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.math.genetic.IContinuation;
import com.phloc.math.genetic.IEventHandler;
import com.phloc.math.genetic.model.IPopulation;

public class ContinuationKnownOptimum extends AbstractContinuation
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ContinuationKnownOptimum.class);

  private final double m_dOptimumFitness;
  private final IEventHandler m_aEventHandler;

  public ContinuationKnownOptimum (@Nonnegative final double dOptimumFitness, @Nonnull final IEventHandler aEventHandler)
  {
    this (dOptimumFitness, aEventHandler, (IContinuation) null);
  }

  public ContinuationKnownOptimum (@Nonnegative final double dOptimumFitness,
                                   @Nonnull final IEventHandler aEventHandler,
                                   @Nullable final IContinuation aNestedGACallback)
  {
    super (aNestedGACallback);
    if (aEventHandler == null)
      throw new NullPointerException ("EventHandler");
    m_dOptimumFitness = dOptimumFitness;
    m_aEventHandler = aEventHandler;
  }

  @Override
  protected boolean internalShouldContinue (@Nonnull final IPopulation aPopulation)
  {
    final double dKnownBestFitness = m_aEventHandler.getFittestChromosome ().getFitness ();
    final int nCmp = Double.compare (dKnownBestFitness, m_dOptimumFitness);
    if (nCmp == 0)
    {
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Found optimum fitness of " + dKnownBestFitness + "!");
    }
    else
      if (nCmp > 0)
        s_aLogger.warn ("Found something better than the optimum: " +
                        dKnownBestFitness +
                        " vs " +
                        m_dOptimumFitness +
                        "!!!");
    return nCmp < 0;
  }
}
