/**
 * Copyright (C) 2006-2012 phloc systems
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
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.math.graph.simple.SimpleDirectedGraph;
import com.phloc.math.graph.simple.SimpleDirectedGraphObjectFastFactory;
import com.phloc.math.graph.simple.SimpleGraph;
import com.phloc.math.graph.simple.SimpleGraphObjectFastFactory;

public final class DijkstraTest
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (DijkstraTest.class);
  private static final String ATTR_WEIGHT = "weight";

  @Test
  public void testBasic ()
  {
    final SimpleDirectedGraph g = new SimpleDirectedGraph (new SimpleDirectedGraphObjectFastFactory ());
    g.createNode ("O");
    g.createNode ("A");
    g.createNode ("B");
    g.createNode ("C");
    g.createNode ("D");
    g.createNode ("E");
    g.createNode ("T");
    g.createRelation ("O", "A").setAttribute (ATTR_WEIGHT, 2);
    g.createRelation ("O", "B").setAttribute (ATTR_WEIGHT, 5);
    g.createRelation ("O", "C").setAttribute (ATTR_WEIGHT, 4);
    g.createRelation ("A", "D").setAttribute (ATTR_WEIGHT, 7);
    g.createRelation ("A", "B").setAttribute (ATTR_WEIGHT, 2);
    g.createRelation ("C", "B").setAttribute (ATTR_WEIGHT, 1);
    g.createRelation ("C", "E").setAttribute (ATTR_WEIGHT, 4);
    g.createRelation ("B", "D").setAttribute (ATTR_WEIGHT, 4);
    g.createRelation ("B", "E").setAttribute (ATTR_WEIGHT, 3);
    g.createRelation ("D", "E").setAttribute (ATTR_WEIGHT, 1);
    g.createRelation ("D", "T").setAttribute (ATTR_WEIGHT, 5);
    g.createRelation ("E", "T").setAttribute (ATTR_WEIGHT, 7);
    assertEquals ("O", g.getSingleStartNode ().getID ());
    assertEquals ("T", g.getSingleEndNode ().getID ());

    final Dijkstra.Result <?> r = Dijkstra.applyDijkstra (g, "O", "T", ATTR_WEIGHT);
    assertNotNull (r);
    s_aLogger.info (r.getAsString ());
    assertEquals (13, r.getResultDistance ());
  }

  @Test
  public void example1aDirected ()
  {
    final SimpleDirectedGraph g = new SimpleDirectedGraph (new SimpleDirectedGraphObjectFastFactory ());
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
    assertEquals ("1", g.getSingleStartNode ().getID ());
    assertEquals ("6", g.getSingleEndNode ().getID ());

    final Dijkstra.Result <?> r = Dijkstra.applyDijkstra (g, "1", "6", ATTR_WEIGHT);
    assertNotNull (r);
    s_aLogger.info (r.getAsString ());
    assertEquals (5, r.getResultDistance ());
  }

  @Test
  public void example1aUndirected ()
  {
    final SimpleGraph g = new SimpleGraph (new SimpleGraphObjectFastFactory ());
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

    final Dijkstra.Result <?> r = Dijkstra.applyDijkstra (g, "1", "6", ATTR_WEIGHT);
    assertNotNull (r);
    s_aLogger.info (r.getAsString ());
    assertEquals (5, r.getResultDistance ());
  }

  @Test
  public void example2a ()
  {
    final SimpleDirectedGraph g = new SimpleDirectedGraph (new SimpleDirectedGraphObjectFastFactory ());
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
    assertEquals ("O", g.getSingleStartNode ().getID ());
    assertEquals ("T", g.getSingleEndNode ().getID ());

    final Dijkstra.Result <?> r = Dijkstra.applyDijkstra (g, "O", "T", ATTR_WEIGHT);
    assertNotNull (r);
    s_aLogger.info (r.getAsString ());
    assertEquals (24, r.getResultDistance ());
  }

  @Test
  public void testCities ()
  {
    final SimpleGraph g = new SimpleGraph (new SimpleGraphObjectFastFactory ());
    g.createNode ("Barcelona");
    g.createNode ("Narbonne");
    g.createNode ("Marseille");
    g.createNode ("Toulouse");
    g.createNode ("Geneve");
    g.createNode ("Paris");
    g.createNode ("Lausanne");
    g.createRelation ("Barcelona", "Narbonne").setAttribute (ATTR_WEIGHT, 250);
    g.createRelation ("Narbonne", "Marseille").setAttribute (ATTR_WEIGHT, 260);
    g.createRelation ("Narbonne", "Toulouse").setAttribute (ATTR_WEIGHT, 150);
    g.createRelation ("Narbonne", "Geneve").setAttribute (ATTR_WEIGHT, 550);
    g.createRelation ("Marseille", "Geneve").setAttribute (ATTR_WEIGHT, 470);
    g.createRelation ("Toulouse", "Paris").setAttribute (ATTR_WEIGHT, 680);
    g.createRelation ("Toulouse", "Geneve").setAttribute (ATTR_WEIGHT, 700);
    g.createRelation ("Geneve", "Paris").setAttribute (ATTR_WEIGHT, 540);
    g.createRelation ("Geneve", "Lausanne").setAttribute (ATTR_WEIGHT, 64);
    g.createRelation ("Lausanne", "Paris").setAttribute (ATTR_WEIGHT, 536);

    Dijkstra.Result <?> r = Dijkstra.applyDijkstra (g, "Barcelona", "Lausanne", ATTR_WEIGHT);
    assertNotNull (r);
    s_aLogger.info (r.getAsString ());
    assertEquals (864, r.getResultDistance ());

    r = Dijkstra.applyDijkstra (g, "Lausanne", "Barcelona", ATTR_WEIGHT);
    assertNotNull (r);
    s_aLogger.info (r.getAsString ());
    assertEquals (864, r.getResultDistance ());
  }
}
