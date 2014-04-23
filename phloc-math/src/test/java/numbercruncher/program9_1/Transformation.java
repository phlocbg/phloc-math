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

import numbercruncher.matrix.IdentityMatrix;
import numbercruncher.matrix.MatrixException;
import numbercruncher.matrix.SquareMatrix;

/**
 * Transformations of a graphic image.
 */
class Transformation
{
  /** translation matrix */
  private final SquareMatrix m_aTranslate = new IdentityMatrix (4);

  /** scaling matrix */
  private final SquareMatrix scale = new IdentityMatrix (4);

  /** matrix to rotate about the x axis */
  private final SquareMatrix rotateX = new IdentityMatrix (4);

  /** matrix to rotate about the y axis */
  private final SquareMatrix rotateY = new IdentityMatrix (4);

  /** matrix to rotate about the z axis */
  private final SquareMatrix rotateZ = new IdentityMatrix (4);

  /** concatenated rotation matrix */
  private SquareMatrix rotate = new IdentityMatrix (4);

  /** concatenated transformation matrix */
  private SquareMatrix transform = new IdentityMatrix (4);

  /** center of rotation */
  private Vertex center = new Vertex (0, 0, 0);

  /**
   * Initialize for a new set of transformations.
   */
  void init ()
  {
    IdentityMatrix.convert (transform);
  }

  /**
   * Reset to the initial conditions.
   */
  void reset ()
  {
    center = new Vertex (0, 0, 0);

    setTranslation (0, 0, 0);
    setScaling (1, 1, 1);
    setRotation (0, 0, 0);
  }

  /**
   * Set the translation matrix.
   * 
   * @param tx
   *        the change in the x direction
   * @param ty
   *        the change in the y direction
   * @param tz
   *        the change in the z direction
   */
  void setTranslation (final float tx, final float ty, final float tz)
  {
    try
    {
      m_aTranslate.set (3, 0, tx);
      m_aTranslate.set (3, 1, ty);
      m_aTranslate.set (3, 2, tz);
    }
    catch (final MatrixException ex)
    {}
  }

  /**
   * Set the scaling matrix.
   * 
   * @param sx
   *        the scaling factor in the x direction
   * @param sy
   *        the scaling factor in the y direction
   * @param sz
   *        the scaling factor in the z direction
   */
  void setScaling (final float sx, final float sy, final float sz)
  {
    try
    {
      scale.set (0, 0, sx);
      scale.set (1, 1, sy);
      scale.set (2, 2, sz);
    }
    catch (final MatrixException ex)
    {}
  }

  /**
   * Set the rotation matrix.
   * 
   * @param thetaX
   *        amount (in radians) to rotate around the x axis
   * @param thetaY
   *        amount (in radians) to rotate around the y axis
   * @param thetaZ
   *        amount (in radians) to rotate around the z axis
   */
  void setRotation (final float thetaX, final float thetaY, final float thetaZ)
  {
    try
    {
      float sin = (float) Math.sin (thetaX);
      float cos = (float) Math.cos (thetaX);

      // Rotate about the x axis.
      rotateX.set (1, 1, cos);
      rotateX.set (1, 2, -sin);
      rotateX.set (2, 1, sin);
      rotateX.set (2, 2, cos);

      sin = (float) Math.sin (thetaY);
      cos = (float) Math.cos (thetaY);

      // Rotate about the y axis.
      rotateY.set (0, 0, cos);
      rotateY.set (0, 2, sin);
      rotateY.set (2, 0, -sin);
      rotateY.set (2, 2, cos);

      sin = (float) Math.sin (thetaZ);
      cos = (float) Math.cos (thetaZ);

      // Rotate about the z axis.
      rotateZ.set (0, 0, cos);
      rotateZ.set (0, 1, -sin);
      rotateZ.set (1, 0, sin);
      rotateZ.set (1, 1, cos);

      // Concatenate rotations.
      rotate = rotateX.multiply (rotateY.multiply (rotateZ));
    }
    catch (final MatrixException ex)
    {}
  }

