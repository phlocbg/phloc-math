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

import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.math.genetic.tsp.AbstractFileBasedTSPRunner;
import com.phloc.math.genetic.tsp.TSPRunner;
import com.phloc.math.genetic.utils.random.RandomGenerator;
import com.phloc.math.genetic.utils.random.RandomGeneratorRandom;
import com.phloc.math.matrix.Matrix;

public final class MainTSPRunnerBrd14051 extends AbstractFileBasedTSPRunner
{
  public static void main (final String [] args)
  {
    // Use fixed seed
    RandomGenerator.setRandomGenerator (new RandomGeneratorRandom (new Random (471147124713L)));
    final Matrix m = readTSPFromFile (new ClassPathResource ("tsp/brd14051.tsp"), true);
    new TSPRunner ("brd14051").runWithDefaultSettings (m, 469385);
  }
}
