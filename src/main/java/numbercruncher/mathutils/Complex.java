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
 * Perform basic complex arithmetic. The complex objects are immutable, and
 * complex operations create new complex objects.
 */
public class Complex
{
  /** the real part */
  private final float m_fReal;
  /** the imaginary part */
  private final float m_fImaginary;

  /**
   * Constructor.
   * 
   * @param real
   *        the real part
   * @param imaginary
   *        the imaginary part
   */
  public Complex (final float real, final float imaginary)
  {
    this.m_fReal = real;
    this.m_fImaginary = imaginary;
  }

  /**
   * Return this complex number's real part.
   * 
   * @return the real part
   */
  public float real ()
  {
    return m_fReal;
  }

  /**
   * Return this complex number's imaginary part.
   * 
   * @return the imaginary part
   */
  public float imaginary ()
  {
    return m_fImaginary;
  }

  /**
   * Compute this complex number's modulus
   */
  public float modulus ()
  {
    return (float) Math.sqrt (m_fReal * m_fReal + m_fImaginary * m_fImaginary);
  }

  /**
   * Return whether or not this complex number is equal to another one.
   * 
   * @param z
   *        the other complex number
   * @return true if equal, false if not
   */
  public boolean equal (final Complex z)
  {
    return (m_fReal == z.real ()) && (m_fImaginary == z.imaginary ());
  }

  /**
   * Add another complex number to this one.
   * 
   * @param z
   *        the other complex number
   * @return a new complex number that is the sum
   */
  public Complex add (final Complex z)
  {
    return new Complex (m_fReal + z.real (), m_fImaginary + z.imaginary ());
  }

  /**
   * Subtract another complex number from this one.
   * 
   * @param z
   *        the other complex number
   * @return a new complex number that is the difference
   */
  public Complex subtract (final Complex z)
  {
    return new Complex (m_fReal - z.real (), m_fImaginary - z.imaginary ());
  }

  /**
   * Multiply this complex number by another one.
   * 
   * @param z
   *        the other complex number
   * @return a new complex number that is the product
   */
  public Complex multiply (final Complex z)
  {
    return new Complex (m_fReal * z.real () - m_fImaginary * z.imaginary (), m_fReal *
                                                                             z.imaginary () +
                                                                             m_fImaginary *
                                                                             z.real ());
  }

  /**
   * Divide this complex number by another one.
   * 
   * @param z
   *        the other complex number
   * @return a new complex number that is the quotient
   */
  public Complex divide (final Complex z)
  {
    final float denom = z.real () * z.real () + z.imaginary () * z.imaginary ();
    final float qr = (m_fReal * z.real () + m_fImaginary * z.imaginary ()) / denom;
    final float qi = (m_fImaginary * z.real () - m_fReal * z.imaginary ()) / denom;

    return new Complex (qr, qi);
  }

  /**
   * Return the string representation of this complex number.
   * 
   * @return the string representation
   */
  @Override
  public String toString ()
  {
    final String operator = (m_fImaginary >= 0) ? "+" : "-";
    return m_fReal + operator + Math.abs (m_fImaginary) + "i";
  }
}
