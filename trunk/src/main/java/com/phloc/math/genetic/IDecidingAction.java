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
package com.phloc.math.genetic;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.math.genetic.utils.decisionmaker.IDecisionMaker;

public interface IDecidingAction
{
  /**
   * @return The underlying decision maker. Never <code>null</code>.
   */
  @Nonnull
  IDecisionMaker getDecisionMaker ();

  /**
   * @return Number of tried actions.
   */
  @Nonnegative
  int getTryCount ();

  /**
   * @return Number of real actions executed, dependent on the used decision
   *         maker. Always &le; than the try count.
   */
  @Nonnegative
  int getExecutionCount ();
}
