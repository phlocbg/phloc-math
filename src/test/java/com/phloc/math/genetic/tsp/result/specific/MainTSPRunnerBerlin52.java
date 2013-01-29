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
package com.phloc.math.genetic.tsp.result.specific;

import java.util.Random;

import com.phloc.commons.CGlobal;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.math.genetic.IContinuation;
import com.phloc.math.genetic.ICrossover;
import com.phloc.math.genetic.IEventHandler;
import com.phloc.math.genetic.IMutation;
import com.phloc.math.genetic.IPopulationCreator;
import com.phloc.math.genetic.ISelector;
import com.phloc.math.genetic.continuation.ContinuationKnownOptimum;
import com.phloc.math.genetic.continuation.ContinuationTimeBased;
import com.phloc.math.genetic.continuation.ContinuationTotalGeneration;
import com.phloc.math.genetic.crossover.CrossoverEdgeRecombination;
import com.phloc.math.genetic.eventhandler.EventHandlerDefault;
import com.phloc.math.genetic.model.IChromosome;
import com.phloc.math.genetic.mutation.MutationRandomPartialReverse;
import com.phloc.math.genetic.selector.SelectorAllSortedBest;
import com.phloc.math.genetic.tsp.AbstractFileBasedTSPRunner;
import com.phloc.math.genetic.tsp.TSPRunner;
import com.phloc.math.genetic.tsp.eventhandler.TSPEventHandlerLogging;
import com.phloc.math.genetic.tsp.model.TSPChromosomeValidator;
import com.phloc.math.genetic.tsp.model.TSPFitnessFunction;
import com.phloc.math.genetic.tsp.populationcreator.TSPPopulationCreatorRandom;
import com.phloc.math.genetic.utils.decisionmaker.DecisionMakerPercentage;
import com.phloc.math.genetic.utils.random.RandomGenerator;
import com.phloc.math.genetic.utils.random.RandomGeneratorRandom;
import com.phloc.math.matrix.Matrix;

