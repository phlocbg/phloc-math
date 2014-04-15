package numbercruncher.mathutils;

import numbercruncher.matrix.ColumnVector;
import numbercruncher.matrix.LinearSystem;
import numbercruncher.matrix.MatrixException;

/**
 * A least-squares regression polynomial function.
 */
public class RegressionPolynomial implements IEvaluatable
{
  /** number of data points */
  private int n;
  /** degree of the polynomial */
  private final int m_nDegree;
  /** maximum no. of data points */
  private final int m_nMaxPoints;
  /** true if coefficients valid */
  private boolean coefsValid;
  /** warning message */
  private String warningMsg;

  /** data points */
  private DataPoint m_aData[];

  /** coefficient matrix A */
  private LinearSystem A;
  /** regression coefficients vector a */
  private ColumnVector a;
  /** right-hand-side vector b */
  private ColumnVector b;

  /**
   * Constructor.
   * 
   * @param degree
   *        the degree of the polynomial
   * @param maxPoints
   *        the maximum number of data points
   */
  public RegressionPolynomial (final int degree, final int maxPoints)
  {
    this.m_nDegree = degree;
    this.m_nMaxPoints = maxPoints;
    this.m_aData = new DataPoint [maxPoints];
  }

  /**
   * Constructor.
   * 
   * @param degree
   *        the degree of the polynomial
   * @param data
   *        the array of data points
   */
  public RegressionPolynomial (final int degree, final DataPoint [] data)
  {
    this.m_nDegree = degree;
    this.m_nMaxPoints = data.length;
    this.m_aData = data;
    this.n = data.length;
  }

  /**
   * Return the degree of the polynomial.
   * 
   * @return the count
   */
  public int getDegree ()
  {
    return m_nDegree;
  }

  /**
   * Return the current number of data points.
   * 
   * @return the count
   */
  public int getDataPointCount ()
  {
    return n;
  }

  /**
   * Return the data points.
   * 
   * @return the count
   */
  public DataPoint [] getDataPoints ()
  {
    return m_aData;
  }

  /**
   * Return the coefficients matrix.
   * 
   * @return the A matrix
   * @throws matrix.MatrixException
   *         if a matrix error occurred
   * @throws Exception
   *         if an overflow occurred
   */
  public LinearSystem getCoefficientsMatrix () throws Exception, MatrixException
  {
    validateCoefficients ();
    return A;
  }

  /**
   * Return the regression coefficients.
   * 
   * @return the a vector
   * @throws matrix.MatrixException
   *         if a matrix error occurred
   * @throws Exception
   *         if an overflow occurred
   */
  public ColumnVector getRegressionCoefficients () throws Exception, MatrixException
  {
    validateCoefficients ();
    return a;
  }

  /**
   * Return the right hand side.
   * 
   * @return the b vector
   * @throws matrix.MatrixException
   *         if a matrix error occurred
   * @throws Exception
   *         if an overflow occurred
   */
  public ColumnVector getRHS () throws Exception, MatrixException
  {
    validateCoefficients ();
    return b;
  }

  /**
   * Return the warning message (if any).
   * 
   * @return the message or null
   */
  public String getWarningMessage ()
  {
    return warningMsg;
  }

  /**
   * Add a new data point: Update the sums.
   * 
   * @param dataPoint
   *        the new data point
   */
  public void addDataPoint (final DataPoint dataPoint)
  {
    if (n == m_nMaxPoints)
      return;

    m_aData[n++] = dataPoint;
    coefsValid = false;
  }

  /**
   * Return the value of the regression polynomial function at x.
   * (Implementation of Evaluatable.)
   * 
   * @param x
   *        the value of x
   * @return the value of the function at x
   */
  public float at (final float x)
  {
    if (n < m_nDegree + 1)
      return Float.NaN;

    try
    {
      validateCoefficients ();

      float xPower = 1;
      float y = 0;

      // Compute y = a[0] + a[1]*x + a[2]*x^2 + ... + a[n]*x^n
      for (int i = 0; i <= m_nDegree; ++i)
      {
        y += a.at (i) * xPower;
        xPower *= x;
      }

      return y;
    }
    catch (final MatrixException ex)
    {
      return Float.NaN;
    }
    catch (final Exception ex)
    {
      return Float.NaN;
    }
  }

  /**
   * Reset.
   */
  public void reset ()
  {
    n = 0;
    m_aData = new DataPoint [m_nMaxPoints];
    coefsValid = false;
  }

  /**
   * Compute the coefficients.
   * 
   * @throws matrix.MatrixException
   *         if a matrix error occurred
   * @throws Exception
   *         if an overflow occurred
   */
  public void computeCoefficients () throws Exception, MatrixException
  {
    validateCoefficients ();
  }

  /**
   * Validate the coefficients.
   * 
   * @throws matrix.MatrixException
   *         if a matrix error occurred
   * @throws Exception
   *         if an overflow occurred
   */
  private void validateCoefficients () throws Exception, MatrixException
  {
    if (coefsValid)
      return;

    A = new LinearSystem (m_nDegree + 1);
    b = new ColumnVector (m_nDegree + 1);

    // Compute the multipliers of a[0] for each equation.
    for (int r = 0; r <= m_nDegree; ++r)
    {
      final float sum = sumXPower (r);
      int j = 0;

      if (Float.isInfinite (sum))
      {
        throw new Exception ("Overflow occurred.");
      }

      // Set the multipliers along the diagonal.
      for (int i = r; i >= 0; --i)
        A.set (i, j++, sum);

      // Set the right-hand-side value.
      b.set (r, sumXPowerY (r));
    }

    // Compute the multipliers of a[c] for the last equation.
    for (int c = 1; c <= m_nDegree; ++c)
    {
      final float sum = sumXPower (m_nDegree + c);
      int i = m_nDegree;

      if (Float.isInfinite (sum))
      {
        throw new Exception ("Overflow occurred.");
      }

      // Set the multipliers along the diagonal.
      for (int j = c; j <= m_nDegree; ++j)
        A.set (i--, j, sum);
    }

    warningMsg = null;

    // First try solving with iterative improvement. If that
    // fails, then try solving without iterative improvement.
    try
    {
      a = A.solve (b, true);
    }
    catch (final MatrixException ex)
    {
      warningMsg = ex.getMessage ();
      a = A.solve (b, false);
    }

    coefsValid = true;
  }

  /**
   * Compute the sum of the x coordinates each raised to an integer power.
   * 
   * @return the sum
   */
  private float sumXPower (final int power)
  {
    float sum = 0;

    for (int i = 0; i < n; ++i)
    {
      sum += (float) IntPower.raise (m_aData[i].getX (), power);
    }

    return sum;
  }

  /**
   * Compute the sum of the x coordinates each raised to an integer power and
   * multiplied by the corresponding y coordinate.
   * 
   * @return the sum
   */
  private float sumXPowerY (final int power)
  {
    float sum = 0;

    for (int i = 0; i < n; ++i)
    {
      sum += m_aData[i].getY () * IntPower.raise (m_aData[i].getX (), power);
    }

    return sum;
  }
}
