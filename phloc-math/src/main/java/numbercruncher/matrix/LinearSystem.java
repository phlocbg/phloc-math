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

import numbercruncher.mathutils.AlignRight;
import numbercruncher.mathutils.Epsilon;

/**
 * Solve a system of linear equations using LU decomposition.
 */
public class LinearSystem extends SquareMatrix
{
  private static final float TOLERANCE = Epsilon.floatValue ();

  /** max iters for improvement = twice # of significant digits */
  private static final int MAX_ITER;
  static
  {
    int i = 0;
    float t = TOLERANCE;
    while (t < 1)
    {
      ++i;
      t *= 10;
    }
    MAX_ITER = 2 * i;
  }

  /** decomposed matrix A = LU */
  protected SquareMatrix LU;
  /** row index permutation vector */
  protected int permutation[];
  /** row exchange count */
  protected int exchangeCount;

  /**
   * Constructor.
   * 
   * @param n
   *        the number of rows = the number of columns
   */
  public LinearSystem (final int n)
  {
    super (n);
    reset ();
  }

  /**
   * Constructor.
   * 
   * @param values
   *        the array of values
   */
  public LinearSystem (final float values[][])
  {
    super (values);
  }

  /**
   * Set the values of the matrix.
   * 
   * @param values
   *        the 2-d array of values
   */
  @Override
  protected void set (final float values[][])
  {
    super.set (values);
    reset ();
  }

  /**
   * Set the value of element [r,c] in the matrix.
   * 
   * @param r
   *        the row index, 0..nRows
   * @param c
   *        the column index, 0..nRows
   * @param value
   *        the value
   * @throws MatrixException
   *         for invalid index
   */
  @Override
  public void set (final int r, final int c, final float value) throws MatrixException
  {
    super.set (r, c, value);
    reset ();
  }

  /**
   * Set a row of this matrix from a row vector.
   * 
   * @param rv
   *        the row vector
   * @param r
   *        the row index
   * @throws MatrixException
   *         for an invalid index or an invalid vector size
   */
  @Override
  public void setRow (final RowVector rv, final int r) throws MatrixException
  {
    super.setRow (rv, r);
    reset ();
  }

  /**
   * Set a column of this matrix from a column vector.
   * 
   * @param cv
   *        the column vector
   * @param c
   *        the column index
   * @throws MatrixException
   *         for an invalid index or an invalid vector size
   */
  @Override
  public void setColumn (final ColumnVector cv, final int c) throws MatrixException
  {
    super.setColumn (cv, c);
    reset ();
  }

  /**
   * Reset. Invalidate LU and the permutation vector.
   */
  protected void reset ()
  {
    LU = null;
    permutation = null;
    exchangeCount = 0;
  }

  /**
   * Solve Ax = b for x using the Gaussian elimination algorithm.
   * 
   * @param b
   *        the right-hand-side column vector
   * @param improve
   *        true to improve the solution
   * @return the solution column vector
   * @throws MatrixException
   *         if an error occurred
   */
  public ColumnVector solve (final ColumnVector b, final boolean improve) throws MatrixException
  {
    // Validate b's size.
    if (b.m_nRows != m_nRows)
    {
      throw new MatrixException (MatrixException.INVALID_DIMENSIONS);
    }

    decompose ();

    // Solve Ly = b for y by forward substitution.
    // Solve Ux = y for x by back substitution.
    final ColumnVector y = forwardSubstitution (b);
    final ColumnVector x = backSubstitution (y);

    // Improve and return x.
    if (improve)
      improve (b, x);
    return x;
  }

  /**
   * Print the decomposed matrix LU.
   * 
   * @param width
   *        the column width
   * @throws MatrixException
   *         if an error occurred
   */
  public void printDecomposed (final int width) throws MatrixException
  {
    decompose ();

    final AlignRight ar = new AlignRight ();

    for (int r = 0; r < m_nRows; ++r)
    {
      final int pr = permutation[r]; // permuted row index
      ar.print ("Row ", 0);
      ar.print (r + 1, 2);
      ar.print (":", 0);

      for (int c = 0; c < m_nCols; ++c)
      {
        ar.print (LU.m_aValues[pr][c], width);
      }
      ar.println ();
    }
  }

  /**
   * Compute the upper triangular matrix U and lower triangular matrix L such
   * that A = L*U. Store L and U together in matrix LU. Compute the permutation
   * vector permutation of the row indices.
   * 
   * @throws MatrixException
   *         for a zero row or a singular matrix
   */
  protected void decompose () throws MatrixException
  {
    // Return if the decomposition is valid.
    if (LU != null)
      return;

    // Create a new LU matrix and permutation vector.
    // LU is initially just a copy of the values of this system.
    LU = new SquareMatrix (this.copyValues2D ());
    permutation = new int [m_nRows];

    final float scales[] = new float [m_nRows];

    // Loop to initialize the permutation vector and scales.
    for (int r = 0; r < m_nRows; ++r)
    {
      permutation[r] = r; // initially no row exchanges

      // Find the largest row element.
      float largestRowElmt = 0;
      for (int c = 0; c < m_nRows; ++c)
      {
        final float elmt = Math.abs (LU.at (r, c));
        if (largestRowElmt < elmt)
          largestRowElmt = elmt;
      }

      // Set the scaling factor for row equilibration.
      if (largestRowElmt != 0)
      {
        scales[r] = 1 / largestRowElmt;
      }
      else
      {
        throw new MatrixException (MatrixException.ZERO_ROW);
      }
    }

    // Do forward elimination with scaled partial row pivoting.
    forwardElimination (scales);

    // Check bottom right element of the permuted matrix.
    if (LU.at (permutation[m_nRows - 1], m_nRows - 1) == 0)
    {
      throw new MatrixException (MatrixException.SINGULAR);
    }
  }

