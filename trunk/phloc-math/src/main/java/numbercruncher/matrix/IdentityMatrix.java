package numbercruncher.matrix;

public class IdentityMatrix extends SquareMatrix
{
  /**
   * Constructor.
   * 
   * @param n
   *        the number of rows == the number of columns
   */
  public IdentityMatrix (final int n)
  {
    super (n);
    for (int i = 0; i < n; ++i)
      m_aValues[i][i] = 1;
  }

  /**
   * Convert a square matrix into an identity matrix.
   * 
   * @param sm
   *        the square matrix to convert
   */
  public static void convert (final SquareMatrix sm)
  {
    for (int r = 0; r < sm.m_nRows; ++r)
    {
      for (int c = 0; c < sm.m_nCols; ++c)
      {
        sm.m_aValues[r][c] = (r == c) ? 1 : 0;
      }
    }
  }
}
