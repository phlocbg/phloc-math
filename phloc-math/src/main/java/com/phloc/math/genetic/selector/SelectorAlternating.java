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
package com.phloc.math.genetic.selector;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.math.genetic.IEventHandler;
import com.phloc.math.genetic.ISelector;
import com.phloc.math.genetic.model.IChromosome;

public class SelectorAlternating extends AbstractSelector
{
  private final ISelector m_aCS1;
  private final ISelector m_aCS2;
  private ISelector m_aCS;
  private final int m_nAlternatingGenerationCount;
  private final IEventHandler m_aEventHandler;

  public SelectorAlternating (@Nonnull final ISelector aCS1,
                              @Nonnull final ISelector aCS2,
                              @Nonnegative final int nAlternatingGenerationCount,
                              @Nonnull final IEventHandler aEventHandler)
  {
    super ();
    if (aCS1 == null)
      throw new NullPointerException ("crossoverSelector1");
    if (aCS2 == null)
      throw new NullPointerException ("crossoverSelector2");
    if (nAlternatingGenerationCount < 1)
      throw new IllegalArgumentException ("AlternatingGenerationCount may not be < 1: " + nAlternatingGenerationCount);
    if (aEventHandler == null)
      throw new NullPointerException ("eventHandler");
    m_aCS1 = aCS1;
    m_aCS2 = aCS2;
    m_aCS = aCS1;
    m_nAlternatingGenerationCount = nAlternatingGenerationCount;
    m_aEventHandler = aEventHandler;
  }

  /**
   * Invoked each time the cross over selector is changed
   * 
   * @param aNewCS
   *        The new crossover selector to be used. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void onCrossoverSelectionAlternation (@SuppressWarnings ("unused") @Nonnull final ISelector aNewCS)
  {}

  @Nonnull
  public List <IChromosome> selectSurvivingChromosomes (@Nonnull final List <IChromosome> aChromosomes)
  {
    if ((m_aEventHandler.getLastGeneration () % m_nAlternatingGenerationCount) == 0)
    {
      // At this generation switch to other crossover selector
      m_aCS = m_aCS == m_aCS2 ? m_aCS1 : m_aCS2;
      onCrossoverSelectionAlternation (m_aCS);
    }
    return m_aCS.selectSurvivingChromosomes (aChromosomes);
  }
}
