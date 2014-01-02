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
package com.phloc.math.graph;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;

/**
 * Base interface for a single directed graph relation.
 * 
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public interface IDirectedGraphRelation extends IBaseGraphRelation <IDirectedGraphNode, IDirectedGraphRelation>
{
  /**
   * @return The from-node of this relation. Never <code>null</code>.
   */
  @Nonnull
  IDirectedGraphNode getFrom ();

  /**
   * @return The ID of the from-node of this relation. Never <code>null</code>.
   */
  @Nonnull
  String getFromID ();

  /**
   * @return The to-node of this relation. Never <code>null</code>.
   */
  @Nonnull
  IDirectedGraphNode getTo ();

  /**
   * @return The ID of the to-node of this relation. Never <code>null</code>.
   */
  @Nonnull
  String getToID ();
}
