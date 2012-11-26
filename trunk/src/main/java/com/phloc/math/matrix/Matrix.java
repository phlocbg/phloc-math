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
package com.phloc.math.matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;

import com.phloc.commons.ICloneable;
import com.phloc.commons.math.MathHelper;
import com.phloc.commons.system.SystemHelper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Jama = Java Matrix class.
 * <P>
 * The Java Matrix Class provides the fundamental operations of numerical linear
 * algebra. Various constructors create Matrices from two dimensional arrays of
 * double precision floating point numbers. Various "gets" and "sets" provide
 * access to submatrices and matrix elements. Several methods implement basic
 * matrix arithmetic, including matrix addition and multiplication, matrix
 * norms, and element-by-element array operations. Methods for reading and
 * printing matrices are also included. All the operations in this version of
 * the Matrix Class involve real matrices. Complex matrices may be handled in a
 * future version.
 * <P>
 * Five fundamental matrix decompositions, which consist of pairs or triples of
 * matrices, permutation vectors, and the like, produce results in five
 * decomposition classes. These decompositions are accessed by the Matrix class
 * to compute solutions of simultaneous linear equations, determinants, inverses
 * and other matrix functions. The five decompositions are:
 * <P>
 * <UL>
 * <LI>Cholesky Decomposition of symmetric, positive definite matrices.
 * <LI>LU Decomposition of rectangular matrices.
 * <LI>QR Decomposition of rectangular matrices.
 * <LI>Singular Value Decomposition of rectangular matrices.
 * <LI>Eigenvalue Decomposition of both symmetric and nonsymmetric square
 * matrices.
 * </UL>
 * <DL>
 * <DT><B>Example of use:</B></DT>
 * <P>
 * <DD>Solve a linear system A x = b and compute the residual norm, ||b - A x||.
 * <P>
 * 
 * <PRE>
 * double [][] vals = { { 1., 2., 3 }, { 4., 5., 6. }, { 7., 8., 10. } };
 * Matrix A = new Matrix (vals);
 * Matrix b = Matrix.random (3, 1);
 * Matrix x = A.solve (b);
 * Matrix r = A.times (x).minus (b);
 * double rnorm = r.normInf ();
 * </PRE>
 * 
 * </DD>
 * </DL>
 * 
 * @author The MathWorks, Inc. and the National Institute of Standards and
 *         Technology.
 * @version 5 August 1998
 */

public class Matrix implements Serializable, ICloneable <Matrix>
{
  /**
   * Array for internal storage of elements.
   * 
   * @serial internal array storage.
   */
  private final double [][] m_aData;

  /**
   * Row dimensions.
   * 
   * @serial row dimension.
   */
  private final int m_nRows;

  /**
   * Column dimensions.
   * 
   * @serial column dimension.
   */
  private final int m_nCols;

  /**
   * Construct an nRows-by-nCols matrix of zeros.
   * 
   * @param nRows
   *        Number of rows.
   * @param nCols
   *        Number of columns.
   */
  public Matrix (@Nonnegative final int nRows, @Nonnegative final int nCols)
  {
    m_nRows = nRows;
    m_nCols = nCols;
    m_aData = new double [nRows] [nCols];
  }

  /**
   * Construct an nRows-by-nCols constant matrix.
   * 
   * @param nRows
   *        Number of rows.
   * @param nCols
   *        Number of columns.
   * @param dValue
   *        Fill the matrix with this scalar value.
   */

  public Matrix (@Nonnegative final int nRows, @Nonnegative final int nCols, final double dValue)
  {
    if (nRows <= 0)
      throw new IllegalArgumentException ("rows may not be negative!");
    if (nCols <= 0)
      throw new IllegalArgumentException ("cols may not be negative!");
    m_nRows = nRows;
    m_nCols = nCols;
    m_aData = new double [nRows] [nCols];
    for (int i = 0; i < nRows; i++)
      for (int j = 0; j < nCols; j++)
        m_aData[i][j] = dValue;
  }

  /**
   * Construct a matrix from a 2-D array.
   * 
   * @param aOther
   *        Two-dimensional array of doubles.
   * @exception IllegalArgumentException
   *            All rows must have the same length
   * @see #constructWithCopy
   */
  @SuppressFBWarnings ("EI_EXPOSE_REP")
  public Matrix (@Nonnull final double [][] aOther)
  {
    if (aOther == null)
      throw new NullPointerException ("other");

    m_nRows = aOther.length;
    m_nCols = aOther[0].length;
    for (int i = 0; i < m_nRows; i++)
      if (aOther[i].length != m_nCols)
        throw new IllegalArgumentException ("All rows must have the same length.");
    m_aData = aOther;
  }

