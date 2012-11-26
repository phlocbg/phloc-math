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
package com.phloc.math.graph.impl;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.math.graph.IGraphNode;
import com.phloc.math.graph.IGraphRelation;

/**
 * Default implementation of the {@link IGraphRelation} interface
 * 
 * @author philip
 */
@NotThreadSafe
public class GraphRelation extends AbstractBaseGraphObject implements IGraphRelation
{
  private final Set <IGraphNode> m_aNodes = new HashSet <IGraphNode> ();

  public GraphRelation (@Nonnull final IGraphNode aFrom, @Nonnull final IGraphNode aTo)
  {
    this (null, aFrom, aTo);
  }

  public GraphRelation (@Nullable final String sID, @Nonnull final IGraphNode aFrom, @Nonnull final IGraphNode aTo)
  {
    super (sID);
    if (aFrom == null)
      throw new NullPointerException ("from");
    if (aTo == null)
      throw new NullPointerException ("to");
    m_aNodes.add (aFrom);
    m_aNodes.add (aTo);
  }

  public final boolean isDirected ()
  {
    return false;
  }

  public boolean isRelatedTo (@Nullable final IGraphNode aNode)
  {
    return m_aNodes.contains (aNode);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <IGraphNode> getAllConnectedNodes ()
  {
    return ContainerHelper.newSet (m_aNodes);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllConnectedNodeIDs ()
  {
    final Set <String> ret = new HashSet <String> ();
    for (final IGraphNode aNode : m_aNodes)
      ret.add (aNode.getID ());
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final GraphRelation rhs = (GraphRelation) o;
    return m_aNodes.equals (rhs.m_aNodes);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aNodes).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("nodes", m_aNodes).toString ();
  }
}
