package numbercruncher.program10_2;

import numbercruncher.mathutils.DataPoint;
import numbercruncher.mathutils.RegressionPolynomial;
import numbercruncher.matrix.ColumnVector;

/**
 * PROGRAM 10-2: Polynomial Regression Demonstrate polynomial regression by
 * fitting a polynomial to a set of data points.
 */
public class Regression
{
  private static final int MAX_POINTS = 20;
  private static final float TWO_PI = (float) (2 * Math.PI);
  private static final float H = TWO_PI / MAX_POINTS;

  /**
   * Main program.
   * 
   * @param args
   *        the array of runtime arguments
   */
  public static void main (final String args[])
  {
    final int degree = 3;
    final float testX = (float) Math.PI;

    try
    {
      final RegressionPolynomial poly = new RegressionPolynomial (degree, MAX_POINTS);

      // Compute MAX_POINTS data points along the sine curve
      // between 0 and 2*pi.
      for (int i = 0; i < MAX_POINTS; ++i)
      {
        final float x = i * H;
        final float y = (float) Math.sin (x);
        poly.addDataPoint (new DataPoint (x, y));
      }

      // Compute and print the regression polynomial.
      System.out.print ("y = ");
      final ColumnVector a = poly.getRegressionCoefficients ();
      System.out.print (a.at (0) + " + " + a.at (1) + "x");
      for (int i = 2; i <= degree; ++i)
      {
        System.out.print (" + " + a.at (i) + "x^" + i);
      }
      System.out.println ();

      // Compute an estimate.
      System.out.println ("y(" + testX + ") = " + poly.at (testX));

      // Print the warning if there is one.
      final String warning = poly.getWarningMessage ();
      if (warning != null)
      {
        System.out.println ("WARNING: " + warning);
      }
    }
    catch (final Exception ex)
    {
      System.out.println ("\nERROR: " + ex.getMessage ());
    }
  }
}
/*
 * Output: y = -0.14296114 + 1.8568094x + -0.87079257x^2 + 0.09318722x^3
 * y(3.1415927) = -0.014611721
 */
