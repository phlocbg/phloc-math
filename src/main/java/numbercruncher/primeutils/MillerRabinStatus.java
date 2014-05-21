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
 * The current status of the Miller-Rabin test.
 */
public class MillerRabinStatus
{
  // Status codes
  public static final int DONT_KNOW_YET = 0;
  public static final int DEFINITELY_COMPOSITE = 1;
  public static final int PROBABLY_PRIME = 2;

  /** random base */
  int b;
  /** shifted p-1 */
  int k;
  /** no. of right shifts */
  int s;
  /** counter */
  int i;
  /** modulo value */
  int r;
  /** status code */
  int code;

  public int getB ()
  {
    return b;
  }

  public int getK ()
  {
    return k;
  }

  public int getS ()
  {
    return s;
  }

  public int getIndex ()
  {
    return i;
  }

  public int getValue ()
  {
    return r;
  }

  public int getCode ()
  {
    return code;
  }
}
