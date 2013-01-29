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
package com.phloc.math.genetic.crossover;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.math.genetic.model.Chromosome;
import com.phloc.math.genetic.model.IChromosome;
import com.phloc.math.genetic.model.MockFitnessFunction;
import com.phloc.math.genetic.utils.decisionmaker.DecisionMakerAlways;

/**
 * Test class for class {@link CrossoverOnePointInt}.
 * 
 * @author philip
 */
public final class CrossoverOnePointIntTest
{
  @Test
  public void testBasic ()
  {
    final CrossoverOnePointInt aCrossover = new CrossoverOnePointInt (DecisionMakerAlways.getInstance ())
    {
      @Override
      protected int getCrossoverIndex (final int nGenes)
      {
        return 2;
      }
    };
    final IChromosome c1 = Chromosome.createGenesInt (new MockFitnessFunction (), null, 0, 1, 2, 3, 4, 5);
    final IChromosome c2 = Chromosome.createGenesInt (new MockFitnessFunction (), null, 3, 1, 5, 4, 0, 2);
    final List <IChromosome> ret = aCrossover.crossover (ContainerHelper.newList (c1, c2));
    assertNotNull (ret);
    assertEquals (2, ret.size ());
    final int [] aNew0 = new int [] { 0, 1, 5, 4, 2, 3 };
    final int [] aNew1 = new int [] { 3, 1, 2, 4, 5, 0 };
    assertArrayEquals (aNew0, ret.get (0).getGeneIntArray ());
    assertArrayEquals (aNew1, ret.get (1).getGeneIntArray ());
  }
}