  /**
   * Do forward elimination with scaled partial row pivoting.
   * 
   * @parm scales the scaling vector
   * @throws MatrixException
   *         for a singular matrix
   */
  private void forwardElimination (final float scales[]) throws MatrixException
  {
    // Loop once per pivot row 0..nRows-1.
    for (int rPivot = 0; rPivot < m_nRows - 1; ++rPivot)
    {
      float largestScaledElmt = 0;
      int rLargest = 0;

      // Starting from the pivot row rPivot, look down
      // column rPivot to find the largest scaled element.
      for (int r = rPivot; r < m_nRows; ++r)
      {

        // Use the permuted row index.
        final int pr = permutation[r];
        final float absElmt = Math.abs (LU.at (pr, rPivot));
        final float scaledElmt = absElmt * scales[pr];

        if (largestScaledElmt < scaledElmt)
        {

          // The largest scaled element and
          // its row index.
          largestScaledElmt = scaledElmt;
          rLargest = r;
        }
      }

      // Is the matrix singular?
      if (largestScaledElmt == 0)
      {
        throw new MatrixException (MatrixException.SINGULAR);
      }

      // Exchange rows if necessary to choose the best
      // pivot element by making its row the pivot row.
      if (rLargest != rPivot)
      {
        final int temp = permutation[rPivot];
        permutation[rPivot] = permutation[rLargest];
        permutation[rLargest] = temp;

        ++exchangeCount;
      }

      // Use the permuted pivot row index.
      final int prPivot = permutation[rPivot];
      final float pivotElmt = LU.at (prPivot, rPivot);

      // Do the elimination below the pivot row.
      for (int r = rPivot + 1; r < m_nRows; ++r)
      {

        // Use the permuted row index.
        final int pr = permutation[r];
        final float multiple = LU.at (pr, rPivot) / pivotElmt;

        // Set the multiple into matrix L.
        LU.set (pr, rPivot, multiple);

        // Eliminate an unknown from matrix U.
        if (multiple != 0)
        {
          for (int c = rPivot + 1; c < m_nCols; ++c)
          {
            float elmt = LU.at (pr, c);

            // Subtract the multiple of the pivot row.
            elmt -= multiple * LU.at (prPivot, c);
            LU.set (pr, c, elmt);
          }
        }
      }
    }
  }

  /**
   * Solve Ly = b for y by forward substitution.
   * 
   * @param b
   *        the column vector b
   * @return the column vector y
   * @throws MatrixException
   *         if an error occurred
   */
  private ColumnVector forwardSubstitution (final ColumnVector b) throws MatrixException
  {
    final ColumnVector y = new ColumnVector (m_nRows);

    // Do forward substitution.
    for (int r = 0; r < m_nRows; ++r)
    {
      final int pr = permutation[r]; // permuted row index
      float dot = 0;
      for (int c = 0; c < r; ++c)
      {
        dot += LU.at (pr, c) * y.at (c);
      }
      y.set (r, b.at (pr) - dot);
    }

    return y;
  }

  /**
   * Solve Ux = y for x by back substitution.
   * 
   * @param y
   *        the column vector y
   * @return the solution column vector x
   * @throws MatrixException
   *         if an error occurred
   */
  private ColumnVector backSubstitution (final ColumnVector y) throws MatrixException
  {
    final ColumnVector x = new ColumnVector (m_nRows);

    // Do back substitution.
    for (int r = m_nRows - 1; r >= 0; --r)
    {
      final int pr = permutation[r]; // permuted row index
      float dot = 0;
      for (int c = r + 1; c < m_nRows; ++c)
      {
        dot += LU.at (pr, c) * x.at (c);
      }
      x.set (r, (y.at (r) - dot) / LU.at (pr, r));
    }

    return x;
  }

  /**
   * Iteratively improve the solution x to machine accuracy.
   * 
   * @param b
   *        the right-hand side column vector
   * @param x
   *        the improved solution column vector
   * @throws MatrixException
   *         if failed to converge
   */
  private void improve (final ColumnVector b, final ColumnVector x) throws MatrixException
  {
    // Find the largest x element.
    float largestX = 0;
    for (int r = 0; r < m_nRows; ++r)
    {
      final float absX = Math.abs (x.m_aValues[r][0]);
      if (largestX < absX)
        largestX = absX;
    }

    // Is x already as good as possible?
    if (largestX == 0)
      return;

    final ColumnVector residuals = new ColumnVector (m_nRows);

    // Iterate to improve x.
    for (int iter = 0; iter < MAX_ITER; ++iter)
    {

      // Compute residuals = b - Ax.
      // Must use double precision!
      for (int r = 0; r < m_nRows; ++r)
      {
        double dot = 0;
        @SuppressWarnings ("unused")
        final float [] row = m_aValues[r];
        for (int c = 0; c < m_nRows; ++c)
        {
          final double elmt = at (r, c);
          dot += elmt * x.at (c); // dbl.prec. *
        }
        final double value = b.at (r) - dot; // dbl.prec. -
        residuals.set (r, (float) value);
      }

      // Solve Az = residuals for z.
      final ColumnVector z = solve (residuals, false);

      // Set x = x + z.
      // Find largest the largest difference.
      float largestDiff = 0;
      for (int r = 0; r < m_nRows; ++r)
      {
        final float oldX = x.at (r);
        x.set (r, oldX + z.at (r));

        final float diff = Math.abs (x.at (r) - oldX);
        if (largestDiff < diff)
          largestDiff = diff;
      }

      // Is any further improvement possible?
      if (largestDiff < largestX * TOLERANCE)
        return;
    }

    // Failed to converge because A is nearly singular.
    throw new MatrixException (MatrixException.NO_CONVERGENCE);
  }
}
