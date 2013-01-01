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
package com.phloc.math.graph.algo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.math.graph.algo.Kruskal;
import com.phloc.math.graph.simple.SimpleGraph;

public final class KruskalTest
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (KruskalTest.class);
  private static final String ATTR_WEIGHT = "weight";

  @Test
  public void testBasic ()
  {
    final SimpleGraph g = new SimpleGraph ();
    g.createNode ("1");
    g.createNode ("2");
    g.createNode ("3");
    g.createNode ("4");
    g.createNode ("5");
    g.createNode ("6");
    g.createRelation ("1", "2").setAttribute (ATTR_WEIGHT, 4);
    g.createRelation ("1", "3").setAttribute (ATTR_WEIGHT, 2);
    g.createRelation ("1", "4").setAttribute (ATTR_WEIGHT, 5);
    g.createRelation ("2", "5").setAttribute (ATTR_WEIGHT, 3);
    g.createRelation ("2", "3").setAttribute (ATTR_WEIGHT, 4);
    g.createRelation ("4", "3").setAttribute (ATTR_WEIGHT, 4);
    g.createRelation ("4", "6").setAttribute (ATTR_WEIGHT, 6);
    g.createRelation ("3", "5").setAttribute (ATTR_WEIGHT, 3);
    g.createRelation ("3", "6").setAttribute (ATTR_WEIGHT, 5);
    g.createRelation ("5", "6").setAttribute (ATTR_WEIGHT, 4);

    final Kruskal.Result r = Kruskal.applyKruskal (g, ATTR_WEIGHT);
    s_aLogger.info (r.getAsString ());
    assertEquals (16, r.getTotalWeight ());
  }

  @Test
  public void example1a ()
  {
    final SimpleGraph g = new SimpleGraph ();
    for (int i = 1; i <= 6; ++i)
      g.createNode (Integer.toString (i));
    g.createRelation ("1", "2").setAttribute (ATTR_WEIGHT, 3);
    g.createRelation ("1", "3").setAttribute (ATTR_WEIGHT, 1);
    g.createRelation ("2", "5").setAttribute (ATTR_WEIGHT, 1);
    g.createRelation ("3", "4").setAttribute (ATTR_WEIGHT, 1);
    g.createRelation ("3", "6").setAttribute (ATTR_WEIGHT, 4);
    g.createRelation ("4", "5").setAttribute (ATTR_WEIGHT, 5);
    g.createRelation ("4", "6").setAttribute (ATTR_WEIGHT, 5);
    g.createRelation ("5", "6").setAttribute (ATTR_WEIGHT, 2);

    final Kruskal.Result r = Kruskal.applyKruskal (g, ATTR_WEIGHT);
    s_aLogger.info (r.getAsString ());
    assertEquals (8, r.getTotalWeight ());
  }

  @Test
  public void example2a ()
  {
    final SimpleGraph g = new SimpleGraph ();
    g.createNode ("O");
    g.createNode ("A");
    g.createNode ("B");
    g.createNode ("C");
    g.createNode ("D");
    g.createNode ("E");
    g.createNode ("F");
    g.createNode ("H");
    g.createNode ("I");
    g.createNode ("T");
    g.createRelation ("O", "A").setAttribute (ATTR_WEIGHT, 12);
    g.createRelation ("O", "B").setAttribute (ATTR_WEIGHT, 5);
    g.createRelation ("O", "C").setAttribute (ATTR_WEIGHT, 8);
    g.createRelation ("C", "F").setAttribute (ATTR_WEIGHT, 1);
    g.createRelation ("A", "B").setAttribute (ATTR_WEIGHT, 4);
    g.createRelation ("A", "D").setAttribute (ATTR_WEIGHT, 4);
    g.createRelation ("A", "E").setAttribute (ATTR_WEIGHT, 6);
    g.createRelation ("A", "I").setAttribute (ATTR_WEIGHT, 3);
    g.createRelation ("B", "F").setAttribute (ATTR_WEIGHT, 5);
    g.createRelation ("B", "E").setAttribute (ATTR_WEIGHT, 7);
    g.createRelation ("F", "E").setAttribute (ATTR_WEIGHT, 4);
    g.createRelation ("E", "D").setAttribute (ATTR_WEIGHT, 2);
    g.createRelation ("F", "I").setAttribute (ATTR_WEIGHT, 7);
    g.createRelation ("E", "H").setAttribute (ATTR_WEIGHT, 5);
    g.createRelation ("D", "T").setAttribute (ATTR_WEIGHT, 10);
    g.createRelation ("H", "T").setAttribute (ATTR_WEIGHT, 10);
    g.createRelation ("I", "H").setAttribute (ATTR_WEIGHT, 11);

    final Kruskal.Result r = Kruskal.applyKruskal (g, ATTR_WEIGHT);
    s_aLogger.info (r.getAsString ());
    assertEquals (38, r.getTotalWeight ());
  }
}
