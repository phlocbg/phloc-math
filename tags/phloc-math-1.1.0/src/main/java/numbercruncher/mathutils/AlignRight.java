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
 * Print text and numbers right-aligned in columns.
 */
public class AlignRight
{
  /** line size */
  private int lineSize;

  /**
   * Constructor.
   */
  public AlignRight ()
  {}

  /**
   * Print text right-aligned in the column.
   * 
   * @param text
   *        the text to print
   * @param width
   *        the column width
   */
  public void print (final String text, final int width)
  {
    int padding = width - text.length ();
    while (--padding >= 0)
      System.out.print (" ");
    System.out.print (text);

    lineSize += width;
  }

  /**
   * Print an integer value right-aligned in the column.
   * 
   * @param value
   *        the value to print
   * @param width
   *        the column width
   */
  public void print (final int value, final int width)
  {
    print (Integer.toString (value), width);
  }

  /**
   * Print a float value right-aligned in the column.
   * 
   * @param value
   *        the value to print
   */
  public void print (final float value, final int width)
  {
    print (Float.toString (value), width);
  }

  /**
   * Print a double value right-aligned in the column.
   * 
   * @param value
   *        the value to print
   * @param width
   *        the column width
   */
  public void print (final double value, final int width)
  {
    print (Double.toString (value), width);
  }

  /**
   * Print a line.
   */
  public void println ()
  {
    System.out.println ();
    lineSize = 0;
  }

  /**
   * Print an underline.
   */
  public void underline ()
  {
    System.out.println ();
    for (int i = 0; i < lineSize; ++i)
      System.out.print ("-");
    System.out.println ();
    lineSize = 0;
  }
}
