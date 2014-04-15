package numbercruncher.rootutils;

import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.Map;

import numbercruncher.graphutils.IPlottable;
import numbercruncher.graphutils.PlotProperties;
import numbercruncher.mathutils.Function;
import numbercruncher.mathutils.IEvaluatable;

/**
 * Wrapper class that makes functions plottable.
 */
public class PlotFunction implements IPlottable
{
  private static final int X1 = 1;
  private static final int X2 = 216;

  private static final int Y11A = 25;
  private static final int Y12A = 52;
  private static final int Y21A = Y12A + 2;
  private static final int Y22A = 82;
  private static final int Y31A = Y22A + 2;
  private static final int Y32A = 111;
  private static final int Y41A = Y32A + 2;
  private static final int Y42A = 141;
  private static final int Y51A = Y42A + 2;
  private static final int Y52A = 170;
  private static final int Y61A = Y52A + 2;
  private static final int Y62A = 200;
  private static final int Y71A = Y62A + 2;
  private static final int Y72A = 229;

  private static final int XL1 = 277;
  private static final int XL2 = 424;
  private static final int XR1 = XL2 + 2;
  private static final int XR2 = 539;

  private static final int Y11B = 24;
  private static final int Y12B = 92;
  private static final int Y21B = Y12B + 2;
  private static final int Y22B = 128;
  private static final int Y31B = Y22B + 2;
  private static final int Y32B = 174;
  private static final int Y41B = Y32B + 2;
  private static final int Y42B = 212;
  private static final int Y51B = Y42B + 2;
  private static final int Y52B = 264;
  private static final int Y61B = Y52B + 2;
  private static final int Y62B = 336;
  private static final int Y71B = Y62B + 2;
  private static final int Y72B = 388;
  private static final int Y81B = Y72B + 2;
  private static final int Y82B = 445;
  private static final int Y91B = Y82B + 2;
  private static final int Y92B = 506;

  /** function */
  private Function m_aFunction;
  /** image region */
  private Rectangle rectangle;
  /** plot properties */
  private PlotProperties properties;

  /** wrapped function table */
  private static Map <String, PlotFunction> TABLE = new Hashtable <String, PlotFunction> (32);

  // Enter the wrapped functions into the table.
  static
  {
    enter ("x^2 - 4", X1, Y11A, X2, Y12A);
    enter ("-x^2 + 4x + 5", X1, Y21A, X2, Y22A);
    enter ("x^3 + 3x^2 - 9x - 10", X1, Y31A, X2, Y32A);
    enter ("x^2 - 2x + 3", X1, Y41A, X2, Y42A);
    enter ("2x^3 - 10x^2 + 11x - 5", X1, Y51A, X2, Y52A);
    enter ("e^-x - x", X1, Y61A, X2, Y62A);
    enter ("x - e^(1/x)", X1, Y71A, X2, Y72A);
    enter ("(x + 4/x)/2", XL1, Y11B, XL2, Y12B);
    enter ("4/x", XR1, Y11B, XR2, Y12B);
    enter ("sqrt(x + 2)", XL1, Y21B, XL2, Y22B);
    enter ("2/x + 1", XL1, Y31B, XL2, Y32B);
    enter ("x*x - 2", XR1, Y21B, XR2, Y22B);
    enter ("exp(-x)", XL1, Y41B, XL2, Y42B);
    enter ("-log(x)", XR1, Y41B, XR2, Y42B);
    enter ("exp(1/x)", XL1, Y51B, XL2, Y52B);
    enter ("(x + exp(1/x))/2", XL1, Y61B, XL2, Y62B);
    enter ("1/log(x)", XR1, Y51B, XR2, Y52B);
    enter ("sin(x)/2 + 1", XL1, Y71B, XL2, Y72B);
    enter ("1 + 1/x + 1/(x*x)", XL1, Y81B, XL2, Y82B);
    enter ("20/(x*x + 2*x + 10)", XL1, Y91B, XL2, Y92B);
  }

  /**
   * Create a wrapped function and enter it into the table.
   * 
   * @param key
   *        the hash key
   * @param x1
   *        the x-coordinate of upper left corner of image region
   * @param y1
   *        the y-coordinate of upper left corner of image region
   * @param x1
   *        the x-coordinate of lower left corner of image region
   * @param y1
   *        the y-coordinate of lower left corner of image region
   */
  private static void enter (final String key, final int x1, final int y1, final int x2, final int y2)
  {
    final PlotFunction plotFunction = new PlotFunction (x1, y1, x2, y2);
    plotFunction.m_aFunction = RootFunctions.function (key);

    TABLE.put (key, plotFunction);
  }

  /**
   * Constructor.
   * 
   * @param key
   *        the function key
   * @param xMin
   *        the minimum x value of the plot bounds
   * @param xMax
   *        the maximum x value of the plot bounds
   * @param yMin
   *        the minimum y value of the plot bounds
   * @param yMax
   *        the maximum y value of the plot bounds
   */
  public PlotFunction (final String key, final float xMin, final float xMax, final float yMin, final float yMax)
  {
    final PlotFunction plotFunction = TABLE.get (key);

    if (plotFunction != null)
    {
      this.m_aFunction = plotFunction.m_aFunction;
      this.rectangle = plotFunction.rectangle;
      this.properties = new PlotProperties (xMin, xMax, yMin, yMax);
    }
  }

  /**
   * Constructor.
   * 
   * @param function
   *        the function
   * @param xMin
   *        the minimum x value of the plot bounds
   * @param xMax
   *        the maximum x value of the plot bounds
   * @param yMin
   *        the minimum y value of the plot bounds
   * @param yMax
   *        the maximum y value of the plot bounds
   */
  public PlotFunction (final Function function, final float xMin, final float xMax, final float yMin, final float yMax)
  {
    this.m_aFunction = function;
    this.rectangle = null;
    this.properties = new PlotProperties (xMin, xMax, yMin, yMax);
  }

  /**
   * Constructor.
   * 
   * @param x1
   *        the x-coordinate of uppper left corner of image region
   * @param y1
   *        the y-coordinate of uppper left corner of image region
   * @param x1
   *        the x-coordinate of lower left corner of image region
   * @param y1
   *        the y-coordinate of lower left corner of image region
   */
  private PlotFunction (final int x1, final int y1, final int x2, final int y2)
  {
    rectangle = new Rectangle (x1, y1, x2 - x1 + 1, y2 - y1 + 1);
  }

  /**
   * Return the unwrapped function.
   * 
   * @return the function
   */
  public IEvaluatable getFunction ()
  {
    return m_aFunction;
  }

  /**
   * Return the value of the function at x. (Plottable implementation.)
   * 
   * @return the function value
   */
  public float at (final float x)
  {
    return m_aFunction.at (x);
  }

  /**
   * Return the value of the function's derivative at x.
   * 
   * @return the derivative value
   */
  public float derivativeAt (final float x)
  {
    return m_aFunction.derivativeAt (x);
  }

  // --------------------------//
  // Plottable implementation //
  // --------------------------//

  /**
   * Get the plot function's image rectangle
   * 
   * @return the rectangle
   */
  public Rectangle getRectangle ()
  {
    return rectangle;
  }

  /**
   * Get the plot function's properties
   * 
   * @return the properties
   */
  public PlotProperties getPlotProperties ()
  {
    return properties;
  }
}
