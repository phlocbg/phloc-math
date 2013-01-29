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
package com.phloc.math.genetic.tsp.eventhandler;

import javax.annotation.Nonnull;

import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.collections.primitives.IntList;

import com.phloc.math.genetic.eventhandler.EventHandlerDefault;
import com.phloc.math.genetic.model.IChromosome;
import com.phloc.math.genetic.model.IPopulation;
import com.phloc.math.genetic.tsp.model.TSPFitnessFunction;

public class TSPEventHandlerGenerationTracker extends EventHandlerDefault
{
  private final TSPFitnessFunction m_aFF;
  private final IntList m_aDistanceListPerPopulation;
  private final IntList m_aDistanceListBest;

  public TSPEventHandlerGenerationTracker (@Nonnull final TSPFitnessFunction ff)
  {
    m_aFF = ff;
    m_aDistanceListPerPopulation = new ArrayIntList ();
    m_aDistanceListBest = new ArrayIntList ();
  }

  @Override
  protected void internalOnNewPopulation (@Nonnull final IPopulation aPopulation)
  {
    final IChromosome aFittest = aPopulation.getFittestChromosome ();
    final int nDistance = (int) m_aFF.getDistance (aFittest);
    m_aDistanceListPerPopulation.add (nDistance);
    if (m_aDistanceListBest.isEmpty ())
      m_aDistanceListBest.add (nDistance);
    else
    {
      final int nLast = m_aDistanceListBest.get (m_aDistanceListBest.size () - 1);
      if (nDistance < nLast)
        m_aDistanceListBest.add (nDistance);
      else
        m_aDistanceListBest.add (nLast);
    }
  }

  @Nonnull
  public IntList getDistanceListPerPopulation ()
  {
    return m_aDistanceListPerPopulation;
  }

  @Nonnull
  public IntList getDistanceListBest ()
  {
    return m_aDistanceListBest;
  }
}
