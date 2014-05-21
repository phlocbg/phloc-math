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
package numbercruncher.program2_1;

/**
 * PROGRAM 2-1: Integer Overflow Show the effects of integer overflow and of
 * division by zero.
 */
public class IntegerOverflow
{
  public static void main (final String args[])
  {
    final int big = 2147483645;

    for (int i = 1; i <= 4; ++i)
    {
      System.out.println (big + " + " + i + " = " + (big + i));
    }
    System.out.println ();

    for (int i = 1; i <= 4; ++i)
    {
      System.out.println (big + " * " + i + " = " + (big * i));
    }
    System.out.println ();

    @SuppressWarnings ("unused")
    final int dze = big / 0;
  }
}
/*
 * Output: 2147483645 + 1 = 2147483646 2147483645 + 2 = 2147483647 2147483645 +
 * 3 = -2147483648 2147483645 + 4 = -2147483647 2147483645 * 1 = 2147483645
 * 2147483645 * 2 = -6 2147483645 * 3 = 2147483639 2147483645 * 4 = -12
 * java.lang.ArithmeticException: / by zero at
 * numbercruncher.program2_1.IntegerOverflow.main(IntegerOverflow.java:24)
 * Exception in thread "main"
 */
