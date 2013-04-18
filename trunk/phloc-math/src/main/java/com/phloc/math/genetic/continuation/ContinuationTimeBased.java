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
package com.phloc.math.genetic.continuation;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.math.genetic.IContinuation;
import com.phloc.math.genetic.model.IPopulation;

public class ContinuationTimeBased extends AbstractContinuation
{
  private final long m_nMilliSecondsToRun;
  private long m_nStartTime;

  public ContinuationTimeBased (@Nonnegative final long nMilliSecondsToRun)
  {
    this (nMilliSecondsToRun, null);
  }

  public ContinuationTimeBased (@Nonnegative final long nMilliSecondsToRun,
                                @Nullable final IContinuation aNestedGACallback)
  {
    super (aNestedGACallback);
    m_nMilliSecondsToRun = nMilliSecondsToRun;
  }

  @Override
  protected void internalOnStart ()
  {
    m_nStartTime = System.currentTimeMillis ();
  }

  @Override
  protected boolean internalShouldContinue (@Nonnull final IPopulation aPopulation)
  {
    final long nRuntime = System.currentTimeMillis () - m_nStartTime;
    return nRuntime < m_nMilliSecondsToRun;
  }
}
