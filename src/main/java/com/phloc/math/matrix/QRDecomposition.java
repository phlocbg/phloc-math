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

import javax.annotation.Nonnull;

import com.phloc.commons.math.MathHelper;

/**
 * QR Decomposition.
 * <P>
 * For an m-by-n matrix A with m >= n, the QR decomposition is an m-by-n
 * orthogonal matrix Q and an n-by-n upper triangular matrix R so that A = Q*R.
 * <P>
 * The QR decompostion always exists, even if the matrix does not have full
 * rank, so the constructor will never fail. The primary use of the QR
 * decomposition is in the least squares solution of nonsquare systems of
 * simultaneous linear equations. This will fail if isFullRank() returns false.
 */

public class QRDecomposition implements java.io.Serializable
{

  /*
   * ------------------------ Class variables ------------------------
   */

  /**
   * Array for internal storage of decomposition.
   * 
   * @serial internal array storage.
   */
  private final double [][] QR;

  /**
   * Row and column dimensions.
   * 
   * @serial column dimension.
   * @serial row dimension.
   */
  private final int m_nRows, m_nCols;

  /**
   * Array for internal storage of diagonal of R.
   * 
   * @serial diagonal of R.
   */
  private final double [] Rdiag;

  /*
   * ------------------------ Constructor ------------------------
   */

  /**
   * QR Decomposition, computed by Householder reflections. Structure to access
   * R and the Householder vectors and compute Q.
   * 
   * @param A
   *        Rectangular matrix
   */

  public QRDecomposition (final Matrix A)
  {
    // Initialize.
    QR = A.getArrayCopy ();
    m_nRows = A.getRowDimension ();
    m_nCols = A.getColumnDimension ();
    Rdiag = new double [m_nCols];

    // Main loop.
    for (int k = 0; k < m_nCols; k++)
    {
      // Compute 2-norm of k-th column without under/overflow.
      double nrm = 0;
      for (int i = k; i < m_nRows; i++)
        nrm = MathHelper.hypot (nrm, QR[i][k]);

      if (nrm != 0.0)
      {
        // Form k-th Householder vector.
        if (QR[k][k] < 0)
        {
          nrm = -nrm;
        }
        for (int i = k; i < m_nRows; i++)
        {
          QR[i][k] /= nrm;
        }
        QR[k][k] += 1.0;

        // Apply transformation to remaining columns.
        for (int j = k + 1; j < m_nCols; j++)
        {
          double s = 0.0;
          for (int i = k; i < m_nRows; i++)
          {
            s += QR[i][k] * QR[i][j];
          }
          s = -s / QR[k][k];
          for (int i = k; i < m_nRows; i++)
          {
            QR[i][j] += s * QR[i][k];
          }
        }
      }
      Rdiag[k] = -nrm;
    }
  }

  /*
   * ------------------------ Public Methods ------------------------
   */

  /**
   * Is the matrix full rank?
   * 
   * @return true if R, and hence A, has full rank.
   */
  public boolean isFullRank ()
  {
    for (int j = 0; j < m_nCols; j++)
      if (Rdiag[j] == 0)
        return false;
    return true;
  }

  /**
   * Return the Householder vectors
   * 
   * @return Lower trapezoidal matrix whose columns define the reflections
   */
  @Nonnull
  public Matrix getH ()
  {
    final Matrix X = new Matrix (m_nRows, m_nCols);
    final double [][] H = X.getArray ();
    for (int i = 0; i < m_nRows; i++)
    {
      for (int j = 0; j < m_nCols; j++)
      {
        if (i >= j)
          H[i][j] = QR[i][j];
        else
          H[i][j] = 0.0;
      }
    }
    return X;
  }

  /**
   * Return the upper triangular factor
   * 
   * @return R
   */

  public Matrix getR ()
  {
    final Matrix X = new Matrix (m_nCols, m_nCols);
    final double [][] R = X.getArray ();
    for (int i = 0; i < m_nCols; i++)
    {
      for (int j = 0; j < m_nCols; j++)
      {
        if (i < j)
          R[i][j] = QR[i][j];
        else
          if (i == j)
            R[i][j] = Rdiag[i];
          else
            R[i][j] = 0.0;
      }
    }
    return X;
  }

  /**
   * Generate and return the (economy-sized) orthogonal factor
   * 
   * @return Q
   */

  public Matrix getQ ()
  {
    final Matrix X = new Matrix (m_nRows, m_nCols);
    final double [][] Q = X.getArray ();
    for (int k = m_nCols - 1; k >= 0; k--)
    {
      for (int i = 0; i < m_nRows; i++)
        Q[i][k] = 0.0;
      Q[k][k] = 1.0;
      for (int j = k; j < m_nCols; j++)
      {
        if (QR[k][k] != 0)
        {
          double s = 0.0;
          for (int i = k; i < m_nRows; i++)
          {
            s += QR[i][k] * Q[i][j];
          }
          s = -s / QR[k][k];
          for (int i = k; i < m_nRows; i++)
          {
            Q[i][j] += s * QR[i][k];
          }
        }
      }
    }
    return X;
  }

  /**
   * Least squares solution of A*X = B
   * 
   * @param B
   *        A Matrix with as many rows as A and any number of columns.
   * @return X that minimizes the two norm of Q*R*X-B.
   * @exception IllegalArgumentException
   *            Matrix row dimensions must agree.
   * @exception RuntimeException
   *            Matrix is rank deficient.
   */
  public Matrix solve (final Matrix B)
  {
    if (B.getRowDimension () != m_nRows)
    {
      throw new IllegalArgumentException ("Matrix row dimensions must agree.");
    }
    if (!this.isFullRank ())
    {
      throw new RuntimeException ("Matrix is rank deficient.");
    }

    // Copy right hand side
    final int nx = B.getColumnDimension ();
    final double [][] X = B.getArrayCopy ();

    // Compute Y = transpose(Q)*B
    for (int k = 0; k < m_nCols; k++)
    {
      for (int j = 0; j < nx; j++)
      {
        double s = 0.0;
        for (int i = k; i < m_nRows; i++)
        {
          s += QR[i][k] * X[i][j];
        }
        s = -s / QR[k][k];
        for (int i = k; i < m_nRows; i++)
        {
          X[i][j] += s * QR[i][k];
        }
      }
    }
    // Solve R*X = Y;
    for (int k = m_nCols - 1; k >= 0; k--)
    {
      for (int j = 0; j < nx; j++)
      {
        X[k][j] /= Rdiag[k];
      }
      for (int i = 0; i < k; i++)
      {
        for (int j = 0; j < nx; j++)
        {
          X[i][j] -= X[k][j] * QR[i][k];
        }
      }
    }
    return (new Matrix (X, m_nCols, nx).getMatrix (0, m_nCols - 1, 0, nx - 1));
  }

  private static final long serialVersionUID = 1;
}
