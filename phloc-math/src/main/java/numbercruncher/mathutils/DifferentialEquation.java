package numbercruncher.mathutils;

/**
 * The base class for functions that can have derivatives. Initialize the static
 * function table with some sample functions.
 */
public abstract class DifferentialEquation implements IEvaluatable
{
  /** initial condition */
  private final DataPoint m_aInitialCondition;
  /** solution function label */
  private final String m_sSolutionLabel;

  /**
   * Constructor.
   * 
   * @param initialCondition
   *        the initial condition data point
   * @param solutionLabel
   *        the solution function label
   */
  public DifferentialEquation (final DataPoint initialCondition, final String solutionLabel)
  {
    this.m_aInitialCondition = initialCondition;
    this.m_sSolutionLabel = solutionLabel;
  }

  /**
   * Return the initial condition data point.
   * 
   * @return the initial condition
   */
  public DataPoint getInitialCondition ()
  {
    return m_aInitialCondition;
  }

  /**
   * Return the solution label.
   * 
   * @return the label
   */
  public String getSolutionLabel ()
  {
    return m_sSolutionLabel;
  }

  /**
   * Return the value of the differential equation at x. (Implementation of
   * {@link IEvaluatable}.)
   * 
   * @param x
   *        the value of x
   * @return the solution value
   */
  public abstract float at (float x);

  /**
   * Return the value of the differential equation at (x, y).
   * 
   * @param x
   * @param y
   * @return the solution value
   */
  public float at (final float x, final float y)
  {
    return at (x);
  }

  /**
   * Return the value of the solution at x.
   * 
   * @return the solution value
   */
  public abstract float solutionAt (float x);
}
