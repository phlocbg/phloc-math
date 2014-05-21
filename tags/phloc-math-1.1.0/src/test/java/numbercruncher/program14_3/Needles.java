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
package numbercruncher.program14_3;

import java.util.Random;

/**
 * Implementation of Buffon's needles, which are randomly dropped onto a ruled
 * sheet of paper.
 */
class Needles
{
  /** x coord of one end of a needle */
  private float x1;
  /** x coord of the other end of a needle */
  private float x2;
  /** y coord of one end of a needle */
  private float y1;
  /** y coord of the other end of a needle */
  private float y2;
  /** angle of rotation about the midpoint */
  @SuppressWarnings ("unused")
  private float m_fTheta;

  /** paper's left edge */
  private final float m_fXMin;
  /** paper's width */
  private final float xWidth;
  /** paper's bottom edge */
  private final float m_fYMin;
  /** paper's height */
  private final float yHeight;

  /** count of dropped needles */
  private int count;
  /** number that cross a line */
  private int crossings;
  /** current computed value of pi */
  private float pi;

  /** generator of uniformly-distributed random values */
  private final Random random = new Random (System.currentTimeMillis ());

  /**
   * Constructor.
   * 
   * @param xMin
   *        the paper's left edge
   * @param xMax
   *        the paper's right edge
   * @param yMin
   *        the paper's bottom edge
   * @param yMax
   *        the paper's top edge
   */
  Needles (final float xMin, final float xMax, final float yMin, final float yMax)
  {
    this.m_fXMin = xMin;
    this.xWidth = xMax - xMin;
    this.m_fYMin = yMin;
    this.yHeight = yMax - yMin;
  }

  /**
   * Return the x coordinate of one end of the needle.
   * 
   * @return the coordinate
   */
  float getX1 ()
  {
    return x1;
  }

  /**
   * Return the x coordinate of one end of the needle.
   * 
   * @return the coordinate
   */
  float getX2 ()
  {
    return x2;
  }

  /**
   * Return the y coordinate of one end of the needle.
   * 
   * @return the coordinate
   */
  float getY1 ()
  {
    return y1;
  }

  /**
   * Return the y coordinate of the other end of the needle.
   * 
   * @return the coordinate
   */
  float getY2 ()
  {
    return y2;
  }

  /**
   * Return the count of all the dropped needles.
   * 
   * @return the count
   */
  int getCount ()
  {
    return count;
  }

  /**
   * Return the number of needles that crossed a line.
   * 
   * @return the number of crossings
   */
  int getCrossings ()
  {
    return crossings;
  }

  /**
   * Return the current computed value of pi
   * 
   * @return the value
   */
  float getPi ()
  {
    return pi = (2f * count) / crossings;
  }

  /**
   * Return the error of the current computed value of pi
   * 
   * @return the error
   */
  float getError ()
  {
    return pi - ((float) Math.PI);
  }

  /**
   * Initialize.
   */
  void init ()
  {
    count = crossings = 0;
  }

  /**
   * Drop the next needle.
   */
  void dropNext ()
  {
    ++count;

    // Compute random values for the x and y coordinates of the
    // needle's midpoint, and for the needle's angle of rotation.
    final float xCenter = xWidth * random.nextFloat () + m_fXMin;
    final float yCenter = yHeight * random.nextFloat () + m_fYMin;
    final float theta = (float) Math.PI * random.nextFloat ();

    final float sin = (float) Math.sin (theta);
    final float cos = (float) Math.cos (theta);

    // Rotate about the origin a 1-unit length needle on
    // the x axis with endpoints at -0.5 and +0.5.
    x1 = -0.5f * cos;
    x2 = +0.5f * cos;
    y1 = -0.5f * (-sin);
    y2 = +0.5f * (-sin);

    // Translate the needle to its location.
    x1 += xCenter;
    x2 += xCenter;
    y1 += yCenter;
    y2 += yCenter;

    // Does the needle cross a line?
    if (Math.floor (y1) != Math.floor (y2))
      ++crossings;
  }
}
