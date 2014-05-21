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
 * Constants related to the IEEE 754 standard.
 */
public final class IEEE754Constants
{
  public static final int FLOAT_SIGN_INDEX = 0;
  public static final int FLOAT_SIGN_SIZE = 1;
  public static final int FLOAT_EXPONENT_INDEX = 1;
  public static final int FLOAT_EXPONENT_SIZE = 8;
  public static final int FLOAT_EXPONENT_RESERVED = 255;
  public static final int FLOAT_EXPONENT_BIAS = 127;
  public static final int FLOAT_FRACTION_INDEX = 9;
  public static final int FLOAT_FRACTION_SIZE = 23;

  public static final int DOUBLE_SIGN_INDEX = 0;
  public static final int DOUBLE_SIGN_SIZE = 1;
  public static final int DOUBLE_EXPONENT_INDEX = 1;
  public static final int DOUBLE_EXPONENT_SIZE = 11;
  public static final int DOUBLE_EXPONENT_RESERVED = 2047;
  public static final int DOUBLE_EXPONENT_BIAS = 1023;
  public static final int DOUBLE_FRACTION_INDEX = 12;
  public static final int DOUBLE_FRACTION_SIZE = 52;

  private IEEE754Constants ()
  {}
}
