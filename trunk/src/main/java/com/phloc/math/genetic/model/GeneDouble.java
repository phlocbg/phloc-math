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
package com.phloc.math.genetic.model;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

@MustImplementEqualsAndHashcode
public final class GeneDouble implements IGene
{
  private final double m_dValue;

  public GeneDouble (final double dValue)
  {
    m_dValue = dValue;
  }

  public double doubleValue ()
  {
    return m_dValue;
  }

  public int intValue ()
  {
    return (int) m_dValue;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof GeneDouble))
      return false;
    final GeneDouble rhs = (GeneDouble) o;
    return EqualsUtils.equals (m_dValue, rhs.m_dValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_dValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("value", m_dValue).toString ();
  }
}
