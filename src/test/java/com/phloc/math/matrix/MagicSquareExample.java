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

import java.util.Date;

import javax.annotation.Nonnegative;

import org.junit.Test;

import com.phloc.math.matrix.EigenvalueDecomposition;
import com.phloc.math.matrix.LUDecomposition;
import com.phloc.math.matrix.Matrix;
import com.phloc.math.matrix.QRDecomposition;

/** Example of use of Matrix Class, featuring magic squares. **/

public class MagicSquareExample
{

  /** Generate magic square test matrix. **/

  public static Matrix magic (@Nonnegative final int n)
  {
    if (n <= 0)
      throw new IllegalStateException ();

    final double [][] M = new double [n] [n];

    // Odd order

    if ((n & 1) == 1)
    {
      final int a = (n + 1) / 2;
      final int b = (n + 1);
      for (int j = 0; j < n; j++)
      {
        for (int i = 0; i < n; i++)
        {
          M[i][j] = n * ((i + j + a) % n) + ((i + 2 * j + b) % n) + 1;
        }
      }

      // Doubly Even Order

    }
    else
      if ((n % 4) == 0)
      {
        for (int j = 0; j < n; j++)
        {
          for (int i = 0; i < n; i++)
          {
            if (((i + 1) / 2) % 2 == ((j + 1) / 2) % 2)
            {
              M[i][j] = n * n - n * i - j;
            }
            else
            {
              M[i][j] = n * i + j + 1;
            }
          }
        }

        // Singly Even Order

      }
      else
      {
        final int p = n / 2;
        final int k = (n - 2) / 4;
        final Matrix A = magic (p);
        for (int j = 0; j < p; j++)
        {
          for (int i = 0; i < p; i++)
          {
            final double aij = A.get (i, j);
            M[i][j] = aij;
            M[i][j + p] = aij + 2 * p * p;
            M[i + p][j] = aij + 3 * p * p;
            M[i + p][j + p] = aij + p * p;
          }
        }
        for (int i = 0; i < p; i++)
        {
          for (int j = 0; j < k; j++)
          {
            final double t = M[i][j];
            M[i][j] = M[i + p][j];
            M[i + p][j] = t;
          }
          for (int j = n - k + 1; j < n; j++)
          {
            final double t = M[i][j];
            M[i][j] = M[i + p][j];
            M[i + p][j] = t;
          }
        }
        double t = M[k][0];
        M[k][0] = M[k + p][0];
        M[k + p][0] = t;
        t = M[k][k];
        M[k][k] = M[k + p][k];
        M[k + p][k] = t;
      }
    return new Matrix (M);
  }

  /** Shorten spelling of print. **/

  private static void print (final String s)
  {
    System.out.print (s);
  }

  /** Format double with Fw.d. **/

  public static String fixedWidthDoubletoString (final double x, final int w, final int d)
  {
    final java.text.DecimalFormat fmt = new java.text.DecimalFormat ();
    fmt.setMaximumFractionDigits (d);
    fmt.setMinimumFractionDigits (d);
    fmt.setGroupingUsed (false);
    String s = fmt.format (x);
    while (s.length () < w)
    {
      s = " " + s;
    }
    return s;
  }

  /** Format integer with Iw. **/

  public static String fixedWidthIntegertoString (final int n, final int w)
  {
    String s = Integer.toString (n);
    while (s.length () < w)
    {
      s = " " + s;
    }
    return s;
  }

  @Test
  public void testMain ()
  {

    /*
     * | Tests LU, QR, SVD and symmetric Eig decompositions. | | n = order of
     * magic square. | trace = diagonal sum, should be the magic sum, (n^3 +
     * n)/2. | max_eig = maximum eigenvalue of (A + A')/2, should equal trace. |
     * rank = linear algebraic rank, | should equal n if n is odd, be less than
     * n if n is even. | cond = L_2 condition number, ratio of singular values.
     * | lu_res = test of LU factorization, norm1(L*U-A(p,:))/(n*eps). | qr_res
     * = test of QR factorization, norm1(Q*R-A)/(n*eps).
     */

    print ("\n    Test of Matrix Class, using magic squares.\n");
    print ("    See MagicSquareExample.main() for an explanation.\n");
    print ("\n      n     trace       max_eig   rank        cond      lu_res      qr_res\n\n");

    final Date start_time = new Date ();
    final double eps = Math.pow (2.0, -52.0);
    for (int n = 3; n <= 32; n++)
    {
      print (fixedWidthIntegertoString (n, 7));

      final Matrix M = magic (n);

      final int t = (int) M.trace ();
      print (fixedWidthIntegertoString (t, 10));

      final EigenvalueDecomposition E = new EigenvalueDecomposition (M.plus (M.transpose ()).times (0.5));
      final double [] d = E.getRealEigenvalues ();
      print (fixedWidthDoubletoString (d[n - 1], 14, 3));

      final int r = M.rank ();
      print (fixedWidthIntegertoString (r, 7));

      final double c = M.cond ();
      print (c < 1 / eps ? fixedWidthDoubletoString (c, 12, 3) : "         Inf");

      final LUDecomposition LU = new LUDecomposition (M);
      final Matrix L = LU.getL ();
      final Matrix U = LU.getU ();
      final int [] p = LU.getPivot ();
      Matrix R = L.times (U).minus (M.getMatrix (p, 0, n - 1));
      double res = R.norm1 () / (n * eps);
      print (fixedWidthDoubletoString (res, 12, 3));

      final QRDecomposition QR = new QRDecomposition (M);
      final Matrix Q = QR.getQ ();
      R = QR.getR ();
      R = Q.times (R).minus (M);
      res = R.norm1 () / (n * eps);
      print (fixedWidthDoubletoString (res, 12, 3));

      print ("\n");
    }
    final Date stop_time = new Date ();
    final double etime = (stop_time.getTime () - start_time.getTime ()) / 1000.;
    print ("\nElapsed Time = " + fixedWidthDoubletoString (etime, 12, 3) + " seconds\n");
    print ("Adios\n");
  }
}
