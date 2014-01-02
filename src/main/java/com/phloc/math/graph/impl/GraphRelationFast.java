/**
 * Copyright (C) 2006-2014 phloc systems
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.math.graph.IGraphNode;

/**
 * Implementation of {@link com.phloc.math.graph.IGraphRelation} interface with
 * quick and dirty equals and hashCode (on ID only)
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class GraphRelationFast extends GraphRelation
{
  private Integer m_aHashCode;

  public GraphRelationFast (@Nonnull final IGraphNode aFrom, @Nonnull final IGraphNode aTo)
  {
    super (aFrom, aTo);
  }

  public GraphRelationFast (@Nullable final String sID, @Nonnull final IGraphNode aFrom, @Nonnull final IGraphNode aTo)
  {
    super (sID, aFrom, aTo);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof GraphRelationFast))
      return false;
    final GraphRelationFast rhs = (GraphRelationFast) o;
    return getID ().equals (rhs.getID ());
  }

  @Override
  public int hashCode ()
  {
    if (m_aHashCode == null)
      m_aHashCode = new HashCodeGenerator (this).append (getID ()).getHashCodeObj ();
    return m_aHashCode.intValue ();
  }
}
