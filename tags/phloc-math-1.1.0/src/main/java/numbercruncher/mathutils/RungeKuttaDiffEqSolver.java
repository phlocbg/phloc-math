/**
 * Copyright (C) 2006-2014 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package numbercruncher.mathutils;

/**
 * Differential equation solver that implements a fourth-order Runge-Kutta
 * algorithm.
 */
public class RungeKuttaDiffEqSolver extends DiffEqSolver
{
  /**
   * Constructor.
   * 
   * @param equation
   *        the differential equation to solve
   */
  public RungeKuttaDiffEqSolver (final DifferentialEquation equation)
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
    final float k1 = m_aEquation.at (x, y);
    final float k2 = m_aEquation.at (x + h / 2, y + k1 * h / 2);
    final float k3 = m_aEquation.at (x + h / 2, y + k2 * h / 2);
    final float k4 = m_aEquation.at (x + h, y + k3 * h);

    y += (k1 + 2 * (k2 + k3) + k4) * h / 6;
    x += h;

    return new DataPoint (x, y);
  }
}
