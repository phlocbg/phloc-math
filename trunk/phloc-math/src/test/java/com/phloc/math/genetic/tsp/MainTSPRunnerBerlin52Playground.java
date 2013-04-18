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
package com.phloc.math.genetic.tsp;

import java.util.Random;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.math.genetic.IContinuation;
import com.phloc.math.genetic.ICrossover;
import com.phloc.math.genetic.IMutation;
import com.phloc.math.genetic.IPopulationCreator;
import com.phloc.math.genetic.ISelector;
import com.phloc.math.genetic.continuation.ContinuationKnownOptimum;
import com.phloc.math.genetic.crossover.CrossoverCycle;
import com.phloc.math.genetic.eventhandler.EventHandlerDefault;
import com.phloc.math.genetic.mutation.MutationRandomPartialReverse;
import com.phloc.math.genetic.selector.SelectorAllSortedBest;
import com.phloc.math.genetic.tsp.eventhandler.TSPEventHandlerLogging;
import com.phloc.math.genetic.tsp.model.TSPChromosomeValidator;
import com.phloc.math.genetic.tsp.model.TSPFitnessFunction;
import com.phloc.math.genetic.tsp.populationcreator.TSPPopulationCreatorRandom;
import com.phloc.math.genetic.utils.decisionmaker.DecisionMakerPercentage;
import com.phloc.math.genetic.utils.random.RandomGenerator;
import com.phloc.math.genetic.utils.random.RandomGeneratorRandom;
import com.phloc.math.matrix.Matrix;

public final class MainTSPRunnerBerlin52Playground extends AbstractFileBasedTSPRunner
{
  public static void main (final String [] args)
  {
    GlobalDebug.setDebugModeDirect (true);
    final Matrix aDistances = readTSPFromFile (new ClassPathResource ("tsp/berlin52.tsp"), true);

    final int nOptimumDistance = 7542;
    final int nCities = aDistances.getRowDimension ();
    final TSPFitnessFunction ff = new TSPFitnessFunction (aDistances);
    final TSPChromosomeValidator cv = true ? null : new TSPChromosomeValidator (nCities);

    // Use fixed seed
    RandomGenerator.setRandomGenerator (new RandomGeneratorRandom (new Random (471147124713L)));

    final int nPopulationSize = 8;
    final EventHandlerDefault eh = new TSPEventHandlerLogging ();
    IContinuation cont = null;
    cont = new ContinuationKnownOptimum (ff.getFitness (nOptimumDistance), eh, cont);
    final IPopulationCreator pc = new TSPPopulationCreatorRandom (nCities, nPopulationSize, ff, cv);
    final ISelector s = new SelectorAllSortedBest (2);
    final ICrossover c = new CrossoverCycle (new DecisionMakerPercentage (2));
    final IMutation m = new MutationRandomPartialReverse (new DecisionMakerPercentage (70));

    new TSPRunner ("berlin52").run (aDistances, nOptimumDistance, ff, eh, cont, pc, s, c, m);
  }
}
