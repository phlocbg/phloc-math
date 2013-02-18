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
package com.phloc.math.genetic.tsp.mutation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.timing.StopWatch;
import com.phloc.math.genetic.tsp.AbstractFileBasedTSPRunner;
import com.phloc.math.matrix.Matrix;

public final class TSPMutationGreedyTest
{
  public TSPMutationGreedyTest ()
  {}

  @Ignore
  @Test
  public void testPerformance ()
  {
    final Matrix aDistances = AbstractFileBasedTSPRunner.readTSPFromFile (new ClassPathResource ("tsp/usa13509.tsp"),
                                                                          true);
    assertNotNull (aDistances);
    assertEquals (13509, aDistances.getRowDimension ());

    final int nMaxElements = Math.min (200, aDistances.getRowDimension ());

    for (int nElements = 1; nElements < nMaxElements; ++nElements)
    {
      final StopWatch aSW = new StopWatch (true);
      for (int count = 0; count < 1000; ++count)
      {
        final int [] aSelectedIndices = new int [nElements];
        for (int j = 0; j < aSelectedIndices.length; ++j)
          aSelectedIndices[j] = nElements;

        TSPMutationGreedy.getGreedyOrder (aDistances.getMatrix (aSelectedIndices, aSelectedIndices));
      }
      final long nMillis = aSW.stopAndGetMillis ();
      System.out.println (nElements + " elements: " + nMillis + "ms");
    }
  }
}
