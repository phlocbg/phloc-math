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

import com.phloc.commons.math.MathHelper;

/**
 * Singular Value Decomposition.
 * <P>
 * For an m-by-n matrix A with m >= n, the singular value decomposition is an
 * m-by-n orthogonal matrix U, an n-by-n diagonal matrix S, and an n-by-n
 * orthogonal matrix V so that A = U*S*V'.
 * <P>
 * The singular values, sigma[k] = S[k][k], are ordered so that sigma[0] >=
 * sigma[1] >= ... >= sigma[n-1].
 * <P>
 * The singular value decompostion always exists, so the constructor will never
 * fail. The matrix condition number and the effective numerical rank can be
 * computed from this decomposition.
 */

public class SingularValueDecomposition implements java.io.Serializable
{

  /*
   * ------------------------ Class variables ------------------------
   */

  /**
   * Arrays for internal storage of U and V.
   * 
   * @serial internal storage of U.
   * @serial internal storage of V.
   */
  private final double [][] U, V;

  /**
   * Array for internal storage of singular values.
   * 
   * @serial internal storage of singular values.
   */
  private final double [] s;

  /**
   * Row and column dimensions.
   * 
   * @serial row dimension.
   * @serial column dimension.
   */
  private final int m_nRows, m_nCols;

  /*
   * ------------------------ Constructor ------------------------
   */

  /**
   * Construct the singular value decomposition Structure to access U, S and V.
   * 
   * @param Arg
   *        Rectangular matrix
   */

