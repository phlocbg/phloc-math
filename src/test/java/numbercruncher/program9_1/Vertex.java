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
package numbercruncher.program9_1;

import numbercruncher.matrix.MatrixException;
import numbercruncher.matrix.RowVector;
import numbercruncher.matrix.SquareMatrix;

/**
 * Represent a vertex of the wire-frame cube in three dimensions.
 */
class Vertex extends RowVector
{
  /**
   * Constructor.
   * 
   * @param x
   *        the x value
   * @param y
   *        the y value
   * @param z
   *        the z value
   */
  Vertex (final float x, final float y, final float z)
  {
    super (4);

    m_aValues[0][0] = x;
    m_aValues[0][1] = y;
    m_aValues[0][2] = z;
    m_aValues[0][3] = 1;
  }

  /**
   * Return this vertex's x value.
   * 
   * @return the x value
   */
  float x ()
  {
    return m_aValues[0][0];
  }

  /**
   * Return this vertex's y value.
   * 
   * @return the y value
   */
  float y ()
  {
    return m_aValues[0][1];
  }

  /**
   * Return this vertex's z value.
   * 
   * @return the z value
   */
  float z ()
  {
    return m_aValues[0][2];
  }

  /**
   * Transform this vector by multiplying it by a transformation matrix.
   * 
   * @param t
   *        the transformation matrix
   */
  void multiply (final SquareMatrix t) throws MatrixException
  {
    final RowVector rv = t.multiply (this);
    this.m_aValues = rv.values ();
  }
}
