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

import javax.annotation.Nonnull;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.math.genetic.model.ComparatorChromosomeFitness;
import com.phloc.math.genetic.model.IChromosome;

/**
 * Cross over selector:
 * <ul>
 * <li>Take all chromosomes in best fitting order</li>
 * <li>Always choose the best fitting chromosome</li>
 * </ul>
 * 
 * @author Philip Helger
 */
public class SelectorAllSortedBestOnly extends AbstractSelector
{
  public SelectorAllSortedBestOnly ()
  {}

  @Nonnull
  public List <IChromosome> selectSurvivingChromosomes (@Nonnull final List <IChromosome> aChromosomes)
  {
    // Sort all chromosomes by descending fitness
    final IChromosome aFittestChromosome = ContainerHelper.getSortedInline (aChromosomes,
                                                                            new ComparatorChromosomeFitness (ESortOrder.DESCENDING))
                                                          .get (0);

    final int nChromosomes = aChromosomes.size ();
    final List <IChromosome> ret = new ArrayList <IChromosome> ();
    for (int i = 0; i < nChromosomes; ++i)
      ret.add (aFittestChromosome);
    return ret;
  }
}
