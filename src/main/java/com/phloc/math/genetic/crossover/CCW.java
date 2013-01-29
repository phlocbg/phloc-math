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
package com.phloc.math.genetic.crossover;

import java.util.Arrays;
import java.util.BitSet;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.math.genetic.model.IChromosome;

/**
 * Crossover chromosome wrapper (CCW) implementation
 * 
 * @author philip
 */
class CCW
{
  protected final int [] m_aOldGenes;
  private final int [] m_aNewGenes;
  private final BitSet m_aUsedNewValues;

  public CCW (@Nonnull final IChromosome aChromosome)
  {
    if (aChromosome == null)
      throw new NullPointerException ("chromosome");

    m_aOldGenes = aChromosome.getGeneIntArray ();
    m_aNewGenes = new int [m_aOldGenes.length];
    Arrays.fill (m_aNewGenes, -1);
    m_aUsedNewValues = new BitSet (m_aOldGenes.length);
  }

  @Nonnull
  @ReturnsMutableObject (reason = "Design")
  public final int [] getOldGenes ()
  {
    return m_aOldGenes;
  }

  @Nonnegative
  public final int getOldValue (@Nonnegative final int nIndex)
  {
    return m_aOldGenes[nIndex];
  }

  public void setNewValue (@Nonnegative final int nIndex, @Nonnegative final int nValue)
  {
    m_aNewGenes[nIndex] = nValue;
    m_aUsedNewValues.set (nValue);
  }

  public final boolean isNewValueUsed (@Nonnegative final int nValue)
  {
    return m_aUsedNewValues.get (nValue);
  }

  @Nonnegative
  public final int getNextUnusedNewValue (@Nonnegative final int nIndex)
  {
    return m_aUsedNewValues.nextClearBit (nIndex);
  }

  @Nonnegative
  public final int getNewGeneCount ()
  {
    return m_aUsedNewValues.cardinality ();
  }

  @Nonnull
  @ReturnsMutableObject (reason = "Design")
  public final int [] getNewGenes ()
  {
    return m_aNewGenes;
  }
}
