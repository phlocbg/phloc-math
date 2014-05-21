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
package numbercruncher.primeutils;

/**
 * The current status of the Lucas test.
 */
public class LucasStatus
{
  /** trial integer a */
  int a;
  /** prime factor */
  int q;
  /** exponent of a */
  int exponent;
  /** modulo value */
  int value;
  /** pass or fail */
  boolean pass;

  public int getA ()
  {
    return a;
  }

  public int getQ ()
  {
    return q;
  }

  public int getExponent ()
  {
    return exponent;
  }

  public int getValue ()
  {
    return value;
  }

  public boolean didPass ()
  {
    return pass;
  }
}
