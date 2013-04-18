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
package com.phloc.math.genetic.selector;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.math.genetic.model.ComparatorChromosomeFitness;
import com.phloc.math.genetic.model.IChromosome;
import com.phloc.math.genetic.utils.random.RandomGenerator;

/**
 * Cross over selector:
 * <ul>
 * <li>Take a subset of the best fitting chromosomes</li>
 * <li>Randomly choose a chromosome of them</li>
 * </ul>
 * 
 * @author philip
 */
public class SelectorBestSortedRandom extends AbstractSelector
{
  private final int m_nTournamentSize;

  public SelectorBestSortedRandom (@Nonnegative final int nTournamentSize)
  {
    if (nTournamentSize < 1)
      throw new IllegalArgumentException ("tournamentSize must not be smaller than 1!");
    m_nTournamentSize = nTournamentSize;
  }

  @Nonnull
  public List <IChromosome> selectSurvivingChromosomes (@Nonnull final List <IChromosome> aChromosomes)
  {
    final List <IChromosome> aSortedChromosomes = ContainerHelper.getSortedInline (aChromosomes,
                                                                                   new ComparatorChromosomeFitness (ESortOrder.DESCENDING))
                                                                 .subList (0, m_nTournamentSize);

    final int nChromosomes = aChromosomes.size ();
    final List <IChromosome> ret = new ArrayList <IChromosome> ();
    for (int i = 0; i < nChromosomes; ++i)
      ret.add (aSortedChromosomes.get (RandomGenerator.getIntInRange (m_nTournamentSize)));
    return ret;
  }
}
