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

import java.awt.Color;
import java.awt.Graphics;

/**
 * A wire-frame cube to transform and display.
 */
class WireFrameCube
{
  /**
   * Represent each face of the cube.
   */
  private class Face
  {
    /** indices of the face's vertices */
    int indices[];

    /**
     * Constructor.
     * 
     * @param v1
     *        the first vertex
     * @param v2
     *        the second vertex
     * @param v3
     *        the third vertex
     * @param v4
     *        the fourth vertex
     */
    Face (final int v1, final int v2, final int v3, final int v4)
    {
      indices = new int [] { v1, v2, v3, v4 };
    }
  }

  /** The cube's vertices. */
  private final Vertex vertices[] = { new Vertex (-0.5f, -0.5f, -0.5f),
                                     new Vertex (+0.5f, -0.5f, -0.5f),
                                     new Vertex (-0.5f, +0.5f, -0.5f),
                                     new Vertex (+0.5f, +0.5f, -0.5f),
                                     new Vertex (-0.5f, -0.5f, +0.5f),
                                     new Vertex (+0.5f, -0.5f, +0.5f),
                                     new Vertex (-0.5f, +0.5f, +0.5f),
                                     new Vertex (+0.5f, +0.5f, +0.5f), };

  /** The cube's faces. */
  private final Face faces[] = { new Face (0, 1, 3, 2),
                                new Face (0, 1, 5, 4),
                                new Face (2, 3, 7, 6),
                                new Face (0, 4, 6, 2),
                                new Face (1, 5, 7, 3),
                                new Face (4, 5, 7, 6), };

  /**
   * Draw the transformed cube.
   * 
   * @param g
   *        the graphics context
   * @param transformation
   *        the transformation to apply
   */
  void draw (final Graphics g, final Transformation transformation)
  {
    // Transform the vertices.
    transformation.transform (vertices);

    // Loop for each face.
    for (final Face face : faces)
    {
      final int indices[] = face.indices;

      // Draw the edges of the face.
      for (int j = 0; j < indices.length; ++j)
      {
        final int k = (j + 1) % indices.length;
        final int c1 = Math.round (vertices[indices[j]].x ());
        final int r1 = Math.round (vertices[indices[j]].y ());
        final int c2 = Math.round (vertices[indices[k]].x ());
        final int r2 = Math.round (vertices[indices[k]].y ());

        // Set the color based on the edge's position.
        final Color color = transformation.behindCenter (vertices[indices[j]], vertices[indices[k]]) ? Color.lightGray
                                                                                                    : Color.black;

        // Draw the edge.
        g.setColor (color);
        g.drawLine (c1, r1, c2, r2);
      }
    }
  }
}