public final class MainTSPRunnerBerlin52 extends AbstractFileBasedTSPRunner
{
  /**
   * Best so far:
   * 
   * <pre>
   * CrossoverPartialReverse:
   * PopulationSize=31; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (0.0); M:new DecisionMakerPercentage (75.8) ==> nMinGen=353
   * PopulationSize=32; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (0.5); M:new DecisionMakerPercentage (79.4) ==> nMinGen=287
   * PopulationSize=32; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (1.0); M:new DecisionMakerPercentage (90.2) ==> nMinGen=283
   * PopulationSize=32; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (1.1); M:new DecisionMakerPercentage (90.2) ==> nMinGen=283
   * PopulationSize=32; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (1.2); M:new DecisionMakerPercentage (90.2) ==> nMinGen=283
   * PopulationSize=32; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (1.3); M:new DecisionMakerPercentage (90.2) ==> nMinGen=283
   * PopulationSize=32; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (1.6); M:new DecisionMakerPercentage (90.2) ==> nMinGen=283
   * PopulationSize=32; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (1.7); M:new DecisionMakerPercentage (90.2) ==> nMinGen=283
   * PopulationSize=31; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (1.9); M:new DecisionMakerPercentage (75.8) ==> nMinGen=306
   * PopulationSize=31; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (1.9); M:new DecisionMakerPercentage (75.9) ==> nMinGen=306
   * PopulationSize=30; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (3.4); M:new DecisionMakerPercentage (86.7) ==> nMinGen=284
   * PopulationSize=30; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (3.5); M:new DecisionMakerPercentage (86.7) ==> nMinGen=284
   * PopulationSize=30; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (3.6); M:new DecisionMakerPercentage (86.7) ==> nMinGen=284
   * PopulationSize=30; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (3.7); M:new DecisionMakerPercentage (86.7) ==> nMinGen=284
   * PopulationSize=31; SelectorAllSortedBest(3); C:new DecisionMakerPercentage (3.1); M:new DecisionMakerPercentage (79.2) ==> nMinGen=272
   * PopulationSize=31; SelectorAllSortedBest(3); C:new DecisionMakerPercentage (3.2); M:new DecisionMakerPercentage (79.2) ==> nMinGen=272
   * PopulationSize=31; SelectorAllSortedBest(3); C:new DecisionMakerPercentage (3.3); M:new DecisionMakerPercentage (79.2) ==> nMinGen=272
   * </pre>
   * 
   * <pre>
   * CrossoverEdgeRecombination:
   * PopulationSize=18; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (0.0); M:new DecisionMakerPercentage (61.0) ==> nMinGen=451
   * PopulationSize=32; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (0.0); M:new DecisionMakerPercentage (85.5) ==> nMinGen=394
   * PopulationSize=30; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (0.8); M:new DecisionMakerPercentage (90.8) ==> nMinGen=367
   * PopulationSize=30; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (0.9); M:new DecisionMakerPercentage (90.8) ==> nMinGen=367
   * PopulationSize=30; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (1.1); M:new DecisionMakerPercentage (82.8) ==> nMinGen=360
   * PopulationSize=30; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (1.2); M:new DecisionMakerPercentage (82.8) ==> nMinGen=360
   * PopulationSize=32; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (2.7); M:new DecisionMakerPercentage (86.0) ==> nMinGen=351
   * PopulationSize=41; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (0.0); M:new DecisionMakerPercentage (81.0) ==> nMinGen=285
   * PopulationSize=48; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (0.0); M:new DecisionMakerPercentage (87.3) ==> nMinGen=218
   * PopulationSize=51; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (0.2); M:new DecisionMakerPercentage (92.9) ==> nMinGen=214
   * PopulationSize=51; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (0.3); M:new DecisionMakerPercentage (92.9) ==> nMinGen=214
   * PopulationSize=51; SelectorAllSortedBest(2); C:new DecisionMakerPercentage (0.4); M:new DecisionMakerPercentage (92.9) ==> nMinGen=214
   * </pre>
   */
  public static void main (final String [] args)
  {
    // Optimum length = 7542
    final Matrix aDistances = readTSPFromFile (new ClassPathResource ("tsp/berlin52.tsp"), true);

    final int nOptimumDistance = 7542;
    final int nCities = aDistances.getRowDimension ();
    final TSPFitnessFunction ff = new TSPFitnessFunction (aDistances);
    final TSPChromosomeValidator cv = true ? null : new TSPChromosomeValidator (nCities);

    long nMinGen = 1000;
    String sBest = "";
    for (int nBest = 2; nBest <= 2; ++nBest)
    {
      for (int nCPerc = 0; nCPerc < 40; ++nCPerc)
      {
        System.out.println ("nCPerc = " + nCPerc);
        for (int nMPerc = 800; nMPerc < 1000; ++nMPerc)
        {
          for (int nPopulationSize = 30; nPopulationSize <= nCities; nPopulationSize++)
          {
            // Use fixed seed
            RandomGenerator.setRandomGenerator (new RandomGeneratorRandom (new Random (471147124713L)));

            final IEventHandler eh = true ? new EventHandlerDefault () : new TSPEventHandlerLogging ();
            IContinuation cont = null;
            cont = new ContinuationTotalGeneration (nMinGen + 1, cont);
            cont = new ContinuationKnownOptimum (ff.getFitness (nOptimumDistance), eh, cont);
            cont = new ContinuationTimeBased (20 * CGlobal.MILLISECONDS_PER_SECOND, cont);
            final IPopulationCreator pc = new TSPPopulationCreatorRandom (nCities, nPopulationSize, ff, cv);
            final ISelector s = new SelectorAllSortedBest (nBest);
            final ICrossover c = new CrossoverEdgeRecombination (new DecisionMakerPercentage (nCPerc / 10.0));
            final IMutation m = new MutationRandomPartialReverse (new DecisionMakerPercentage (nMPerc / 10.0));

            final IChromosome aBest = new TSPRunner ("berlin52").run (aDistances,
                                                                      nOptimumDistance,
                                                                      ff,
                                                                      eh,
                                                                      cont,
                                                                      pc,
                                                                      s,
                                                                      c,
                                                                      m);
            if (ff.getDistance (aBest) == nOptimumDistance)
              if (eh.getLastGeneration () == nMinGen)
              {
                System.out.println ("PopulationSize=" +
                                    nPopulationSize +
                                    "; SelectorAllSortedBest(" +
                                    nBest +
                                    "); C:new DecisionMakerPercentage (" +
                                    (nCPerc / 10.0) +
                                    "); M:new DecisionMakerPercentage (" +
                                    (nMPerc / 10.0) +
                                    ") ==> nMinGen=" +
                                    nMinGen);
              }
              else
                if (eh.getLastGeneration () < nMinGen)
                {
                  nMinGen = eh.getLastGeneration ();
                  sBest = "PopulationSize=" +
                          nPopulationSize +
                          "; SelectorAllSortedBest(" +
                          nBest +
                          "); C:new DecisionMakerPercentage (" +
                          (nCPerc / 10.0) +
                          "); M:new DecisionMakerPercentage (" +
                          (nMPerc / 10.0) +
                          ") ==> nMinGen=" +
                          nMinGen;
                  System.out.println (sBest);
                }
          }
        }
      }
    }
    System.out.println ("Overall best: " + sBest);
  }
}
