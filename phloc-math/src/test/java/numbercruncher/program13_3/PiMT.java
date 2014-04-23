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
package numbercruncher.program13_3;

import java.text.SimpleDateFormat;
import java.util.Date;

import numbercruncher.piutils.PiFormula;

/**
 * PROGRAM 13-3: The Borwein Pi Algorithm Compute digits of pi by the Borwein
 * algorithm.
 */
public class PiMT extends PiFormula implements IPiBorweinParent
{
  private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat ("HH:mm:ss.SSS");

  private long m_nStartTime;
  private long m_nMarkTime;
  /** number of digits to compute */
  private int m_nDigits;
  /** the Borwein algorithm */
  private PiMTAlgorithm algorithm;

  /**
   * Compute the digits of pi using the Borwein algorithm.
   * 
   * @param digits
   *        the number of digits of pi to compute
   */
  private void compute (final int digits)
  {
    this.m_nDigits = digits;

    m_nStartTime = System.currentTimeMillis ();
    System.out.println (timestamp (m_nStartTime) + " START TIME\n");

    algorithm = new PiMTAlgorithm (digits, this);
    algorithm.compute ();
  }

  /**
   * Main.
   * 
   * @param args
   *        the array of program arguments
   */
  public static void main (final String args[])
  {
    final PiMT pi = new PiMT ();

    try
    {
      final int digits = Integer.parseInt (args[0]);
      pi.compute (digits);
    }
    catch (final Exception ex)
    {
      System.out.println ("ERROR: " + ex.getMessage ());
    }
  }

  // -----------------------------------//
  // Implementation of PiBorweinParent //
  // -----------------------------------//

  /**
   * Scale notification.
   * 
   * @param scale
   *        the scale being used
   */
  public void notifyScale (final int scale)
  {
    System.out.println ("digits = " + m_nDigits);
    System.out.println ("scale  = " + scale);
    System.out.println ();
  }

  /**
   * Phase notification.
   * 
   * @param phase
   *        the current phase
   */
  public void notifyPhase (final int phase)
  {
    switch (phase)
    {

      case PiBorweinConstants.INITIALIZING:
      {
        System.out.print ("\n" + timestamp (m_nStartTime) + " Initialization:");
        break;
      }

      case PiBorweinConstants.INVERTING:
      {
        System.out.println ("\n" + timestamp (m_nMarkTime) + " Inverting");
        break;
      }

      case PiBorweinConstants.DONE:
      {
        final String totalTime = timestamp (m_nStartTime);
        printPi (algorithm.getPi ());

        System.out.println ("\n" + algorithm.getIterations () + " iterations");
        System.out.println (totalTime + " TOTAL COMPUTE TIME");

        break;
      }

      default:
      {
        System.out.print ("\n" + timestamp (m_nMarkTime) + " Iteration " + phase + ":");
        break;
      }
    }

    m_nMarkTime = System.currentTimeMillis ();
  }

  /**
   * Task notification.
   * 
   * @param task
   *        the current computation task
   */
  public void notifyTask (final String task)
  {
    final String tString = TIME_FORMAT.format (new Date ());
    System.out.println (tString + " " + task);
  }
}
