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
package numbercruncher.matrix;

/**
 * A column vector.
 */
public class ColumnVector extends Matrix
{
  // --------------//
  // Constructors //
  // --------------//

  /**
   * Constructor.
   * 
   * @param n
   *        the number of elements
   */
  public ColumnVector (final int n)
  {
    super (n, 1);
  }

  /**
   * Constructor.
   * 
   * @param values
   *        the array of values
   */
  public ColumnVector (final float [] values)
  {
    set (values);
  }

  /**
   * Constructor.
   * 
   * @param m
   *        the matrix (only the first column used)
   */
  private ColumnVector (final Matrix m)
  {
    set (m);
  }

  // ---------//
  // Getters //
  // ---------//

  /**
   * Return this column vector's size.
   */
  public int size ()
  {
    return m_nRows;
  }

  /**
   * Return the i'th value of the vector.
   * 
   * @param i
   *        the index
   * @return the value
   */
  public float at (final int i)
  {
    return m_aValues[i][0];
  }

  /**
   * Copy the values of this matrix.
   * 
   * @return the copied values
   */
  public float [] copyValues1D ()
  {
    final float v[] = new float [m_nRows];

    for (int r = 0; r < m_nRows; ++r)
    {
      v[r] = m_aValues[r][0];
    }

    return v;
  }

  // ---------//
  // Setters //
  // ---------//

  /**
   * Set this column vector from a matrix. Only the first column is used.
   * 
   * @param m
   *        the matrix
   */
  private void set (final Matrix m)
  {
    this.m_nRows = m.m_nRows;
    this.m_nCols = 1;
    this.m_aValues = m.m_aValues;
  }

  /**
   * Set this column vector from an array of values.
   * 
   * @param values
   *        the array of values
   */
  protected void set (final float values[])
  {
    this.m_nRows = values.length;
    this.m_nCols = 1;
    this.m_aValues = new float [m_nRows] [1];

    for (int r = 0; r < m_nRows; ++r)
    {
      this.m_aValues[r][0] = values[r];
    }
  }

  /**
   * Set the value of the i'th element.
   * 
   * @param i
   *        the index
   * @param value
   *        the value
   */
  public void set (final int i, final float value)
  {
    m_aValues[i][0] = value;
  }

  // -------------------//
  // Vector operations //
  // -------------------//

  /**
   * Add another column vector to this column vector.
   * 
   * @param cv
   *        the other column vector
   * @return the sum column vector
   * @throws MatrixException
   *         for invalid size
   */
  public ColumnVector add (final ColumnVector cv) throws MatrixException
  {
    return new ColumnVector (super.add (cv));
  }

  /**
   * Subtract another column vector from this column vector.
   * 
   * @param cv
   *        the other column vector
   * @return the sum column vector
   * @throws MatrixException
   *         for invalid size
   */
  public ColumnVector subtract (final ColumnVector cv) throws MatrixException
  {
    return new ColumnVector (super.subtract (cv));
  }

  /**
   * Compute the Euclidean norm.
   * 
   * @return the norm
   */
  public float norm ()
  {
    double t = 0;

    for (int r = 0; r < m_nRows; ++r)
    {
      final float v = m_aValues[r][0];
      t += v * v;
    }

    return (float) Math.sqrt (t);
  }

  /**
   * Print the vector values.
   */
  public void print ()
  {
    for (int r = 0; r < m_nRows; ++r)
    {
      System.out.print ("  " + m_aValues[r][0]);
    }
    System.out.println ();
  }
}
