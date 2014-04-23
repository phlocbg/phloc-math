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
import java.awt.Image;
import java.awt.Panel;
import java.awt.Rectangle;

/**
 * The panel that represents the enclosed 3-D space for the tumbling wire-frame
 * cube.
 */
public class CubePanel extends Panel
{
  private static final float MAX_TRANSLATE = 5;
  private static final float MAX_SCALING = 3;

  /** width of space */
  private int width;
  /** height of space */
  private int height;
  /** depth of space */
  private int depth;

  /** image buffer */
  private Image buffer;
  /** buffer graphics context */
  private Graphics bg;

  /** true for first draw */
  private boolean first = true;

  /** wire frame cube */
  private WireFrameCube m_aCube;
  /** transformation */
  private final Transformation m_aTransformation;
  /** parent panel */
  private final TransformationPanel m_aParent;

  /**
   * Constructor.
   * 
   * @param transformation
   *        the graphics transformation
   * @param parent
   *        the parent panel
   */
  CubePanel (final Transformation transformation, final TransformationPanel parent)
  {
    this.m_aTransformation = transformation;
    this.m_aParent = parent;
    this.m_aCube = new WireFrameCube ();

    setBackground (Color.white);
  }

  /**
   * Reset the cube to its starting position.
   */
  public void reset ()
  {
    m_aCube = new WireFrameCube ();
    first = true;
    bg = null;

    repaint ();
  }

  /**
   * Draw the contents of the panel.
   */
  public void draw ()
  {
    if (bg == null)
      return;
    bg.clearRect (0, 0, width, height);

    m_aTransformation.init ();

    if (first)
    {
      firstDraw ();
    }
    else
    {
      subsequentDraw ();
    }

    repaint ();
    m_aParent.updateMatrixDisplay ();
  }

  /**
   * Paint without first clearing.
   * 
   * @param g
   *        the graphics context
   */
  @Override
  public void update (final Graphics g)
  {
    paint (g);
  }

  /**
   * Paint the contents of the image buffer.
   * 
   * @param g
   *        the graphics context
   */
  @Override
  public void paint (final Graphics g)
  {
    // Has the buffer been created?
    if (bg == null)
    {
      final Rectangle r = getBounds ();

      width = r.width;
      height = r.height;
      depth = width;

      // Create the image buffer and get its graphics context.
      buffer = createImage (width, height);
      bg = buffer.getGraphics ();

      draw ();
    }

    // Paint the buffer contents.
    g.drawImage (buffer, 0, 0, null);
  }

  /**
   * First time drawing.
   */
  private void firstDraw ()
  {
    // Scale and move to the center.
    m_aTransformation.setScaling (50, 50, 50);
    m_aTransformation.setTranslation (width / 2, height / 2, depth / 2);

    m_aCube.draw (bg, m_aTransformation);

    // Random subsequent translations.
    final float xDelta = (float) (2 * MAX_TRANSLATE * Math.random () - MAX_TRANSLATE);
    final float yDelta = (float) (2 * MAX_TRANSLATE * Math.random () - MAX_TRANSLATE);
    final float zDelta = (float) (2 * MAX_TRANSLATE * Math.random () - MAX_TRANSLATE);

    m_aTransformation.setTranslation (xDelta, yDelta, zDelta);

    // Set the scale factor based on the space's depth and
    // whether the cube is moving towards or away from the viewer.
    // At maximum z, the cube should be twice its original size,
    // and at minimum z, it should be half its original size.
    final float steps = (depth / 2) / Math.abs (zDelta);
    float scaleFactor = (float) Math.pow (MAX_SCALING, 1 / steps);
    if (zDelta < 0)
      scaleFactor = 1 / scaleFactor;

    m_aTransformation.setScaling (scaleFactor, scaleFactor, scaleFactor);

    setRandomRotation ();
    first = false;
  }

  /**
   * Subsequent drawing.
   */
  private void subsequentDraw ()
  {
    // Draw the transformed cube.
    m_aCube.draw (bg, m_aTransformation);

    // If there was a bounce, set new random rotation angles.
    if (m_aTransformation.bounced (width, height, depth))
    {
      setRandomRotation ();
    }
  }

  /**
   * Set random rotation angles about the axes.
   */
  private void setRandomRotation ()
  {
    m_aTransformation.setRotation ((float) (0.1 * Math.random ()), // x axis
                                   (float) (0.1 * Math.random ()), // y axis
                                   (float) (0.1 * Math.random ())); // z axis
  }
}