  /**
   * Construct a matrix quickly without checking arguments.
   * 
   * @param aOther
   *        Two-dimensional array of doubles.
   * @param nRows
   *        Number of rows.
   * @param nCols
   *        Number of columns.
   */
  @SuppressFBWarnings ("EI_EXPOSE_REP")
  public Matrix (@Nonnull final double [][] aOther, @Nonnegative final int nRows, @Nonnegative final int nCols)
  {
    if (aOther == null)
      throw new NullPointerException ("other");
    if (nRows <= 0)
      throw new IllegalArgumentException ("rows may not be negative!");
    if (nCols <= 0)
      throw new IllegalArgumentException ("cols may not be negative!");
    if (aOther.length < nRows)
      throw new IllegalArgumentException ("array is too short");
    for (int i = 0; i < nRows; i++)
      if (aOther[i].length < nCols)
        throw new IllegalArgumentException ("All rows must have the same length.");

    m_aData = aOther;
    m_nRows = nRows;
    m_nCols = nCols;
  }

  /**
   * Construct a matrix from a one-dimensional packed array
   * 
   * @param aVals
   *        One-dimensional array of doubles, packed by columns (ala Fortran).
   * @param nRows
   *        Number of rows.
   * @exception IllegalArgumentException
   *            Array length must be a multiple of nRows.
   */
  public Matrix (@Nonnull final double [] aVals, @Nonnegative final int nRows)
  {
    m_nRows = nRows;
    m_nCols = (nRows != 0 ? aVals.length / nRows : 0);
    if (nRows * m_nCols != aVals.length)
      throw new IllegalArgumentException ("Array length must be a multiple of nRows.");

    m_aData = new double [nRows] [m_nCols];
    for (int i = 0; i < nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        m_aData[i][j] = aVals[i + j * nRows];
  }

  /*
   * ------------------------ Public Methods ------------------------
   */

  /**
   * Construct a matrix from a copy of a 2-D array.
   * 
   * @param A
   *        Two-dimensional array of doubles.
   * @exception IllegalArgumentException
   *            All rows must have the same length
   */
  @Nonnull
  public static Matrix constructWithCopy (@Nonnull final double [][] A)
  {
    final int nRows = A.length;
    final int nCols = A[0].length;
    final Matrix X = new Matrix (nRows, nCols);
    final double [][] C = X.getArray ();
    for (int i = 0; i < nRows; i++)
    {
      if (A[i].length != nCols)
        throw new IllegalArgumentException ("All rows must have the same length.");
      for (int j = 0; j < nCols; j++)
        C[i][j] = A[i][j];
    }
    return X;
  }

  /**
   * Make a deep copy of a matrix
   */
  @Nonnull
  public Matrix getClone ()
  {
    final Matrix X = new Matrix (m_nRows, m_nCols);
    final double [][] C = X.m_aData;
    for (int i = 0; i < m_nRows; i++)
    {
      final double [] aCol = m_aData[i];
      System.arraycopy (aCol, 0, C[i], 0, aCol.length);
    }
    return X;
  }

  /**
   * Access the internal two-dimensional array.
   * 
   * @return Pointer to the two-dimensional array of matrix elements.
   */
  @SuppressFBWarnings ("EI_EXPOSE_REP")
  double [][] getArray ()
  {
    return m_aData;
  }

  /**
   * Copy the internal two-dimensional array.
   * 
   * @return Two-dimensional array copy of matrix elements.
   */

  public double [][] getArrayCopy ()
  {
    final double [][] C = new double [m_nRows] [m_nCols];
    for (int i = 0; i < m_nRows; i++)
    {
      for (int j = 0; j < m_nCols; j++)
      {
        C[i][j] = m_aData[i][j];
      }
    }
    return C;
  }

  /**
   * Make a one-dimensional column packed copy of the internal array.
   * 
   * @return Matrix elements packed in a one-dimensional array by columns.
   */
  @Nonnull
  public double [] getColumnPackedCopy ()
  {
    final double [] vals = new double [m_nRows * m_nCols];
    for (int j = 0; j < m_nCols; j++)
    {
      final int nRowIndex = j * m_nRows;
      for (int i = 0; i < m_nRows; i++)
        vals[i + nRowIndex] = m_aData[i][j];
    }
    return vals;
  }

  /**
   * Make a one-dimensional row packed copy of the internal array.
   * 
   * @return Matrix elements packed in a one-dimensional array by rows.
   */
  @Nonnull
  public double [] getRowPackedCopy ()
  {
    final double [] vals = new double [m_nRows * m_nCols];
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        vals[i * m_nCols + j] = m_aData[i][j];
    return vals;
  }

  /**
   * Get row dimension.
   * 
   * @return nRows, the number of rows.
   */
  @Nonnegative
  public int getRowDimension ()
  {
    return m_nRows;
  }

  /**
   * Get column dimension.
   * 
   * @return nCols, the number of columns.
   */
  @Nonnegative
  public int getColumnDimension ()
  {
    return m_nCols;
  }

  /**
   * Get a single element.
   * 
   * @param i
   *        Row index.
   * @param j
   *        Column index.
   * @return A(i,j)
   * @exception ArrayIndexOutOfBoundsException
   */
  public double get (@Nonnegative final int i, @Nonnegative final int j)
  {
    return m_aData[i][j];
  }

  /**
   * Get a submatrix.
   * 
   * @param nStartRowIndex
   *        Initial row index
   * @param nEndRowIndex
   *        Final row index
   * @param nStartColumnIndex
   *        Initial column index
   * @param nEndColumnIndex
   *        Final column index
   * @return A(i0:i1,j0:j1)
   * @exception ArrayIndexOutOfBoundsException
   *            Submatrix indices
   */
  public Matrix getMatrix (final int nStartRowIndex,
                           final int nEndRowIndex,
                           final int nStartColumnIndex,
                           final int nEndColumnIndex)
  {
    final Matrix X = new Matrix (nEndRowIndex - nStartRowIndex + 1, nEndColumnIndex - nStartColumnIndex + 1);
    final double [][] B = X.getArray ();
    try
    {
      for (int i = nStartRowIndex; i <= nEndRowIndex; i++)
        for (int j = nStartColumnIndex; j <= nEndColumnIndex; j++)
          B[i - nStartRowIndex][j - nStartColumnIndex] = m_aData[i][j];
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      throw new ArrayIndexOutOfBoundsException ("Submatrix indices");
    }
    return X;
  }

  /**
   * Get a submatrix.
   * 
   * @param aRows
   *        Array of row indices.
   * @param aCols
   *        Array of column indices.
   * @return A(r(:),c(:))
   * @exception ArrayIndexOutOfBoundsException
   *            Submatrix indices
   */
  @Nonnull
  public Matrix getMatrix (final int [] aRows, final int [] aCols)
  {
    final Matrix X = new Matrix (aRows.length, aCols.length);
    final double [][] B = X.getArray ();
    try
    {
      for (int i = 0; i < aRows.length; i++)
      {
        final int nRowIndex = aRows[i];
        for (int j = 0; j < aCols.length; j++)
          B[i][j] = m_aData[nRowIndex][aCols[j]];
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      throw new ArrayIndexOutOfBoundsException ("Submatrix indices");
    }
    return X;
  }

  /**
   * Get a submatrix.
   * 
   * @param nStartRowIndex
   *        Initial row index
   * @param nEndRowIndex
   *        Final row index
   * @param aCols
   *        Array of column indices.
   * @return A(i0:i1,c(:))
   * @exception ArrayIndexOutOfBoundsException
   *            Submatrix indices
   */
  @Nonnull
  public Matrix getMatrix (final int nStartRowIndex, final int nEndRowIndex, final int [] aCols)
  {
    final Matrix X = new Matrix (nEndRowIndex - nStartRowIndex + 1, aCols.length);
    final double [][] B = X.getArray ();
    try
    {
      for (int j = 0; j < aCols.length; j++)
      {
        final int nColIndex = aCols[j];
        for (int i = nStartRowIndex; i <= nEndRowIndex; i++)
          B[i - nStartRowIndex][j] = m_aData[i][nColIndex];
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      throw new ArrayIndexOutOfBoundsException ("Submatrix indices");
    }
    return X;
  }

  /**
   * Get a submatrix.
   * 
   * @param aRows
   *        Array of row indices.
   * @param nStartColumnIndex
   *        Initial column index
   * @param nEndColumnIndex
   *        Final column index
   * @return A(r(:),j0:j1)
   * @exception ArrayIndexOutOfBoundsException
   *            Submatrix indices
   */
  @Nonnull
  public Matrix getMatrix (@Nonnull final int [] aRows,
                           @Nonnegative final int nStartColumnIndex,
                           @Nonnegative final int nEndColumnIndex)
  {
    final Matrix X = new Matrix (aRows.length, nEndColumnIndex - nStartColumnIndex + 1);
    final double [][] B = X.getArray ();
    try
    {
      for (int i = 0; i < aRows.length; i++)
      {
        final int nRowIndex = aRows[i];
        for (int j = nStartColumnIndex; j <= nEndColumnIndex; j++)
          B[i][j - nStartColumnIndex] = m_aData[nRowIndex][j];
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      throw new ArrayIndexOutOfBoundsException ("Submatrix indices");
    }
    return X;
  }

  /**
   * Set a single element.
   * 
   * @param i
   *        Row index.
   * @param j
   *        Column index.
   * @param s
   *        A(i,j).
   * @exception ArrayIndexOutOfBoundsException
   */
  public void set (@Nonnegative final int i, @Nonnegative final int j, final double s)
  {
    m_aData[i][j] = s;
  }

  /**
   * Set a submatrix.
   * 
   * @param nStartRowIndex
   *        Initial row index
   * @param nEndRowIndex
   *        Final row index
   * @param nStartColumnIndex
   *        Initial column index
   * @param nEndColumnIndex
   *        Final column index
   * @param X
   *        A(i0:i1,j0:j1)
   * @exception ArrayIndexOutOfBoundsException
   *            Submatrix indices
   */
  public void setMatrix (final int nStartRowIndex,
                         final int nEndRowIndex,
                         final int nStartColumnIndex,
                         final int nEndColumnIndex,
                         @Nonnull final Matrix X)
  {
    try
    {
      for (int i = nStartRowIndex; i <= nEndRowIndex; i++)
        for (int j = nStartColumnIndex; j <= nEndColumnIndex; j++)
          m_aData[i][j] = X.get (i - nStartRowIndex, j - nStartColumnIndex);
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      throw new ArrayIndexOutOfBoundsException ("Submatrix indices");
    }
  }

  /**
   * Set a submatrix.
   * 
   * @param aRows
   *        Array of row indices.
   * @param aCols
   *        Array of column indices.
   * @param X
   *        A(r(:),c(:))
   * @exception ArrayIndexOutOfBoundsException
   *            Submatrix indices
   */
  public void setMatrix (final int [] aRows, final int [] aCols, final Matrix X)
  {
    try
    {
      for (int i = 0; i < aRows.length; i++)
      {
        final int nRowIndex = aRows[i];
        for (int j = 0; j < aCols.length; j++)
          m_aData[nRowIndex][aCols[j]] = X.get (i, j);
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      throw new ArrayIndexOutOfBoundsException ("Submatrix indices");
    }
  }

  /**
   * Set a submatrix.
   * 
   * @param aRows
   *        Array of row indices.
   * @param nStartColumnIndex
   *        Initial column index
   * @param nEndColumnIndex
   *        Final column index
   * @param X
   *        A(r(:),j0:j1)
   * @exception ArrayIndexOutOfBoundsException
   *            Submatrix indices
   */
  public void setMatrix (@Nonnull final int [] aRows,
                         final int nStartColumnIndex,
                         final int nEndColumnIndex,
                         final Matrix X)
  {
    try
    {
      for (int i = 0; i < aRows.length; i++)
      {
        final int nRowIndex = aRows[i];
        for (int j = nStartColumnIndex; j <= nEndColumnIndex; j++)
          m_aData[nRowIndex][j] = X.get (i, j - nStartColumnIndex);
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      throw new ArrayIndexOutOfBoundsException ("Submatrix indices");
    }
  }

  /**
   * Set a submatrix.
   * 
   * @param nStartRowIndex
   *        Initial row index
   * @param nEndRowIndex
   *        Final row index
   * @param aCols
   *        Array of column indices.
   * @param X
   *        A(i0:i1,c(:))
   * @exception ArrayIndexOutOfBoundsException
   *            Submatrix indices
   */
  public void setMatrix (final int nStartRowIndex, final int nEndRowIndex, final int [] aCols, final Matrix X)
  {
    try
    {
      for (int j = 0; j < aCols.length; j++)
      {
        final int nColIndex = aCols[j];
        for (int i = nStartRowIndex; i <= nEndRowIndex; i++)
          m_aData[i][nColIndex] = X.get (i - nStartRowIndex, j);
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      throw new ArrayIndexOutOfBoundsException ("Submatrix indices");
    }
  }

  /**
   * Matrix transpose.
   * 
   * @return A'
   */
  @Nonnull
  public Matrix transpose ()
  {
    final Matrix X = new Matrix (m_nCols, m_nRows);
    final double [][] C = X.getArray ();
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        C[j][i] = m_aData[i][j];
    return X;
  }

  /**
   * One norm
   * 
   * @return maximum column sum.
   */
  public double norm1 ()
  {
    double dRet = 0;
    for (int j = 0; j < m_nCols; j++)
    {
      double dSum = 0;
      for (int i = 0; i < m_nRows; i++)
        dSum += MathHelper.abs (m_aData[i][j]);
      dRet = Math.max (dRet, dSum);
    }
    return dRet;
  }

  /**
   * Two norm
   * 
   * @return maximum singular value.
   */
  public double norm2 ()
  {
    return new SingularValueDecomposition (this).norm2 ();
  }

  /**
   * Infinity norm
   * 
   * @return maximum row sum.
   */
  public double normInf ()
  {
    double ret = 0;
    for (int i = 0; i < m_nRows; i++)
    {
      double dSum = 0;
      for (int j = 0; j < m_nCols; j++)
        dSum += Math.abs (m_aData[i][j]);
      ret = Math.max (ret, dSum);
    }
    return ret;
  }

  /**
   * Frobenius norm
   * 
   * @return sqrt of sum of squares of all elements.
   */
  public double normF ()
  {
    double f = 0;
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        f = MathHelper.hypot (f, m_aData[i][j]);
    return f;
  }

  /**
   * Unary minus
   * 
   * @return -A
   */
  public Matrix uminus ()
  {
    final Matrix X = new Matrix (m_nRows, m_nCols);
    final double [][] C = X.getArray ();
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        C[i][j] = -m_aData[i][j];
    return X;
  }

  /** Check if size(A) == size(B) **/
  private void _checkMatrixDimensions (@Nonnull final Matrix B)
  {
    if (B.m_nRows != m_nRows)
      throw new IllegalArgumentException ("Matrix row dimensions must agree.");
    if (B.m_nCols != m_nCols)
      throw new IllegalArgumentException ("Matrix column dimensions must agree.");
  }

  /**
   * C = A + B
   * 
   * @param B
   *        another matrix
   * @return A + B
   */
  @Nonnull
  public Matrix plus (final Matrix B)
  {
    _checkMatrixDimensions (B);
    final Matrix X = new Matrix (m_nRows, m_nCols);
    final double [][] C = X.getArray ();
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        C[i][j] = m_aData[i][j] + B.m_aData[i][j];
    return X;
  }

  /**
   * A = A + B
   * 
   * @param B
   *        another matrix
   * @return A + B
   */
  @Nonnull
  public Matrix plusEquals (final Matrix B)
  {
    _checkMatrixDimensions (B);
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        m_aData[i][j] += B.m_aData[i][j];
    return this;
  }

  /**
   * C = A - B
   * 
   * @param B
   *        another matrix
   * @return A - B
   */
  @Nonnull
  public Matrix minus (final Matrix B)
  {
    _checkMatrixDimensions (B);
    final Matrix X = new Matrix (m_nRows, m_nCols);
    final double [][] C = X.getArray ();
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        C[i][j] = m_aData[i][j] - B.m_aData[i][j];
    return X;
  }

  /**
   * A = A - B
   * 
   * @param B
   *        another matrix
   * @return A - B
   */
  @Nonnull
  public Matrix minusEquals (final Matrix B)
  {
    _checkMatrixDimensions (B);
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        m_aData[i][j] -= B.m_aData[i][j];
    return this;
  }

  /**
   * Element-by-element multiplication, C = A.*B
   * 
   * @param B
   *        another matrix
   * @return A.*B
   */
  @Nonnull
  public Matrix arrayTimes (final Matrix B)
  {
    _checkMatrixDimensions (B);
    final Matrix X = new Matrix (m_nRows, m_nCols);
    final double [][] C = X.getArray ();
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        C[i][j] = m_aData[i][j] * B.m_aData[i][j];
    return X;
  }

  /**
   * Element-by-element multiplication in place, A = A.*B
   * 
   * @param B
   *        another matrix
   * @return A.*B
   */
  @Nonnull
  public Matrix arrayTimesEquals (final Matrix B)
  {
    _checkMatrixDimensions (B);
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        m_aData[i][j] *= B.m_aData[i][j];
    return this;
  }

  /**
   * Element-by-element right division, C = A./B
   * 
   * @param B
   *        another matrix
   * @return A./B
   */
  @Nonnull
  public Matrix arrayRightDivide (final Matrix B)
  {
    _checkMatrixDimensions (B);
    final Matrix X = new Matrix (m_nRows, m_nCols);
    final double [][] C = X.getArray ();
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        C[i][j] = m_aData[i][j] / B.m_aData[i][j];
    return X;
  }

  /**
   * Element-by-element right division in place, A = A./B
   * 
   * @param B
   *        another matrix
   * @return A./B
   */
  @Nonnull
  public Matrix arrayRightDivideEquals (final Matrix B)
  {
    _checkMatrixDimensions (B);
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        m_aData[i][j] = m_aData[i][j] / B.m_aData[i][j];
    return this;
  }

  /**
   * Element-by-element left division, C = B.\A
   * 
   * @param B
   *        another matrix
   * @return B.\A
   */
  @Nonnull
  public Matrix arrayLeftDivide (final Matrix B)
  {
    _checkMatrixDimensions (B);
    final Matrix X = new Matrix (m_nRows, m_nCols);
    final double [][] C = X.getArray ();
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        C[i][j] = B.m_aData[i][j] / m_aData[i][j];
    return X;
  }

  /**
   * Element-by-element left division in place, A = B.\A
   * 
   * @param B
   *        another matrix
   * @return B.\A
   */

  public Matrix arrayLeftDivideEquals (final Matrix B)
  {
    _checkMatrixDimensions (B);
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        m_aData[i][j] = B.m_aData[i][j] / m_aData[i][j];
    return this;
  }

  /**
   * Multiply a matrix by a scalar, C = s*A
   * 
   * @param s
   *        scalar
   * @return s*A
   */
  @Nonnull
  public Matrix times (final double s)
  {
    final Matrix X = new Matrix (m_nRows, m_nCols);
    final double [][] C = X.getArray ();
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        C[i][j] = s * m_aData[i][j];
    return X;
  }

  /**
   * Multiply a matrix by a scalar in place, A = s*A
   * 
   * @param s
   *        scalar
   * @return replace A by s*A
   */
  @Nonnull
  public Matrix timesEquals (final double s)
  {
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        m_aData[i][j] *= s;
    return this;
  }

  /**
   * Linear algebraic matrix multiplication, A * B
   * 
   * @param B
   *        another matrix
   * @return Matrix product, A * B
   * @exception IllegalArgumentException
   *            Matrix inner dimensions must agree.
   */
  @Nonnull
  public Matrix times (final Matrix B)
  {
    if (B.m_nRows != m_nCols)
      throw new IllegalArgumentException ("Matrix inner dimensions must agree.");

    final Matrix X = new Matrix (m_nRows, B.m_nCols);
    final double [][] C = X.getArray ();
    final double [] Bcolj = new double [m_nCols];
    for (int j = 0; j < B.m_nCols; j++)
    {
      for (int k = 0; k < m_nCols; k++)
        Bcolj[k] = B.m_aData[k][j];
      for (int i = 0; i < m_nRows; i++)
      {
        final double [] Arowi = m_aData[i];
        double s = 0;
        for (int k = 0; k < m_nCols; k++)
        {
          s += Arowi[k] * Bcolj[k];
        }
        C[i][j] = s;
      }
    }
    return X;
  }

  /**
   * LU Decomposition
   * 
   * @return LUDecomposition
   * @see LUDecomposition
   */

  public LUDecomposition lu ()
  {
    return new LUDecomposition (this);
  }

  /**
   * QR Decomposition
   * 
   * @return QRDecomposition
   * @see QRDecomposition
   */

  public QRDecomposition qr ()
  {
    return new QRDecomposition (this);
  }

  /**
   * Cholesky Decomposition
   * 
   * @return CholeskyDecomposition
   * @see CholeskyDecomposition
   */

  public CholeskyDecomposition chol ()
  {
    return new CholeskyDecomposition (this);
  }

  /**
   * Singular Value Decomposition
   * 
   * @return SingularValueDecomposition
   * @see SingularValueDecomposition
   */

  public SingularValueDecomposition svd ()
  {
    return new SingularValueDecomposition (this);
  }

  /**
   * Eigenvalue Decomposition
   * 
   * @return EigenvalueDecomposition
   * @see EigenvalueDecomposition
   */

  public EigenvalueDecomposition eig ()
  {
    return new EigenvalueDecomposition (this);
  }

  /**
   * Solve A*X = B
   * 
   * @param B
   *        right hand side
   * @return solution if A is square, least squares solution otherwise
   */

  public Matrix solve (final Matrix B)
  {
    return (m_nRows == m_nCols ? (new LUDecomposition (this)).solve (B) : (new QRDecomposition (this)).solve (B));
  }

  /**
   * Solve X*A = B, which is also A'*X' = B'
   * 
   * @param B
   *        right hand side
   * @return solution if A is square, least squares solution otherwise.
   */

  public Matrix solveTranspose (final Matrix B)
  {
    return transpose ().solve (B.transpose ());
  }

  /**
   * Matrix inverse or pseudoinverse
   * 
   * @return inverse(A) if A is square, pseudoinverse otherwise.
   */

  public Matrix inverse ()
  {
    return solve (identity (m_nRows, m_nRows));
  }

  /**
   * Matrix determinant
   * 
   * @return determinant
   */

  public double det ()
  {
    return new LUDecomposition (this).det ();
  }

  /**
   * Matrix rank
   * 
   * @return effective numerical rank, obtained from SVD.
   */

  public int rank ()
  {
    return new SingularValueDecomposition (this).rank ();
  }

  /**
   * Matrix condition (2 norm)
   * 
   * @return ratio of largest to smallest singular value.
   */

  public double cond ()
  {
    return new SingularValueDecomposition (this).cond ();
  }

  /**
   * Matrix trace.
   * 
   * @return sum of the diagonal elements.
   */

  public double trace ()
  {
    double t = 0;
    for (int i = 0; i < Math.min (m_nRows, m_nCols); i++)
    {
      t += m_aData[i][i];
    }
    return t;
  }

  /**
   * Generate matrix with random elements
   * 
   * @param nRows
   *        Number of rows.
   * @param nCols
   *        Number of colums.
   * @return An nRows-by-nCols matrix with uniformly distributed random
   *         elements.
   */

  public static Matrix random (final int nRows, final int nCols)
  {
    final Matrix A = new Matrix (nRows, nCols);
    final double [][] X = A.getArray ();
    for (int i = 0; i < nRows; i++)
    {
      for (int j = 0; j < nCols; j++)
      {
        X[i][j] = Math.random ();
      }
    }
    return A;
  }

  /**
   * Generate identity matrix
   * 
   * @param nRows
   *        Number of rows.
   * @param nCols
   *        Number of colums.
   * @return An nRows-by-nCols matrix with ones on the diagonal and zeros
   *         elsewhere.
   */

  public static Matrix identity (final int nRows, final int nCols)
  {
    final Matrix A = new Matrix (nRows, nCols);
    final double [][] X = A.getArray ();
    for (int i = 0; i < nRows; i++)
    {
      for (int j = 0; j < nCols; j++)
      {
        X[i][j] = (i == j ? 1.0 : 0.0);
      }
    }
    return A;
  }

  /**
   * Print the matrix to stdout. Line the elements up in columns with a
   * Fortran-like 'Fw.d' style format.
   * 
   * @param w
   *        Column width.
   * @param d
   *        Number of digits after the decimal.
   */
  public void print (final int w, final int d)
  {
    print (new PrintWriter (new OutputStreamWriter (System.out, SystemHelper.getSystemCharset ()), true), w, d);
  }

  /**
   * Print the matrix to the output stream. Line the elements up in columns with
   * a Fortran-like 'Fw.d' style format.
   * 
   * @param output
   *        Output stream.
   * @param w
   *        Column width.
   * @param d
   *        Number of digits after the decimal.
   */

  public void print (final PrintWriter output, final int w, final int d)
  {
    final DecimalFormat format = new DecimalFormat ();
    format.setDecimalFormatSymbols (new DecimalFormatSymbols (Locale.US));
    format.setMinimumIntegerDigits (1);
    format.setMaximumFractionDigits (d);
    format.setMinimumFractionDigits (d);
    format.setGroupingUsed (false);
    print (output, format, w + 2);
  }

  /**
   * Print the matrix to stdout. Line the elements up in columns. Use the format
   * object, and right justify within columns of width characters. Note that is
   * the matrix is to be read back in, you probably will want to use a
   * NumberFormat that is set to US Locale.
   * 
   * @param format
   *        A Formatting object for individual elements.
   * @param width
   *        Field width for each column.
   * @see java.text.DecimalFormat#setDecimalFormatSymbols
   */

  public void print (final NumberFormat format, final int width)
  {
    print (new PrintWriter (new OutputStreamWriter (System.out, SystemHelper.getSystemCharset ()), true), format, width);
  }

  // DecimalFormat is a little disappointing coming from Fortran or C's printf.
  // Since it doesn't pad on the left, the elements will come out different
  // widths. Consequently, we'll pass the desired column width in as an
  // argument and do the extra padding ourselves.

  /**
   * Print the matrix to the output stream. Line the elements up in columns. Use
   * the format object, and right justify within columns of width characters.
   * Note that is the matrix is to be read back in, you probably will want to
   * use a NumberFormat that is set to US Locale.
   * 
   * @param output
   *        the output stream.
   * @param format
   *        A formatting object to format the matrix elements
   * @param width
   *        Column width.
   * @see java.text.DecimalFormat#setDecimalFormatSymbols
   */

  public void print (final PrintWriter output, final NumberFormat format, final int width)
  {
    output.println (); // start on new line.
    for (int i = 0; i < m_nRows; i++)
    {
      for (int j = 0; j < m_nCols; j++)
      {
        final String s = format.format (m_aData[i][j]); // format the number
        final int padding = Math.max (1, width - s.length ()); // At _least_ 1
                                                               // space
        for (int k = 0; k < padding; k++)
          output.print (' ');
        output.print (s);
      }
      output.println ();
    }
    output.println (); // end with blank line.
  }

  /**
   * Read a matrix from a stream. The format is the same the print method, so
   * printed matrices can be read back in (provided they were printed using US
   * Locale). Elements are separated by whitespace, all the elements for each
   * row appear on a single line, the last row is followed by a blank line.
   * 
   * @param input
   *        the input stream.
   */

  public static Matrix read (@WillNotClose final BufferedReader input) throws java.io.IOException
  {
    final StreamTokenizer tokenizer = new StreamTokenizer (input);

    // Although StreamTokenizer will parse numbers, it doesn't recognize
    // scientific notation (E or D); however, Double.valueOf does.
    // The strategy here is to disable StreamTokenizer's number parsing.
    // We'll only get whitespace delimited words, EOL's and EOF's.
    // These words should all be numbers, for Double.valueOf to parse.

    tokenizer.resetSyntax ();
    tokenizer.wordChars (0, 255);
    tokenizer.whitespaceChars (0, ' ');
    tokenizer.eolIsSignificant (true);
    final List <Double> vD = new ArrayList <Double> ();

    // Ignore initial empty lines
    while (tokenizer.nextToken () == StreamTokenizer.TT_EOL)
    {}
    if (tokenizer.ttype == StreamTokenizer.TT_EOF)
      throw new IOException ("Unexpected EOF on matrix read.");
    do
    {
      vD.add (Double.valueOf (tokenizer.sval)); // Read & store 1st row.
    } while (tokenizer.nextToken () == StreamTokenizer.TT_WORD);

    final int nCols = vD.size (); // Now we've got the number of columns!
    double row[] = new double [nCols];
    for (int j = 0; j < nCols; j++)
      // extract the elements of the 1st row.
      row[j] = vD.get (j).doubleValue ();
    final Vector <double []> v = new Vector <double []> ();
    v.add (row); // Start storing rows instead of columns.
    while (tokenizer.nextToken () == StreamTokenizer.TT_WORD)
    {
      // While non-empty lines
      row = new double [nCols];
      v.add (row);
      int j = 0;
      do
      {
        if (j >= nCols)
          throw new IOException ("Row " + v.size () + " is too long.");
        row[j++] = Double.valueOf (tokenizer.sval).doubleValue ();
      } while (tokenizer.nextToken () == StreamTokenizer.TT_WORD);
      if (j < nCols)
        throw new IOException ("Row " + v.size () + " is too short.");
    }
    final int nRows = v.size (); // Now we've got the number of rows.
    final double [][] A = new double [nRows] [];
    v.copyInto (A); // copy the rows out of the vector
    return new Matrix (A);
  }
}
