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
package numbercruncher.matrix;

public class MatrixException extends Exception
{
  public static final String INVALID_INDEX = "Invalid index.";
  public static final String INVALID_DIMENSIONS = "Invalid matrix dimensions.";
  public static final String ZERO_ROW = "Matrix has a zero row.";
  public static final String SINGULAR = "Matrix is singular.";
  public static final String NO_CONVERGENCE = "Solution did not converge.";

  /**
   * Constructor.
   * 
   * @param msg
   *        the error message
   */
  public MatrixException (final String msg)
  {
    super (msg);
  }
}
