package numbercruncher.mathutils;

/**
 * Function integrator that implements the trapezoidal algorithm.
 */
public class TrapezoidalIntegrator implements IIntegrator
{
  /** the function to integrate */
  private final IEvaluatable m_aIntegrand;

  /**
   * Constructor.
   * 
   * @param integrand
   *        the function to integrate
   */
  public TrapezoidalIntegrator (final IEvaluatable integrand)
  {
    this.m_aIntegrand = integrand;
  }

  /**
   * Integrate the function from a to b using the trapezoidal algorithm, and
   * return an approximation to the area. (Integrator implementation.)
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

    final float h = (b - a) / intervals; // interval width
    float totalArea = 0;

    // Compute the area using the current number of intervals.
    for (int i = 0; i < intervals; ++i)
    {
      final float x1 = a + i * h;
      totalArea += areaOf (x1, h);
    }

    return totalArea;
  }

  /**
   * Compute the area of the ith trapezoidal region.
   * 
   * @param x1
   *        the left bound of the region
   * @param h
   *        the interval width
   * @return the area of the region
   */
  private float areaOf (final float x1, final float h)
  {
    final float x2 = x1 + h; // right bound of the region
    final float y1 = m_aIntegrand.at (x1); // value at left bound
    final float y2 = m_aIntegrand.at (x2); // value at right bound
    final float area = h * (y1 + y2) / 2; // area of the region

    return area;
  }
}
