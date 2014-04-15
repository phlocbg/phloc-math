package numbercruncher.graphutils;

import java.awt.Dimension;

/**
 * The properties of a function plot.
 */
public class PlotProperties
{
  /** width */
  private int w;
  /** height */
  private int h;
  /** x-axis row */
  private int xAxisRow;
  /** y-axis column */
  private int yAxisCol;
  /** minimum x value */
  private float m_fXMin;
  /** maximum x value */
  private float m_fXMax;
  /** minimum y value */
  private float m_fYMin;
  /** maximum y value */
  private float m_fYMax;
  /** x delta per pixel */
  private float xDelta;
  /** y delta per pixel */
  private float yDelta;

  /**
   * Constructor.
   * 
   * @param xMin
   *        the minimum x value
   * @param xMax
   *        the maximum x value
   * @param yMin
   *        the minimum y value
   * @param yMax
   *        the maximum y value
   */
  public PlotProperties (final float xMin, final float xMax, final float yMin, final float yMax)
  {
    this.m_fXMin = xMin;
    this.m_fXMax = xMax;
    this.m_fYMin = yMin;
    this.m_fYMax = yMax;
  }

  /**
   * Return the minimum x value.
   * 
   * @return the minimum x value
   */
  public float getXMin ()
  {
    return m_fXMin;
  }

  /**
   * Return the maximum x value.
   * 
   * @return the maximum x value
   */
  public float getXMax ()
  {
    return m_fXMax;
  }

  /**
   * Return the minimum y value.
   * 
   * @return the minimum y value
   */
  public float getYMin ()
  {
    return m_fYMin;
  }

  /**
   * Return the maximum y value.
   * 
   * @return the maximum y value
   */
  public float getYMax ()
  {
    return m_fYMax;
  }

  /**
   * Return the x delta value.
   * 
   * @return the x delta value
   */
  public float getXDelta ()
  {
    return xDelta;
  }

  /**
   * Return the y delta value.
   * 
   * @return the y delta value
   */
  public float getYDelta ()
  {
    return yDelta;
  }

  /**
   * Return the width.
   * 
   * @return the width
   */
  public int getWidth ()
  {
    return w;
  }

  /**
   * Return the height.
   * 
   * @return the height
   */
  public int getHeight ()
  {
    return h;
  }

  /**
   * Return the x-axis row.
   * 
   * @return the row
   */
  public int getXAxisRow ()
  {
    return xAxisRow;
  }

  /**
   * Return the y-axis column.
   * 
   * @return the column
   */
  public int getYAxisColumn ()
  {
    return yAxisCol;
  }

  /**
   * Update the bounds values.
   * 
   * @param xMin
   *        the minimum x value
   * @param xMax
   *        the maximum x value
   * @param yMin
   *        the minimum y value
   * @param yMax
   *        the maximum y value
   */
  public void update (final float xMin, final float xMax, final float yMin, final float yMax)
  {
    this.m_fXMin = xMin;
    this.m_fXMax = xMax;
    this.m_fYMin = yMin;
    this.m_fYMax = yMax;
  }

  /**
   * Compute property values.
   * 
   * @param plotSize
   *        the plot panel size
   */
  void compute (final Dimension plotSize)
  {
    w = plotSize.width;
    h = plotSize.height;

    xDelta = (m_fXMax - m_fXMin) / w;
    yDelta = (m_fYMax - m_fYMin) / h;

    xAxisRow = Math.round (m_fYMax / yDelta);
    yAxisCol = Math.round (-m_fXMin / xDelta);
  }
}
