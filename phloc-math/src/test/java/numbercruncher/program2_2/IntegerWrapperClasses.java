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
package numbercruncher.program2_2;

/**
 * PROGRAM 2-2: Integer Wrapper Classes Print the values of some of the
 * constants defined in the integer wrapper classes.
 */
public class IntegerWrapperClasses
{
  public static void main (final String args[])
  {
    // Byte
    System.out.println ("Byte.MIN_VALUE = " + Byte.MIN_VALUE);
    System.out.println ("Byte.MAX_VALUE = " + Byte.MAX_VALUE);
    System.out.println ();

    // Short
    System.out.println ("Short.MIN_VALUE = " + Short.MIN_VALUE);
    System.out.println ("Short.MAX_VALUE = " + Short.MAX_VALUE);
    System.out.println ();

    // Character
    System.out.println ("Character.MIN_VALUE = " + (int) Character.MIN_VALUE);
    System.out.println ("Character.MAX_VALUE = " + (int) Character.MAX_VALUE);
    System.out.println ();

    // Integer
    System.out.println ("Integer.MIN_VALUE = " + Integer.MIN_VALUE);
    System.out.println ("Binary: " + Integer.toBinaryString (Integer.MIN_VALUE));
    System.out.println ("   Hex: " + Integer.toHexString (Integer.MIN_VALUE));
    System.out.println ();
    System.out.println ("Integer.MAX_VALUE = " + Integer.MAX_VALUE);
    System.out.println ("Binary: " + Integer.toBinaryString (Integer.MAX_VALUE));
    System.out.println ("   Hex: " + Integer.toHexString (Integer.MAX_VALUE));
    System.out.println ();

    // Long
    System.out.println ("Long.MIN_VALUE = " + Long.MIN_VALUE);
    System.out.println ("Binary: " + Long.toBinaryString (Long.MIN_VALUE));
    System.out.println ("   Hex: " + Long.toHexString (Long.MIN_VALUE));
    System.out.println ();
    System.out.println ("Long.MAX_VALUE = " + Long.MAX_VALUE);
    System.out.println ("Binary: " + Long.toBinaryString (Long.MAX_VALUE));
    System.out.println ("   Hex: " + Long.toHexString (Long.MAX_VALUE));
  }
}
/*
 * Output: Byte.MIN_VALUE = -128 Byte.MAX_VALUE = 127 Short.MIN_VALUE = -32768
 * Short.MAX_VALUE = 32767 Character.MIN_VALUE = 0 Character.MAX_VALUE = 65535
 * Integer.MIN_VALUE = -2147483648 Binary: 10000000000000000000000000000000 Hex:
 * 80000000 Integer.MAX_VALUE = 2147483647 Binary:
 * 1111111111111111111111111111111 Hex: 7fffffff Long.MIN_VALUE =
 * -9223372036854775808 Binary:
 * 1000000000000000000000000000000000000000000000000000000000000000 Hex:
 * 8000000000000000 Long.MAX_VALUE = 9223372036854775807 Binary:
 * 111111111111111111111111111111111111111111111111111111111111111 Hex:
 * 7fffffffffffffff
 */
