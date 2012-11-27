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

import java.io.Serializable;

import javax.annotation.Nonnull;

/**
 * LU Decomposition.
 * <P>
 * For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n unit
 * lower triangular matrix L, an n-by-n upper triangular matrix U, and a
 * permutation vector piv of length m so that A(piv,:) = L*U. If m < n, then L
 * is m-by-m and U is m-by-n.
 * <P>
 * The LU decompostion with pivoting always exists, even if the matrix is
 * singular, so the constructor will never fail. The primary use of the LU
 * decomposition is in the solution of square systems of simultaneous linear
 * equations. This will fail if isNonsingular() returns false.
 */
public class LUDecomposition implements Serializable
{
  /**
   * Array for internal storage of decomposition.
   * 
   * @serial internal array storage.
   */
  private final double [][] m_aLU;

  /**
   * Row and column dimensions, and pivot sign.
   * 
   * @serial column dimension.
   * @serial row dimension.
   * @serial pivot sign.
   */
  private final int m_nRows, m_nCols;

  private int m_nPivSign;

  /**
   * Internal storage of pivot vector.
   * 
   * @serial pivot vector.
   */
  private final int [] m_aPivot;

  /**
   * LU Decomposition Structure to access L, U and piv.
   * 
   * @param A
   *        Rectangular matrix
   */

  public LUDecomposition (@Nonnull final Matrix A)
  {
    // Use a "left-looking", dot-product, Crout/Doolittle algorithm.

    m_aLU = A.getArrayCopy ();
    m_nRows = A.getRowDimension ();
    m_nCols = A.getColumnDimension ();
    m_aPivot = new int [m_nRows];
    for (int i = 0; i < m_nRows; i++)
      m_aPivot[i] = i;
    m_nPivSign = 1;
    double [] LUrowi;
    final double [] LUcolj = new double [m_nRows];

    // Outer loop.

    for (int j = 0; j < m_nCols; j++)
    {
      // Make a copy of the j-th column to localize references.

      for (int i = 0; i < m_nRows; i++)
        LUcolj[i] = m_aLU[i][j];

      // Apply previous transformations.

      for (int i = 0; i < m_nRows; i++)
      {
        LUrowi = m_aLU[i];

        // Most of the time is spent in the following dot product.

        final int kmax = Math.min (i, j);
        double s = 0.0;
        for (int k = 0; k < kmax; k++)
          s += LUrowi[k] * LUcolj[k];

        LUrowi[j] = LUcolj[i] -= s;
      }

      // Find pivot and exchange if necessary.

      int p = j;
      for (int i = j + 1; i < m_nRows; i++)
        if (Math.abs (LUcolj[i]) > Math.abs (LUcolj[p]))
          p = i;
      if (p != j)
      {
        for (int k = 0; k < m_nCols; k++)
        {
          final double t = m_aLU[p][k];
          m_aLU[p][k] = m_aLU[j][k];
          m_aLU[j][k] = t;
        }
        final int k = m_aPivot[p];
        m_aPivot[p] = m_aPivot[j];
        m_aPivot[j] = k;
        m_nPivSign = -m_nPivSign;
      }

      // Compute multipliers.
      if (j < m_nRows && m_aLU[j][j] != 0.0)
        for (int i = j + 1; i < m_nRows; i++)
          m_aLU[i][j] /= m_aLU[j][j];
    }
  }

  /*
   * ------------------------ Temporary, experimental code.
   * ------------------------ *\ \** LU Decomposition, computed by Gaussian
   * elimination. <P> This constructor computes L and U with the "daxpy"-based
   * elimination algorithm used in LINPACK and MATLAB. In Java, we suspect the
   * dot-product, Crout algorithm will be faster. We have temporarily included
   * this constructor until timing experiments confirm this suspicion. <P>
   * @param A Rectangular matrix
   * @param linpackflag Use Gaussian elimination. Actual value ignored.
   * @return Structure to access L, U and piv.\ public LUDecomposition (Matrix
   * A, int linpackflag) { // Initialize. LU = A.getArrayCopy(); m =
   * A.getRowDimension(); n = A.getColumnDimension(); piv = new int[m]; for (int
   * i = 0; i < m; i++) { piv[i] = i; } pivsign = 1; // Main loop. for (int k =
   * 0; k < n; k++) { // Find pivot. int p = k; for (int i = k+1; i < m; i++) {
   * if (Math.abs(LU[i][k]) > Math.abs(LU[p][k])) { p = i; } } // Exchange if
   * necessary. if (p != k) { for (int j = 0; j < n; j++) { double t = LU[p][j];
   * LU[p][j] = LU[k][j]; LU[k][j] = t; } int t = piv[p]; piv[p] = piv[k];
   * piv[k] = t; pivsign = -pivsign; } // Compute multipliers and eliminate k-th
   * column. if (LU[k][k] != 0.0) { for (int i = k+1; i < m; i++) { LU[i][k] /=
   * LU[k][k]; for (int j = k+1; j < n; j++) { LU[i][j] -= LU[i][k]*LU[k][j]; }
   * } } } } \* ------------------------ End of temporary code.
   * ------------------------
   */

