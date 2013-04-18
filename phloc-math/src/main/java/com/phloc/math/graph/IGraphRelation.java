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
package com.phloc.math.graph;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;

/**
 * Base interface for a single undirected graph relation.
 * 
 * @author philip
 */
@MustImplementEqualsAndHashcode
public interface IGraphRelation extends IBaseGraphRelation <IGraphNode, IGraphRelation>
{
  /**
   * @return Node1 of this relation. Never <code>null</code>.
   */
  @Nonnull
  IGraphNode getNode1 ();

  /**
   * @return The ID of node1 of this relation. Never <code>null</code>.
   */
  @Nonnull
  String getNode1ID ();

  /**
   * @return Node2 of this relation. Never <code>null</code>.
   */
  @Nonnull
  IGraphNode getNode2 ();

  /**
   * @return The ID of node2 of this relation. Never <code>null</code>.
   */
  @Nonnull
  String getNode2ID ();
}
