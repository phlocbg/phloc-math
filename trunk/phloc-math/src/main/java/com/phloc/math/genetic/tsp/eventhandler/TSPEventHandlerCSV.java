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

import au.com.bytecode.opencsv.CSVWriter;

import com.phloc.math.genetic.eventhandler.EventHandlerDefault;
import com.phloc.math.genetic.model.IChromosome;
import com.phloc.math.genetic.model.IPopulation;
import com.phloc.math.genetic.tsp.model.TSPFitnessFunction;

public class TSPEventHandlerCSV extends EventHandlerDefault
{
  private final CSVWriter m_aWriter;
  private final TSPFitnessFunction m_aFF;

  public TSPEventHandlerCSV (@Nonnull final CSVWriter aWriter, @Nonnull final TSPFitnessFunction ff)
  {
    m_aWriter = aWriter;
    m_aFF = ff;
    m_aWriter.writeNext (new String [] { "Generation", "Distance" });
  }

  @Override
  protected void internalOnNewPopulation (@Nonnull final IPopulation aPopulation)
  {
    final IChromosome aFittest = aPopulation.getFittestChromosome ();
    final int nDistance = (int) m_aFF.getDistance (aFittest);
    m_aWriter.writeNext (new String [] { Long.toString (aPopulation.getGeneration ()), Integer.toString (nDistance) });
  }
}