  public SingularValueDecomposition (final Matrix Arg)
  {

    // Derived from LINPACK code.
    // Initialize.
    final double [][] A = Arg.getArrayCopy ();
    m_nRows = Arg.getRowDimension ();
    m_nCols = Arg.getColumnDimension ();

    /*
     * Apparently the failing cases are only a proper subset of (m<n), so let's
     * not throw error. Correct fix to come later? if (m<n) { throw new
     * IllegalArgumentException("Jama SVD only works for m >= n"); }
     */
    final int nu = Math.min (m_nRows, m_nCols);
    s = new double [Math.min (m_nRows + 1, m_nCols)];
    U = new double [m_nRows] [nu];
    V = new double [m_nCols] [m_nCols];
    final double [] e = new double [m_nCols];
    final double [] work = new double [m_nRows];
    final boolean wantu = true;
    final boolean wantv = true;

    // Reduce A to bidiagonal form, storing the diagonal elements
    // in s and the super-diagonal elements in e.

    final int nct = Math.min (m_nRows - 1, m_nCols);
    final int nrt = Math.max (0, Math.min (m_nCols - 2, m_nRows));
    for (int k = 0; k < Math.max (nct, nrt); k++)
    {
      if (k < nct)
      {

        // Compute the transformation for the k-th column and
        // place the k-th diagonal in s[k].
        // Compute 2-norm of k-th column without under/overflow.
        s[k] = 0;
        for (int i = k; i < m_nRows; i++)
        {
          s[k] = MathHelper.hypot (s[k], A[i][k]);
        }
        if (s[k] != 0.0)
        {
          if (A[k][k] < 0.0)
          {
            s[k] = -s[k];
          }
          for (int i = k; i < m_nRows; i++)
          {
            A[i][k] /= s[k];
          }
          A[k][k] += 1.0;
        }
        s[k] = -s[k];
      }
      for (int j = k + 1; j < m_nCols; j++)
      {
        if ((k < nct) && (s[k] != 0.0))
        {

          // Apply the transformation.

          double t = 0;
          for (int i = k; i < m_nRows; i++)
          {
            t += A[i][k] * A[i][j];
          }
          t = -t / A[k][k];
          for (int i = k; i < m_nRows; i++)
          {
            A[i][j] += t * A[i][k];
          }
        }

        // Place the k-th row of A into e for the
        // subsequent calculation of the row transformation.

        e[j] = A[k][j];
      }
      if (wantu & (k < nct))
      {

        // Place the transformation in U for subsequent back
        // multiplication.

        for (int i = k; i < m_nRows; i++)
        {
          U[i][k] = A[i][k];
        }
      }
      if (k < nrt)
      {

        // Compute the k-th row transformation and place the
        // k-th super-diagonal in e[k].
        // Compute 2-norm without under/overflow.
        e[k] = 0;
        for (int i = k + 1; i < m_nCols; i++)
        {
          e[k] = MathHelper.hypot (e[k], e[i]);
        }
        if (e[k] != 0.0)
        {
          if (e[k + 1] < 0.0)
          {
            e[k] = -e[k];
          }
          for (int i = k + 1; i < m_nCols; i++)
          {
            e[i] /= e[k];
          }
          e[k + 1] += 1.0;
        }
        e[k] = -e[k];
        if ((k + 1 < m_nRows) && (e[k] != 0.0))
        {

          // Apply the transformation.

          for (int i = k + 1; i < m_nRows; i++)
          {
            work[i] = 0.0;
          }
          for (int j = k + 1; j < m_nCols; j++)
          {
            for (int i = k + 1; i < m_nRows; i++)
            {
              work[i] += e[j] * A[i][j];
            }
          }
          for (int j = k + 1; j < m_nCols; j++)
          {
            final double t = -e[j] / e[k + 1];
            for (int i = k + 1; i < m_nRows; i++)
            {
              A[i][j] += t * work[i];
            }
          }
        }
        if (wantv)
        {

          // Place the transformation in V for subsequent
          // back multiplication.

          for (int i = k + 1; i < m_nCols; i++)
          {
            V[i][k] = e[i];
          }
        }
      }
    }

    // Set up the final bidiagonal matrix or order p.

    int p = Math.min (m_nCols, m_nRows + 1);
    if (nct < m_nCols)
    {
      s[nct] = A[nct][nct];
    }
    if (m_nRows < p)
    {
      s[p - 1] = 0.0;
    }
    if (nrt + 1 < p)
    {
      e[nrt] = A[nrt][p - 1];
    }
    e[p - 1] = 0.0;

    // If required, generate U.

    if (wantu)
    {
      for (int j = nct; j < nu; j++)
      {
        for (int i = 0; i < m_nRows; i++)
        {
          U[i][j] = 0.0;
        }
        U[j][j] = 1.0;
      }
      for (int k = nct - 1; k >= 0; k--)
      {
        if (s[k] != 0.0)
        {
          for (int j = k + 1; j < nu; j++)
          {
            double t = 0;
            for (int i = k; i < m_nRows; i++)
            {
              t += U[i][k] * U[i][j];
            }
            t = -t / U[k][k];
            for (int i = k; i < m_nRows; i++)
            {
              U[i][j] += t * U[i][k];
            }
          }
          for (int i = k; i < m_nRows; i++)
          {
            U[i][k] = -U[i][k];
          }
          U[k][k] = 1.0 + U[k][k];
          for (int i = 0; i < k - 1; i++)
          {
            U[i][k] = 0.0;
          }
        }
        else
        {
          for (int i = 0; i < m_nRows; i++)
          {
            U[i][k] = 0.0;
          }
          U[k][k] = 1.0;
        }
      }
    }

    // If required, generate V.

    if (wantv)
    {
      for (int k = m_nCols - 1; k >= 0; k--)
      {
        if ((k < nrt) && (e[k] != 0.0))
        {
          for (int j = k + 1; j < nu; j++)
          {
            double t = 0;
            for (int i = k + 1; i < m_nCols; i++)
            {
              t += V[i][k] * V[i][j];
            }
            t = -t / V[k + 1][k];
            for (int i = k + 1; i < m_nCols; i++)
            {
              V[i][j] += t * V[i][k];
            }
          }
        }
        for (int i = 0; i < m_nCols; i++)
        {
          V[i][k] = 0.0;
        }
        V[k][k] = 1.0;
      }
    }

    // Main iteration loop for the singular values.

    final int pp = p - 1;
    int iter = 0;
    final double eps = Math.pow (2.0, -52.0);
    final double tiny = Math.pow (2.0, -966.0);
    while (p > 0)
    {
      int k, kase;

      // Here is where a test for too many iterations would go.

      // This section of the program inspects for
      // negligible elements in the s and e arrays. On
      // completion the variables kase and k are set as follows.

      // kase = 1 if s(p) and e[k-1] are negligible and k<p
      // kase = 2 if s(k) is negligible and k<p
      // kase = 3 if e[k-1] is negligible, k<p, and
      // s(k), ..., s(p) are not negligible (qr step).
      // kase = 4 if e(p-1) is negligible (convergence).

      for (k = p - 2; k >= -1; k--)
      {
        if (k == -1)
        {
          break;
        }
        if (Math.abs (e[k]) <= tiny + eps * (Math.abs (s[k]) + Math.abs (s[k + 1])))
        {
          e[k] = 0.0;
          break;
        }
      }
      if (k == p - 2)
      {
        kase = 4;
      }
      else
      {
        int ks;
        for (ks = p - 1; ks >= k; ks--)
        {
          if (ks == k)
          {
            break;
          }
          final double t = (ks != p ? Math.abs (e[ks]) : 0.) + (ks != k + 1 ? Math.abs (e[ks - 1]) : 0.);
          if (Math.abs (s[ks]) <= tiny + eps * t)
          {
            s[ks] = 0.0;
            break;
          }
        }
        if (ks == k)
        {
          kase = 3;
        }
        else
          if (ks == p - 1)
          {
            kase = 1;
          }
          else
          {
            kase = 2;
            k = ks;
          }
      }
      k++;

      // Perform the task indicated by kase.

      switch (kase)
      {

      // Deflate negligible s(p).

        case 1:
        {
          double f = e[p - 2];
          e[p - 2] = 0.0;
          for (int j = p - 2; j >= k; j--)
          {
            double t = MathHelper.hypot (s[j], f);
            final double cs = s[j] / t;
            final double sn = f / t;
            s[j] = t;
            if (j != k)
            {
              f = -sn * e[j - 1];
              e[j - 1] = cs * e[j - 1];
            }
            if (wantv)
            {
              for (int i = 0; i < m_nCols; i++)
              {
                t = cs * V[i][j] + sn * V[i][p - 1];
                V[i][p - 1] = -sn * V[i][j] + cs * V[i][p - 1];
                V[i][j] = t;
              }
            }
          }
        }
          break;

        // Split at negligible s(k).

        case 2:
        {
          double f = e[k - 1];
          e[k - 1] = 0.0;
          for (int j = k; j < p; j++)
          {
            double t = MathHelper.hypot (s[j], f);
            final double cs = s[j] / t;
            final double sn = f / t;
            s[j] = t;
            f = -sn * e[j];
            e[j] = cs * e[j];
            if (wantu)
            {
              for (int i = 0; i < m_nRows; i++)
              {
                t = cs * U[i][j] + sn * U[i][k - 1];
                U[i][k - 1] = -sn * U[i][j] + cs * U[i][k - 1];
                U[i][j] = t;
              }
            }
          }
        }
          break;

        // Perform one qr step.

        case 3:
        {

          // Calculate the shift.

          final double scale = Math.max (Math.max (Math.max (Math.max (Math.abs (s[p - 1]), Math.abs (s[p - 2])),
                                                             Math.abs (e[p - 2])),
                                                   Math.abs (s[k])),
                                         Math.abs (e[k]));
          final double sp = s[p - 1] / scale;
          final double spm1 = s[p - 2] / scale;
          final double epm1 = e[p - 2] / scale;
          final double sk = s[k] / scale;
          final double ek = e[k] / scale;
          final double b = ((spm1 + sp) * (spm1 - sp) + epm1 * epm1) / 2.0;
          final double c = (sp * epm1) * (sp * epm1);
          double shift = 0.0;
          if ((b != 0.0) | (c != 0.0))
          {
            shift = Math.sqrt (b * b + c);
            if (b < 0.0)
            {
              shift = -shift;
            }
            shift = c / (b + shift);
          }
          double f = (sk + sp) * (sk - sp) + shift;
          double g = sk * ek;

          // Chase zeros.

          for (int j = k; j < p - 1; j++)
          {
            double t = MathHelper.hypot (f, g);
            double cs = f / t;
            double sn = g / t;
            if (j != k)
            {
              e[j - 1] = t;
            }
            f = cs * s[j] + sn * e[j];
            e[j] = cs * e[j] - sn * s[j];
            g = sn * s[j + 1];
            s[j + 1] = cs * s[j + 1];
            if (wantv)
            {
              for (int i = 0; i < m_nCols; i++)
              {
                t = cs * V[i][j] + sn * V[i][j + 1];
                V[i][j + 1] = -sn * V[i][j] + cs * V[i][j + 1];
                V[i][j] = t;
              }
            }
            t = MathHelper.hypot (f, g);
            cs = f / t;
            sn = g / t;
            s[j] = t;
            f = cs * e[j] + sn * s[j + 1];
            s[j + 1] = -sn * e[j] + cs * s[j + 1];
            g = sn * e[j + 1];
            e[j + 1] = cs * e[j + 1];
            if (wantu && (j < m_nRows - 1))
            {
              for (int i = 0; i < m_nRows; i++)
              {
                t = cs * U[i][j] + sn * U[i][j + 1];
                U[i][j + 1] = -sn * U[i][j] + cs * U[i][j + 1];
                U[i][j] = t;
              }
            }
          }
          e[p - 2] = f;
          iter = iter + 1;
        }
          break;

        // Convergence.

        case 4:
        {

          // Make the singular values positive.

          if (s[k] <= 0.0)
          {
            s[k] = (s[k] < 0.0 ? -s[k] : 0.0);
            if (wantv)
            {
              for (int i = 0; i <= pp; i++)
              {
                V[i][k] = -V[i][k];
              }
            }
          }

          // Order the singular values.

          while (k < pp)
          {
            if (s[k] >= s[k + 1])
            {
              break;
            }
            double t = s[k];
            s[k] = s[k + 1];
            s[k + 1] = t;
            if (wantv && (k < m_nCols - 1))
            {
              for (int i = 0; i < m_nCols; i++)
              {
                t = V[i][k + 1];
                V[i][k + 1] = V[i][k];
                V[i][k] = t;
              }
            }
            if (wantu && (k < m_nRows - 1))
            {
              for (int i = 0; i < m_nRows; i++)
              {
                t = U[i][k + 1];
                U[i][k + 1] = U[i][k];
                U[i][k] = t;
              }
            }
            k++;
          }
          iter = 0;
          p--;
        }
          break;
      }
    }
  }

