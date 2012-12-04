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

import com.phloc.commons.equals.EqualsUtils;

/**
 * Cholesky Decomposition.
 * <P>
 * For a symmetric, positive definite matrix A, the Cholesky decomposition is an
 * lower triangular matrix L so that A = L*L'.
 * <P>
 * If the matrix is not symmetric or positive definite, the constructor returns
 * a partial decomposition and sets an internal flag that may be queried by the
 * isSPD() method.
 */
public class CholeskyDecomposition implements Serializable
{
  /**
   * Array for internal storage of decomposition.
   * 
   * @serial internal array storage.
   */
  private final double [][] m_aData;

  /**
   * Row and column dimension (square matrix).
   * 
   * @serial matrix dimension.
   */
  private final int m_nDim;

  /**
   * Symmetric and positive definite flag.
   * 
   * @serial is symmetric and positive definite flag.
   */
  private boolean m_bIsSPD;

  /**
   * Cholesky algorithm for symmetric and positive definite matrix. Structure to
   * access L and isspd flag.
   * 
   * @param aMatrix
   *        Square, symmetric matrix.
   */
  public CholeskyDecomposition (@Nonnull final Matrix aMatrix)
  {
    // Initialize.
    final double [][] A = aMatrix.internalGetArray ();
    m_nDim = aMatrix.getRowDimension ();
    m_aData = new double [m_nDim] [m_nDim];
    m_bIsSPD = (aMatrix.getColumnDimension () == m_nDim);
    // Main loop.
    for (int j = 0; j < m_nDim; j++)
    {
      final double [] Lrowj = m_aData[j];
      double d = 0.0;
      for (int k = 0; k < j; k++)
      {
        final double [] Lrowk = m_aData[k];
        double s = 0.0;
        for (int i = 0; i < k; i++)
        {
          s += Lrowk[i] * Lrowj[i];
        }
        Lrowj[k] = s = (A[j][k] - s) / m_aData[k][k];
        d = d + s * s;
        m_bIsSPD = m_bIsSPD && EqualsUtils.equals (A[k][j], A[j][k]);
      }
      d = A[j][j] - d;
      m_bIsSPD = m_bIsSPD && (d > 0.0);
      m_aData[j][j] = Math.sqrt (Math.max (d, 0.0));
      for (int k = j + 1; k < m_nDim; k++)
      {
        m_aData[j][k] = 0.0;
      }
    }
  }

  /*
   * ------------------------ Temporary, experimental code.
   * ------------------------ *\ \** Right Triangular Cholesky Decomposition.
   * <P> For a symmetric, positive definite matrix A, the Right Cholesky
   * decomposition is an upper triangular matrix R so that A = R'*R. This
   * constructor computes R with the Fortran inspired column oriented algorithm
   * used in LINPACK and MATLAB. In Java, we suspect a row oriented, lower
   * triangular decomposition is faster. We have temporarily included this
   * constructor here until timing experiments confirm this suspicion.\ \**
   * Array for internal storage of right triangular decomposition. **\ private
   * transient double[][] R; \** Cholesky algorithm for symmetric and positive
   * definite matrix.
   * @param A Square, symmetric matrix.
   * @param rightflag Actual value ignored.
   * @return Structure to access R and isspd flag.\ public CholeskyDecomposition
   * (Matrix Arg, int rightflag) { // Initialize. double[][] A = Arg.getArray();
   * n = Arg.getColumnDimension(); R = new double[n][n]; isspd =
   * (Arg.getColumnDimension() == n); // Main loop. for (int j = 0; j < n; j++)
   * { double d = 0.0; for (int k = 0; k < j; k++) { double s = A[k][j]; for
   * (int i = 0; i < k; i++) { s = s - R[i][k]*R[i][j]; } R[k][j] = s =
   * s/R[k][k]; d = d + s*s; isspd = isspd & (A[k][j] == A[j][k]); } d = A[j][j]
   * - d; isspd = isspd & (d > 0.0); R[j][j] = Math.sqrt(Math.max(d,0.0)); for
   * (int k = j+1; k < n; k++) { R[k][j] = 0.0; } } } \** Return upper
   * triangular factor.
   * @return R\ public Matrix getR () { return new Matrix(R,n,n); } \*
   * ------------------------ End of temporary code. ------------------------
   */

  /*
   * ------------------------ Public Methods ------------------------
   */

  /**
   * Is the matrix symmetric and positive definite?
   * 
   * @return true if A is symmetric and positive definite.
   */
  public boolean isSPD ()
  {
    return m_bIsSPD;
  }

  /**
   * Return triangular factor.
   * 
   * @return L
   */
  public Matrix getL ()
  {
    return new Matrix (m_aData, m_nDim, m_nDim);
  }

  /**
   * Solve A*X = B
   * 
   * @param B
   *        A Matrix with as many rows as A and any number of columns.
   * @return X so that L*L'*X = B
   * @exception IllegalArgumentException
   *            Matrix row dimensions must agree.
   * @exception RuntimeException
   *            Matrix is not symmetric positive definite.
   */
  @Nonnull
  public Matrix solve (@Nonnull final Matrix B)
  {
    if (B.getRowDimension () != m_nDim)
      throw new IllegalArgumentException ("Matrix row dimensions must agree.");
    if (!m_bIsSPD)
      throw new RuntimeException ("Matrix is not symmetric positive definite.");

    // Copy right hand side.
    final double [][] X = B.getArrayCopy ();
    final int nx = B.getColumnDimension ();

    // Solve L*Y = B;
    for (int k = 0; k < m_nDim; k++)
    {
      for (int j = 0; j < nx; j++)
      {
        for (int i = 0; i < k; i++)
        {
          X[k][j] -= X[i][j] * m_aData[k][i];
        }
        X[k][j] /= m_aData[k][k];
      }
    }

    // Solve L'*X = Y;
    for (int k = m_nDim - 1; k >= 0; k--)
    {
      for (int j = 0; j < nx; j++)
      {
        for (int i = k + 1; i < m_nDim; i++)
        {
          X[k][j] -= X[i][j] * m_aData[i][k];
        }
        X[k][j] /= m_aData[k][k];
      }
    }

    return new Matrix (X, m_nDim, nx);
  }
}
