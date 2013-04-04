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
package com.phloc.math.graph.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.phloc.commons.idfactory.IIDFactory;
import com.phloc.commons.idfactory.StringIDFromGlobalIntIDFactory;
import com.phloc.math.graph.impl.GraphObjectIDFactory;

/**
 * Test class for class {@link GraphObjectIDFactory}.
 * 
 * @author philip
 */
public final class GraphObjectIDFactoryTest
{
  @Test
  public void testAll ()
  {
    final IIDFactory <String> aOld = GraphObjectIDFactory.getIDFactory ();
    assertNull (aOld);
    try
    {
      GraphObjectIDFactory.setIDFactory (null);
      assertNotNull (GraphObjectIDFactory.createNewGraphObjectID ());
      GraphObjectIDFactory.setIDFactory (new StringIDFromGlobalIntIDFactory ());
      assertNotNull (GraphObjectIDFactory.createNewGraphObjectID ());
    }
    finally
    {
      GraphObjectIDFactory.setIDFactory (aOld);
    }
  }
}