  /*
   * ------------------------ Public Methods ------------------------
   */

  /**
   * Is the matrix nonsingular?
   * 
   * @return true if U, and hence A, is nonsingular.
   */
  public boolean isNonsingular ()
  {
    for (int j = 0; j < m_nCols; j++)
      if (m_aLU[j][j] == 0)
        return false;
    return true;
  }

  /**
   * Return lower triangular factor
   * 
   * @return L
   */
  @Nonnull
  public Matrix getL ()
  {
    final Matrix X = new Matrix (m_nRows, m_nCols);
    final double [][] L = X.getArray ();
    for (int i = 0; i < m_nRows; i++)
      for (int j = 0; j < m_nCols; j++)
        if (i > j)
          L[i][j] = m_aLU[i][j];
        else
          if (i == j)
            L[i][j] = 1.0;
          else
            L[i][j] = 0.0;
    return X;
  }

  /**
   * Return upper triangular factor
   * 
   * @return U
   */
  @Nonnull
  public Matrix getU ()
  {
    final Matrix X = new Matrix (m_nCols, m_nCols);
    final double [][] U = X.getArray ();
    for (int i = 0; i < m_nCols; i++)
      for (int j = 0; j < m_nCols; j++)
        if (i <= j)
          U[i][j] = m_aLU[i][j];
        else
          U[i][j] = 0.0;
    return X;
  }

  /**
   * Return pivot permutation vector
   * 
   * @return piv
   */
  @Nonnull
  public int [] getPivot ()
  {
    final int [] p = new int [m_nRows];
    for (int i = 0; i < m_nRows; i++)
      p[i] = m_aPivot[i];
    return p;
  }

  /**
   * Return pivot permutation vector as a one-dimensional double array
   * 
   * @return (double) piv
   */
  @Nonnull
  public double [] getDoublePivot ()
  {
    final double [] vals = new double [m_nRows];
    for (int i = 0; i < m_nRows; i++)
      vals[i] = m_aPivot[i];
    return vals;
  }

  /**
   * Determinant
   * 
   * @return det(A)
   * @exception IllegalArgumentException
   *            Matrix must be square
   */

  public double det ()
  {
    if (m_nRows != m_nCols)
    {
      throw new IllegalArgumentException ("Matrix must be square.");
    }
    double d = m_nPivSign;
    for (int j = 0; j < m_nCols; j++)
    {
      d *= m_aLU[j][j];
    }
    return d;
  }

  /**
   * Solve A*X = B
   * 
   * @param B
   *        A Matrix with as many rows as A and any number of columns.
   * @return X so that L*U*X = B(piv,:)
   * @exception IllegalArgumentException
   *            Matrix row dimensions must agree.
   * @exception RuntimeException
   *            Matrix is singular.
   */

  public Matrix solve (final Matrix B)
  {
    if (B.getRowDimension () != m_nRows)
      throw new IllegalArgumentException ("Matrix row dimensions must agree.");
    if (!this.isNonsingular ())
      throw new RuntimeException ("Matrix is singular.");

    // Copy right hand side with pivoting
    final int nx = B.getColumnDimension ();
    final Matrix Xmat = B.getMatrix (m_aPivot, 0, nx - 1);
    final double [][] X = Xmat.getArray ();

    // Solve L*Y = B(piv,:)
    for (int k = 0; k < m_nCols; k++)
    {
      for (int i = k + 1; i < m_nCols; i++)
        for (int j = 0; j < nx; j++)
          X[i][j] -= X[k][j] * m_aLU[i][k];
    }
    // Solve U*X = Y;
    for (int k = m_nCols - 1; k >= 0; k--)
    {
      for (int j = 0; j < nx; j++)
        X[k][j] /= m_aLU[k][k];
      for (int i = 0; i < k; i++)
        for (int j = 0; j < nx; j++)
          X[i][j] -= X[k][j] * m_aLU[i][k];
    }
    return Xmat;
  }
}
