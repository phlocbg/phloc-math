package numbercruncher.matrix;


/**
 * A matrix that can be inverted. Also, compute its determinant, norm, and
 * condition number.
 */
public class InvertibleMatrix extends LinearSystem
{
  /**
   * Constructor.
   * 
   * @param n
   *        the number of rows = the number of columns
   */
  public InvertibleMatrix (final int n)
  {
    super (n);
  }

  /**
   * Constructor.
   * 
   * @param values
   *        the array of values
   */
  public InvertibleMatrix (final float values[][])
  {
    super (values);
  }

  /**
   * Compute the inverse of this matrix.
   * 
   * @return the inverse matrix
   * @throws matrix.MatrixException
   *         if an error occurred
   */
  public InvertibleMatrix inverse () throws MatrixException
  {
    final InvertibleMatrix inverse = new InvertibleMatrix (m_nRows);
    final IdentityMatrix identity = new IdentityMatrix (m_nRows);

    // Compute each column of the inverse matrix
    // using columns of the identity matrix.
    for (int c = 0; c < m_nCols; ++c)
    {
      final ColumnVector col = solve (identity.getColumn (c), true);
      inverse.setColumn (col, c);
    }

    return inverse;
  }

  /**
   * Compute the determinant.
   * 
   * @return the determinant
   * @throws matrix.MatrixException
   *         if an error occurred
   */
  public float determinant () throws MatrixException
  {
    decompose ();

    // Each row exchange during forward elimination flips the sign
    // of the determinant, so check for an odd number of exchanges.
    float determinant = ((exchangeCount & 1) == 0) ? 1 : -1;

    // Form the product of the diagonal elements of matrix U.
    for (int i = 0; i < m_nRows; ++i)
    {
      final int pi = permutation[i]; // permuted index
      determinant *= LU.at (pi, i);
    }

    return determinant;
  }

  /**
   * Compute the Euclidean norm of this matrix.
   * 
   * @return the norm
   */
  public float norm ()
  {
    float sum = 0;

    for (int r = 0; r < m_nRows; ++r)
    {
      for (int c = 0; c < m_nCols; ++c)
      {
        final float v = m_aValues[r][c];
        sum += v * v;
      }
    }

    return (float) Math.sqrt (sum);
  }

  /**
   * Compute the condition number based on the Euclidean norm.
   * 
   * @return the condition number
   */
  public float condition () throws MatrixException
  {
    return norm () * inverse ().norm ();
  }
}