  /**
   * Transform a set of vertices based on previously-set translation, scaling,
   * and rotation. Concatenate the transformations in the order: scale, rotate,
   * translate.
   * 
   * @param vertices
   *        the vertices to transform
   */
  void transform (final Vertex vertices[])
  {
    // Scale and rotate about the origin.
    toOrigin ();
    scale ();
    rotate ();
    reposition ();

    translate ();

    // Apply the concatenated transformations.
    try
    {

      // Do the vertices.
      for (final Vertex v : vertices)
      {
        v.multiply (transform);
      }

      // Do the center of rotation.
      center.multiply (transform);
    }
    catch (final MatrixException ex)
    {}
  }

  /**
   * Check for a bounce against any wall of the space. Return true if bounced.
   * 
   * @param width
   *        the width of the space
   * @param height
   *        the height of the space
   * @param depth
   *        the depth of the space
   * @return true if bounced, else false
   */
  boolean bounced (final float width, final float height, final float depth)
  {
    boolean b = false;

    try
    {

      // Bounced off the sides?
      if ((center.x () < 0) || (center.x () > width))
      {
        m_aTranslate.set (3, 0, -m_aTranslate.at (3, 0));
        b = true;
      }

      // Bounced off the top or bottom?
      if ((center.y () < 0) || (center.y () > height))
      {
        m_aTranslate.set (3, 1, -m_aTranslate.at (3, 1));
        b = true;
      }

      // Bounced off the front or back?
      if ((center.z () < 0) || (center.z () > depth))
      {
        m_aTranslate.set (3, 2, -m_aTranslate.at (3, 2));

        // Invert the scale factor.
        final float scaleFactor = 1 / scale.at (0, 0);

        scale.set (0, 0, scaleFactor);
        scale.set (1, 1, scaleFactor);
        scale.set (2, 2, scaleFactor);

        b = true;
      }
    }
    catch (final MatrixException ex)
    {}

    return b;
  }

  /**
   * Check if a line is behind the center of rotation.
   * 
   * @param v1
   *        the vertex of one end of the line
   * @param v2
   *        the vertex of the other end of the line
   * @return true if behind, else false
   */
  boolean behindCenter (final Vertex v1, final Vertex v2)
  {
    return (v1.z () < center.z ()) && (v2.z () < center.z ());
  }

  /**
   * Return a value from the concatenated transformation matrix.
   * 
   * @param r
   *        the value's row
   * @param c
   *        the value's column
   * @return the value
   */
  float at (final int r, final int c)
  {
    try
    {
      return transform.at (r, c);
    }
    catch (final MatrixException ex)
    {
      return Float.NaN;
    }
  }

  /**
   * Concatenate a translation.
   * 
   * @param translate
   *        the translation matrix to use
   */
  private void translate (final SquareMatrix translate)
  {
    try
    {
      transform = transform.multiply (translate);
    }
    catch (final MatrixException ex)
    {}
  }

  /**
   * Concatenate the preset translation.
   */
  private void translate ()
  {
    translate (m_aTranslate);
  }

  /**
   * Concatenate the preset scaling.
   */
  private void scale ()
  {
    try
    {
      transform = transform.multiply (scale);
    }
    catch (final MatrixException ex)
    {}
  }

  /**
   * Concatenate the preset rotation.
   */
  private void rotate ()
  {
    try
    {
      transform = transform.multiply (rotate);
    }
    catch (final MatrixException ex)
    {}
  }

  /**
   * Translate back to the origin.
   */
  private void toOrigin ()
  {
    try
    {
      final SquareMatrix tempTranslate = new IdentityMatrix (4);

      tempTranslate.set (3, 0, -center.x ());
      tempTranslate.set (3, 1, -center.y ());
      tempTranslate.set (3, 2, -center.z ());

      translate (tempTranslate);
    }
    catch (final MatrixException ex)
    {}
  }

  /**
   * Translate back into position.
   */
  private void reposition ()
  {
    try
    {
      final SquareMatrix tempTranslate = new IdentityMatrix (4);

      tempTranslate.set (3, 0, center.x ());
      tempTranslate.set (3, 1, center.y ());
      tempTranslate.set (3, 2, center.z ());

      translate (tempTranslate);
    }
    catch (final MatrixException ex)
    {}
  }
}
