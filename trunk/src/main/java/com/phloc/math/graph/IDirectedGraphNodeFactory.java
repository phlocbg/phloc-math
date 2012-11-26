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
package com.phloc.math.graph;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Factory interface for creating directed graph nodes
 * 
 * @author philip
 */
public interface IDirectedGraphNodeFactory
{
  /**
   * Create a new graph node with a <code>null</code> value and add it to the
   * graph. A new ID is generated.<br>
   * Equal to calling <code>createNode (null);</code>
   * 
   * @return The created graph node. Never <code>null</code>.
   */
  @Nonnull
  IDirectedGraphNode createNode ();

  /**
   * Create a new graph node with a known ID.
   * 
   * @param sID
   *        The ID of the graph node. If it is <code>null</code> or empty a new
   *        ID is automatically created.
   * @return The created graph node. May not be <code>null</code>.
   */
  @Nonnull
  IDirectedGraphNode createNode (@Nullable String sID);
}
