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
package numbercruncher.mathutils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A polynomial interpolation function.
 */
public class InterpolationPolynomial implements IEvaluatable
{
  /** number of data points */
  private int n;
  /** array of data points */
  private final DataPoint [] m_aData;
  /** divided difference table */
  private final float [][] dd;

  /**
   * Constructor.
   * 
   * @param data
   *        the array of data points
   */
  @SuppressFBWarnings ("EI_EXPOSE_REP2")
  public InterpolationPolynomial (final DataPoint [] data)
  {
    this.m_aData = data;
    this.dd = new float [data.length] [data.length];

    for (final DataPoint element : data)
    {
      addDataPoint (element);
    }
  }

  /**
   * Constructor.
   * 
   * @param maxPoints
   *        the maximum number of data points
   */
  public InterpolationPolynomial (final int maxPoints)
  {
    this.m_aData = new DataPoint [maxPoints];
    this.dd = new float [m_aData.length] [m_aData.length];
  }

  /**
   * Return the data points.
   * 
   * @return the array of data points
   */
  @SuppressFBWarnings ("EI_EXPOSE_REP")
  public DataPoint [] getDataPoints ()
  {
    return m_aData;
  }

  /**
   * Return the divided difference table.
   * 
   * @return the table
   */
  @SuppressFBWarnings ("EI_EXPOSE_REP")
  public float [][] getDividedDifferenceTable ()
  {
    return dd;
  }

  /**
   * Return the current number of data points.
   * 
   * @return the count
   */
  public int getDataPointCount ()
  {
    return n;
  }

  /**
   * Add new data point: Augment the divided difference table by appending a new
   * entry at the bottom of each column.
   * 
   * @param dataPoint
   *        the new data point
   */
  public void addDataPoint (final DataPoint dataPoint)
  {
    if (n >= m_aData.length)
      return;

    m_aData[n] = dataPoint;
    dd[n][0] = dataPoint.getY ();

    ++n;

    for (int order = 1; order < n; ++order)
    {
      final int bottom = n - order - 1;
      final float numerator = dd[bottom + 1][order - 1] - dd[bottom][order - 1];
      final float denominator = m_aData[bottom + order].getX () - m_aData[bottom].getX ();

      dd[bottom][order] = numerator / denominator;
    }
  }

  /**
   * Return the value of the polynomial interpolation function at x.
   * (Implementation of Evaluatable.)
   * 
   * @param x
   *        the value of x
   * @return the value of the function at x
   */
  public float at (final float x)
  {
    if (n < 2)
      return Float.NaN;

    float y = dd[0][0];
    float xFactor = 1;

    // Compute the value of the function.
    for (int order = 1; order < n; ++order)
    {
      xFactor = xFactor * (x - m_aData[order - 1].getX ());
      y = y + xFactor * dd[0][order];
    }

    return y;
  }

  /**
   * Reset.
   */
  public void reset ()
  {
    n = 0;
  }
}
