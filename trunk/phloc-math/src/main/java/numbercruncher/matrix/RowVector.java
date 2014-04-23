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

public class RowVector extends Matrix
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
  public RowVector (final int n)
  {
    super (1, n);
  }

  /**
   * Constructor.
   * 
   * @param values
   *        the array of values
   */
  public RowVector (final float values[])
  {
    set (values);
  }

  /**
   * Constructor.
   * 
   * @param m
   *        the matrix (only the first row used)
   */
  private RowVector (final Matrix m)
  {
    set (m);
  }

  // ---------//
  // Getters //
  // ---------//

  /**
   * Return the row vector's size.
   */
  public int size ()
  {
    return m_nCols;
  }

  /**
   * Copy the values of this matrix.
   * 
   * @return the copied values
   */
  public float [] copyValues1D ()
  {
    final float v[] = new float [m_nCols];

    for (int c = 0; c < m_nCols; ++c)
    {
      v[c] = m_aValues[0][c];
    }

    return v;
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
    return m_aValues[0][i];
  }

  // ---------//
  // Setters //
  // ---------//

  /**
   * Set this row vector from a matrix. Only the first row is used.
   * 
   * @param m
   *        the matrix
   */
  private void set (final Matrix m)
  {
    this.m_nRows = 1;
    this.m_nCols = m.m_nCols;
    this.m_aValues = m.m_aValues;
  }

  /**
   * Set this row vector from an array of values.
   * 
   * @param values
   *        the array of values
   */
  protected void set (final float values[])
  {
    this.m_nRows = 1;
    this.m_nCols = values.length;
    this.m_aValues = new float [1] [];

    this.m_aValues[0] = values;
  }

  /**
   * Set the i'th value of the vector.
   * 
   * @param i
   *        the index
   * @param value
   *        the value
   */
  public void set (final int i, final float value)
  {
    m_aValues[0][i] = value;
  }

  // -------------------//
  // Vector operations //
  // -------------------//

  /**
   * Add another row vector to this row vector.
   * 
   * @param rv
   *        the other row vector
   * @return the sum row vector
   * @throws MatrixException
   *         for invalid size
   */
  public RowVector add (final RowVector rv) throws MatrixException
  {
    return new RowVector (super.add (rv));
  }

  /**
   * Subtract another row vector from this row vector.
   * 
   * @param rv
   *        the other row vector
   * @return the sum row vector
   * @throws MatrixException
   *         for invalid size
   */
  public RowVector subtract (final RowVector rv) throws MatrixException
  {
    return new RowVector (super.subtract (rv));
  }

  /**
   * Compute the Euclidean norm.
   * 
   * @return the norm
   */
  public float norm ()
  {
    double t = 0;
    for (int c = 0; c < m_nCols; ++c)
    {
      final float v = m_aValues[0][c];
      t += v * v;
    }

    return (float) Math.sqrt (t);
  }

  /**
   * Print the vector values.
   */
  public void print ()
  {
    for (int c = 0; c < m_nCols; ++c)
    {
      System.out.print ("  " + m_aValues[0][c]);
    }
    System.out.println ();
  }
}
