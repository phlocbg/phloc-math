package numbercruncher.mathutils;

/**
 * Function integrator that implements Simpson's algorithm with parabolas.
 */
public class SimpsonsIntegrator implements IIntegrator
{
  /** the function to integrate */
  private final IEvaluatable m_aIntegrand;

  /**
   * Constructor.
   * 
   * @param integrand
   *        the function to integrate
   */
  public SimpsonsIntegrator (final IEvaluatable integrand)
  {
    this.m_aIntegrand = integrand;
  }

  /**
   * Integrate the function from a to b using Simpson's algorithm, and return an
   * approximation to the area. (Integrator implementation.)
   * 
   * @param a
   *        the lower limit
   * @param b
   *        the upper limit
   * @param intervals
   *        the number of equal-width intervals
   * @return an approximation to the area
   */
  public float integrate (final float a, final float b, final int intervals)
  {
    if (b <= a)
      return 0;

    final float h = (b - a) / intervals / 2; // interval width
    // (split in two)
    float totalArea = 0;

    // Compute the area using the current number of intervals.
    for (int i = 0; i < intervals; ++i)
    {
      final float x1 = a + 2 * i * h;
      totalArea += areaOf (x1, h);
    }

    return totalArea;
  }

  /**
   * Compute the area of the ith parabolic region.
   * 
   * @param x1
   *        the left bound of the region
   * @param h
   *        the interval width
   * @return the area of the region
   */
  private float areaOf (final float x1, final float h)
  {
    final float x2 = x1 + h; // middle
    final float x3 = x2 + h; // right bound of the region
    final float y1 = m_aIntegrand.at (x1); // value at left bound
    final float y2 = m_aIntegrand.at (x2); // value at the middle
    final float y3 = m_aIntegrand.at (x3); // value at right bound
    final float area = h * (y1 + 4 * y2 + y3) / 3; // area of the region

    return area;
  }
}
