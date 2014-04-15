package numbercruncher.mathutils;

/**
 * Differential equation solver that implements a predictor-corrector algorithm.
 */
public class PredictorCorrectorDiffEqSolver extends DiffEqSolver
{
  /**
   * Constructor.
   * 
   * @param equation
   *        the differential equation to solve
   */
  public PredictorCorrectorDiffEqSolver (final DifferentialEquation equation)
  {
    super (equation);
  }

  /**
   * Return the next data point in the approximation of the solution.
   * 
   * @param h
   *        the width of the interval
   */
  @Override
  public DataPoint nextPoint (final float h)
  {
    final float predictor = y + Math.abs (h) * m_aEquation.at (x);
    final float avgSlope = (m_aEquation.at (x, y) + m_aEquation.at (x + h, predictor)) / 2;

    y += h * avgSlope; // corrector
    x += h;

    return new DataPoint (x, y);
  }
}
