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
package com.phloc.math.genetic.mutation;

import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.math.genetic.model.Chromosome;
import com.phloc.math.genetic.model.IChromosome;
import com.phloc.math.genetic.model.IGene;
import com.phloc.math.genetic.utils.decisionmaker.IDecisionMaker;
import com.phloc.math.genetic.utils.random.RandomGenerator;

/**
 * Mutation that randomly moves a gene. Original:
 * 
 * <pre>
 * 1 2 3 4 5 6 7
 *    +-+   ^
 * </pre>
 * 
 * After the mutation:
 * 
 * <pre>
 * 1 2 4 5 3 6 7
 *        +-+
 * </pre>
 * 
 * Also called "Swap".
 * 
 * @author Philip Helger
 */
public class MutationRandomMoveSingle extends AbstractMutation
{
  public MutationRandomMoveSingle (@Nonnull final IDecisionMaker aDescisionMaker)
  {
    super (aDescisionMaker);
  }

  @Override
  @Nonnull
  public IChromosome executeMutation (@Nonnull final IChromosome aChromosome)
  {
    final int nGenes = aChromosome.getGeneCount ();
    if (nGenes < 2)
    {
      // Avoid endless loop in index selection below
      // If only 1 element is present there is nothing to be exchanged
      throw new IllegalArgumentException ("You need to have at least 2 genes, but you only have " + nGenes + " genes!");
    }

    final int [] aIndices = RandomGenerator.getMultipleUniqueIntsInRange (2, nGenes);
    final int nIndexSrc = aIndices[0];
    final int nIndexDst = aIndices[1];

    // Create a copy of the genes
    final List <IGene> aGenes = aChromosome.getAllGenes ();
    // Remove from the source position
    final IGene aGene = aGenes.remove (nIndexSrc);
    // and insert at the new position
    aGenes.add (nIndexDst, aGene);
    // Return the new chromosome
    return new Chromosome (aChromosome, aGenes);
  }
}
