package numbercruncher.mathutils;

/**
 * A data point for interpolation and regression.
 */
public class DataPoint
{
  /** the x value */
  private final float m_fX;
  /** the y value */
  private final float m_fY;

  /**
   * Constructor.
   * 
   * @param x
   *        the x value
   * @param y
   *        the y value
   */
  public DataPoint (final float x, final float y)
  {
    m_fX = x;
    m_fY = y;
  }

  public float getX ()
  {
    return m_fX;
  }

  public float getY ()
  {
    return m_fY;
  }
}
