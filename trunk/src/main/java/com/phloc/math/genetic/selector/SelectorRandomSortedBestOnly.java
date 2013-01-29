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
 * <li>Take a random subset of chromosomes to choose from</li>
 * <li>Sorted the chosen chromosomes by their fitness</li>
 * <li>Always use the absolute fittest chromosome</li>
 * </ul>
 * 
 * @author philip
 */
public class SelectorRandomSortedBestOnly extends AbstractSelector
{
  private final int m_nTournamentSize;

  public SelectorRandomSortedBestOnly (@Nonnegative final int nTournamentSize)
  {
    if (nTournamentSize < 1)
      throw new IllegalArgumentException ("tournamentSize must be at least 1");
    m_nTournamentSize = nTournamentSize;
  }

  @Nonnull
  public List <IChromosome> selectSurvivingChromosomes (@Nonnull final List <IChromosome> aChromosomes)
  {
    final int nChromosomes = aChromosomes.size ();

    // Randomly choose tournament participants
    final int [] aSelected = RandomGenerator.getMultipleUniqueIntsInRange (m_nTournamentSize, nChromosomes);

    // Retrieve the tournament participants
    final List <IChromosome> aChosen = new ArrayList <IChromosome> (m_nTournamentSize);
    for (final int nSelected : aSelected)
      aChosen.add (aChromosomes.get (nSelected));

    // Sort all chosen chromosomes by descending fitness
    final IChromosome aFittestChromosome = ContainerHelper.getSortedInline (aChosen,
                                                                            new ComparatorChromosomeFitness (ESortOrder.DESCENDING))
                                                          .get (0);
    final List <IChromosome> ret = new ArrayList <IChromosome> (nChromosomes);
    for (int i = 0; i < nChromosomes; ++i)
      ret.add (aFittestChromosome);
    return ret;
  }
}