  /*
   * ------------------------ Public Methods ------------------------
   */

  /**
   * Return the left singular vectors
   * 
   * @return U
   */

  public Matrix getU ()
  {
    return new Matrix (U, m_nRows, Math.min (m_nRows + 1, m_nCols));
  }

  /**
   * Return the right singular vectors
   * 
   * @return V
   */

  public Matrix getV ()
  {
    return new Matrix (V, m_nCols, m_nCols);
  }

  /**
   * Return the one-dimensional array of singular values
   * 
   * @return diagonal of S.
   */

  public double [] getSingularValues ()
  {
    return s;
  }

  /**
   * Return the diagonal matrix of singular values
   * 
   * @return S
   */

  public Matrix getS ()
  {
    final Matrix X = new Matrix (m_nCols, m_nCols);
    final double [][] S = X.getArray ();
    for (int i = 0; i < m_nCols; i++)
    {
      for (int j = 0; j < m_nCols; j++)
      {
        S[i][j] = 0.0;
      }
      S[i][i] = this.s[i];
    }
    return X;
  }

  /**
   * Two norm
   * 
   * @return max(S)
   */

  public double norm2 ()
  {
    return s[0];
  }

  /**
   * Two norm condition number
   * 
   * @return max(S)/min(S)
   */

  public double cond ()
  {
    return s[0] / s[Math.min (m_nRows, m_nCols) - 1];
  }

  /**
   * Effective numerical matrix rank
   * 
   * @return Number of nonnegligible singular values.
   */

  public int rank ()
  {
    final double eps = Math.pow (2.0, -52.0);
    final double tol = Math.max (m_nRows, m_nCols) * s[0] * eps;
    int r = 0;
    for (final double element : s)
    {
      if (element > tol)
      {
        r++;
      }
    }
    return r;
  }

  private static final long serialVersionUID = 1;
}
