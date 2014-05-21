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
package numbercruncher.program6_2;

import numbercruncher.mathutils.DataPoint;
import numbercruncher.mathutils.RegressionLine;

/**
 * PROGRAM 6-2: Linear Regression Demonstrate linear regression by constructing
 * the regression line for a set of data points.
 */
public class LinearRegression
{
  @SuppressWarnings ("unused")
  private static final int MAX_POINTS = 10;

  /**
   * Main program.
   * 
   * @param args
   *        the array of runtime arguments
   */
  public static void main (final String args[])
  {
    final RegressionLine line = new RegressionLine ();

    line.addDataPoint (new DataPoint (6.2f, 6.0f));
    line.addDataPoint (new DataPoint (1.3f, 0.75f));
    line.addDataPoint (new DataPoint (5.5f, 3.05f));
    line.addDataPoint (new DataPoint (2.8f, 2.96f));
    line.addDataPoint (new DataPoint (4.7f, 4.72f));
    line.addDataPoint (new DataPoint (7.9f, 5.81f));
    line.addDataPoint (new DataPoint (3.0f, 2.49f));

    printSums (line);
    printLine (line);
  }

  /**
   * Print the computed sums.
   * 
   * @param line
   *        the regression line
   */
  private static void printSums (final RegressionLine line)
  {
    System.out.println ("n      = " + line.getDataPointCount ());
    System.out.println ("Sum x  = " + line.getSumX ());
    System.out.println ("Sum y  = " + line.getSumY ());
    System.out.println ("Sum xx = " + line.getSumXX ());
    System.out.println ("Sum xy = " + line.getSumXY ());
  }

  /**
   * Print the regression line function.
   * 
   * @param line
   *        the regression line
   */
  private static void printLine (final RegressionLine line)
  {
    System.out.println ("\nRegression line:  y = " + line.getA1 () + "x + " + line.getA0 ());
  }
}
/*
 * Output: n = 7 Sum x = 31.399999618530273 Sum y = 25.77999973297119 Sum xx =
 * 171.71999621391296 Sum xy = 138.7909932732582 Regression line: y =
 * 0.74993044x + 0.31888318
 */
