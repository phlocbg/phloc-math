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
 * Decompose a floating-point value into its parts according to the IEEE 754
 * standard.
 */
public class IEEE754
{
  /** sign bit as a string */
  private String signBit;
  /** exponent bits as a string */
  private String exponentBits;
  /** fraction bits as a string */
  private String fractionBits;
  /** implied bit as a string */
  private String impliedBit;

  /** biased exponent value */
  private int biased;
  /** fraction value */
  private long m_nFraction;

  /** exponent bias */
  private int m_nBias;

  /** float number value */
  private float floatValue;
  /** double number value */
  private double doubleValue;

  /**
   * true if number value is zero
   */
  private boolean isZero;
  /**
   * true if reserved exponent value
   */
  private boolean isReserved;
  /**
   * true if number type is double
   */
  private final boolean isDouble;
  /**
   * true if denormalized number value
   */
  private boolean isDenormalized;

  // --------------//
  // Constructors //
  // --------------//

  /**
   * Constructor. Decompose a float value.
   * 
   * @param value
   *        the float value to decompose
   */
  public IEEE754 (final float value)
  {
    // Convert the value to a character array of '0' and '1'.
    final char bits[] = toCharBitArray (Float.floatToIntBits (value), 32);

    floatValue = value;
    isDouble = false;

    decompose (bits,
               IEEE754Constants.FLOAT_EXPONENT_BIAS,
               IEEE754Constants.FLOAT_EXPONENT_RESERVED,
               IEEE754Constants.FLOAT_SIGN_INDEX,
               IEEE754Constants.FLOAT_SIGN_SIZE,
               IEEE754Constants.FLOAT_EXPONENT_INDEX,
               IEEE754Constants.FLOAT_EXPONENT_SIZE,
               IEEE754Constants.FLOAT_FRACTION_INDEX,
               IEEE754Constants.FLOAT_FRACTION_SIZE);
  }

  /**
   * Constructor. Decompose a double value.
   * 
   * @param value
   *        the double-precision value to decompose
   */
  public IEEE754 (final double value)
  {
    // Convert the value to a character array of '0' and '1'.
    final char bits[] = toCharBitArray (Double.doubleToLongBits (value), 64);

    doubleValue = value;
    isDouble = true;

    decompose (bits,
               IEEE754Constants.DOUBLE_EXPONENT_BIAS,
               IEEE754Constants.DOUBLE_EXPONENT_RESERVED,
               IEEE754Constants.DOUBLE_SIGN_INDEX,
               IEEE754Constants.DOUBLE_SIGN_SIZE,
               IEEE754Constants.DOUBLE_EXPONENT_INDEX,
               IEEE754Constants.DOUBLE_EXPONENT_SIZE,
               IEEE754Constants.DOUBLE_FRACTION_INDEX,
               IEEE754Constants.DOUBLE_FRACTION_SIZE);
  }

  /**
   * Constructor. Reconstitute a float value.
   * 
   * @param sign
   *        the sign bit value, 0 or 1
   * @param biasedExponent
   *        the biased exponent value, 0..255
   * @param fraction
   *        the fraction bits
   * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
   */
  public IEEE754 (final int sign, final int biasedExponent, final FloatFraction fraction) throws IEEE754Exception
  {
    // Check the sign.
    if ((sign != 0) && (sign != 1))
    {
      throw new IEEE754Exception ("Invalid sign bit.");
    }

    validateFloatBiasedExponent (biasedExponent);

    // Consolidate the parts. First the sign ...
    int intBits = sign;

    // ... then the biased exponent value ...
    intBits <<= IEEE754Constants.FLOAT_EXPONENT_SIZE;
    intBits += biasedExponent;

    // ... and finally the fraction value.
    intBits <<= IEEE754Constants.FLOAT_FRACTION_SIZE;
    intBits += fraction.toInt ();

    // Convert to the float value.
    floatValue = Float.intBitsToFloat (intBits);
    isDouble = false;

    // Convert the value to a character array of '0' and '1'.
    final char bits[] = toCharBitArray (intBits, 32);

    decompose (bits,
               IEEE754Constants.FLOAT_EXPONENT_BIAS,
               IEEE754Constants.FLOAT_EXPONENT_RESERVED,
               IEEE754Constants.FLOAT_SIGN_INDEX,
               IEEE754Constants.FLOAT_SIGN_SIZE,
               IEEE754Constants.FLOAT_EXPONENT_INDEX,
               IEEE754Constants.FLOAT_EXPONENT_SIZE,
               IEEE754Constants.FLOAT_FRACTION_INDEX,
               IEEE754Constants.FLOAT_FRACTION_SIZE);
  }

  /**
   * Constructor. Reconstitute a double value.
   * 
   * @param sign
   *        the sign bit value, 0 or 1
   * @param biasedExponent
   *        the biased exponent value, 0..2047
   * @param fraction
   *        the fraction bits
   * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
   */
  public IEEE754 (final int sign, final int biasedExponent, final DoubleFraction fraction) throws IEEE754Exception
  {
    // Check the sign.
    if ((sign != 0) && (sign != 1))
    {
      throw new IEEE754Exception ("Invalid sign bit.");
    }

    validateDoubleBiasedExponent (biasedExponent);

    // Consolidate the parts. First the sign ...
    long longBits = sign;

    // ... then the biased exponent value ...
    longBits <<= IEEE754Constants.DOUBLE_EXPONENT_SIZE;
    longBits += biasedExponent;

    // ... and finally the fraction value.
    longBits <<= IEEE754Constants.DOUBLE_FRACTION_SIZE;
    longBits += fraction.toLong ();

    // Convert to the double value.
    doubleValue = Double.longBitsToDouble (longBits);
    isDouble = true;

    // Convert the value to a character array of '0' and '1'.
    final char bits[] = toCharBitArray (longBits, 64);

    decompose (bits,
               IEEE754Constants.DOUBLE_EXPONENT_BIAS,
               IEEE754Constants.DOUBLE_EXPONENT_RESERVED,
               IEEE754Constants.DOUBLE_SIGN_INDEX,
               IEEE754Constants.DOUBLE_SIGN_SIZE,
               IEEE754Constants.DOUBLE_EXPONENT_INDEX,
               IEEE754Constants.DOUBLE_EXPONENT_SIZE,
               IEEE754Constants.DOUBLE_FRACTION_INDEX,
               IEEE754Constants.DOUBLE_FRACTION_SIZE);
  }

  // -------------------------//
  // Methods to return parts //
  // -------------------------//

  /**
   * Return the float value.
   * 
   * @return the float value
   */
  public float floatValue ()
  {
    return floatValue;
  }

  /**
   * Return the double value.
   * 
   * @return the double value
   */
  public double doubleValue ()
  {
    return doubleValue;
  }

  /**
   * Return the biased value of the exponent.
   * 
   * @return the unbiased exponent value
   */
  public int biasedExponent ()
  {
    return biased;
  }

  /**
   * Return the unbiased value of the exponent.
   * 
   * @return the unbiased exponent value
   */
  public int unbiasedExponent ()
  {
    return isDenormalized ? -m_nBias + 1 : biased - m_nBias;
  }

  /**
   * Return the sign as a string of '0' and '1'.
   * 
   * @return the string
   */
  public String signBit ()
  {
    return signBit;
  }

  /**
   * Return the exponent as a string of '0' and '1'.
   * 
   * @return the string
   */
  public String exponentBits ()
  {
    return exponentBits;
  }

  /**
   * Return the fraction as a string of '0' and '1'.
   * 
   * @return the string
   */
  public String fractionBits ()
  {
    return fractionBits;
  }

  /**
   * Return the significand as a string of '0', '1' and '.'.
   * 
   * @return the string
   */
  public String significandBits ()
  {
    return impliedBit + "." + fractionBits;
  }

  /**
   * Return whether or not the value is zero.
   * 
   * @return true if zero, else false
   */
  public boolean isZero ()
  {
    return isZero;
  }

  /**
   * Return whether or not the value is a double.
   * 
   * @return true if a double, else false
   */
  public boolean isDouble ()
  {
    return isDouble;
  }

  /**
   * Return whether or not the value is denormalized.
   * 
   * @return true if denormalized, else false
   */
  public boolean isDenormalized ()
  {
    return isDenormalized;
  }

  /**
   * Return whether or not the exponent value is reserved.
   * 
   * @return true if reserved, else false
   */
  public boolean isExponentReserved ()
  {
    return isReserved;
  }

  // -----------------------//
  // Decomposition methods //
  // -----------------------//

  /**
   * Convert a long value into a character array of '0' and '1' that represents
   * the value in base 2.
   * 
   * @param value
   *        the long value
   * @param size
   *        the array size
   * @return the character array
   */
  private static char [] toCharBitArray (final long pvalue, final int size)
  {
    long value = pvalue;
    final char bits[] = new char [size];

    // Convert each bit from right to left.
    for (int i = size - 1; i >= 0; --i)
    {
      bits[i] = (value & 1) == 0 ? '0' : '1';
      value >>>= 1;
    }

    return bits;
  }

  /**
   * Decompose a floating-point value into its parts.
   * 
   * @param bits
   *        the character array of '0' and '1' that represents the value in base
   *        2
   * @param bias
   *        the exponent bias value
   * @param reserved
   *        the reserved exponent value (other than 0)
   * @param signIndex
   *        the index of the sign bit
   * @param signSize
   *        the size of the sign bit
   * @param exponentIndex
   *        the index of the exponent
   * @param exponentSize
   *        the size of the exponent
   * @param fractionIndex
   *        the index of the fraction
   * @param fractionSize
   *        the size of the fraction
   */
  private void decompose (final char bits[],
                          final int bias,
                          final int reserved,
                          final int signIndex,
                          final int signSize,
                          final int exponentIndex,
                          final int exponentSize,
                          final int fractionIndex,
                          final int fractionSize)
  {
    this.m_nBias = bias;

    // Extract the individual parts as strings of '0' and '1'.
    signBit = new String (bits, signIndex, signSize);
    exponentBits = new String (bits, exponentIndex, exponentSize);
    fractionBits = new String (bits, fractionIndex, fractionSize);

    try
    {
      biased = Integer.parseInt (exponentBits, 2);
      m_nFraction = Long.parseLong (fractionBits, 2);
    }
    catch (final NumberFormatException ex)
    {}

    isZero = (biased == 0) && (m_nFraction == 0);
    isDenormalized = (biased == 0) && (m_nFraction != 0);
    isReserved = (biased == reserved);

    impliedBit = isDenormalized || isZero || isReserved ? "0" : "1";
  }

  /**
   * Print the decomposed parts of the value.
   */
  public void print ()
  {
    System.out.println ("------------------------------");

    // Print the value.
    if (isDouble ())
    {
      System.out.println ("double value = " + doubleValue ());
    }
    else
    {
      System.out.println ("float value = " + floatValue ());
    }

    // Print the sign.
    System.out.print ("sign=" + signBit ());

    // Print the bit representation of the exponent and its
    // biased and unbiased values. Indicate whether the value
    // is denormalized, or whether the exponent is reserved.
    System.out.print (", exponent=" + exponentBits () + " (biased=" + biasedExponent ());

    if (isZero ())
    {
      System.out.println (", zero)");
    }
    else
      if (isExponentReserved ())
      {
        System.out.println (", reserved)");
      }
      else
        if (isDenormalized ())
        {
          System.out.println (", denormalized, use " + unbiasedExponent () + ")");
        }
        else
        {
          System.out.println (", normalized, unbiased=" + unbiasedExponent () + ")");
        }

    // Print the significand.
    System.out.println ("significand=" + significandBits ());
  }

  // --------------------------------//
  // Compute and validate exponents //
  // --------------------------------//

  /**
   * Compute the value of the float biased exponent given the unbiased value.
   * 
   * @param unbiased
   *        the unbiased exponent value
   * @return the biased exponent value
   */
  public static int toFloatBiasedExponent (final int unbiased)
  {
    return unbiased + IEEE754Constants.FLOAT_EXPONENT_BIAS;
  }

  /**
   * Compute the value of the float unbiased exponent given the biased value.
   * 
   * @param biased
   *        the biased exponent value
   * @return the unbiased exponent value
   */
  public static int toFloatUnbiasedExponent (final int biased)
  {
    return biased == 0 ? -IEEE754Constants.FLOAT_EXPONENT_BIAS + 1 : biased - IEEE754Constants.FLOAT_EXPONENT_BIAS;
  }

  /**
   * Compute the value of the double biased exponent given the unbiased value.
   * 
   * @param unbiased
   *        the unbiased exponent value
   * @return the biased exponent value
   */
  public static int toDoubleBiasedExponent (final int unbiased)
  {
    return unbiased + IEEE754Constants.DOUBLE_EXPONENT_BIAS;
  }

  /**
   * Compute the value of the double unbiased exponent given the biased value.
   * 
   * @param biased
   *        the biased exponent value
   * @return the unbiased exponent value
   */
  public static int toDoubleUnbiasedExponent (final int biased)
  {
    return biased == 0 ? -IEEE754Constants.DOUBLE_EXPONENT_BIAS + 1 : biased - IEEE754Constants.DOUBLE_EXPONENT_BIAS;
  }

  /**
   * Validate the value of the float biased exponent value.
   * 
   * @param biased
   *        the biased exponent value
   * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
   */
  public static void validateFloatBiasedExponent (final int biased) throws IEEE754Exception
  {
    if ((biased < 0) || (biased > IEEE754Constants.FLOAT_EXPONENT_RESERVED))
    {
      throw new IEEE754Exception ("The biased exponent value should be " +
                                  "0 through " +
                                  IEEE754Constants.FLOAT_EXPONENT_RESERVED +
                                  ".");
    }
  }

  /**
   * Validate the value of the float unbiased exponent value.
   * 
   * @param unbiased
   *        the unbiased exponent value
   * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
   */
  public static void validateFloatUnbiasedExponent (final int unbiased) throws IEEE754Exception
  {
    if ((unbiased < -IEEE754Constants.FLOAT_EXPONENT_BIAS + 1) || (unbiased > IEEE754Constants.FLOAT_EXPONENT_BIAS))
    {
      throw new IEEE754Exception ("The unbiased exponent value should be " +
                                  -(IEEE754Constants.FLOAT_EXPONENT_BIAS - 1) +
                                  " through " +
                                  IEEE754Constants.FLOAT_EXPONENT_BIAS +
                                  ".");
    }
  }

  /**
   * Validate the value of the double biased exponent value.
   * 
   * @param biased
   *        the biased exponent value
   * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
   */
  public static void validateDoubleBiasedExponent (final int biased) throws IEEE754Exception
  {
    if ((biased < 0) || (biased > IEEE754Constants.DOUBLE_EXPONENT_RESERVED))
    {
      throw new IEEE754Exception ("The biased exponent value should be " +
                                  "0 through " +
                                  IEEE754Constants.DOUBLE_EXPONENT_RESERVED +
                                  ".");
    }
  }

  /**
   * Validate the value of the double unbiased exponent value.
   * 
   * @param unbiased
   *        the unbiased exponent value
   * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
   */
  public static void validateDoubleUnbiasedExponent (final int unbiased) throws IEEE754Exception
  {
    if ((unbiased < -IEEE754Constants.DOUBLE_EXPONENT_BIAS + 1) || (unbiased > IEEE754Constants.DOUBLE_EXPONENT_BIAS))
    {
      throw new IEEE754Exception ("The unbiased exponent value should be " +
                                  -(IEEE754Constants.DOUBLE_EXPONENT_BIAS - 1) +
                                  " through " +
                                  IEEE754Constants.DOUBLE_EXPONENT_BIAS +
                                  ".");
    }
  }

  // ------------------------------//
  // Nested decomposition classes //
  // ------------------------------//

  /**
   * IEEE 754 exception.
   */
  public static class IEEE754Exception extends java.lang.Exception
  {
    public IEEE754Exception (final String message)
    {
      super (message);
    }
  }

  /**
   * Abstract base class for the IEEE 754 part classes.
   */
  private static abstract class Part
  {
    /** the part buffer */
    private final StringBuffer part;

    /**
     * Constructor.
     * 
     * @param size
     *        the bit size of the part
     * @param bits
     *        the string of character bits '0' and '1'
     * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
     */
    private Part (final int size, final String bits) throws IEEE754Exception
    {
      if (size <= 0)
      {
        throw new IEEE754Exception ("Invalid part size: " + size);
      }

      final int length = bits.length ();
      part = new StringBuffer (size);

      // String length matches part size.
      if (length == size)
      {
        part.append (bits);
        validate ();
      }

      // String length < part size: Pad with '0'.
      else
        if (length < size)
        {
          part.append (bits);
          validate ();
          for (int i = length; i < size; ++i)
            part.append ('0');
        }

        // String length > part size: Truncate at the right end.
        else
        {
          part.append (bits.substring (0, size));
          validate ();
        }
    }

    /**
     * Convert the part to an integer value.
     * 
     * @return the integer value
     * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
     *         if the binary number format is invalid
     */
    protected int toInt () throws IEEE754Exception
    {
      try
      {
        return Integer.parseInt (part.toString (), 2);
      }
      catch (final NumberFormatException ex)
      {
        throw new IEEE754Exception ("Invalid binary number format: " + part.toString ());
      }
    }

    /**
     * Convert the part to an long value.
     * 
     * @return the long value
     * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
     *         if the binary number format is invalid
     */
    protected long toLong () throws IEEE754Exception
    {
      try
      {
        return Long.parseLong (part.toString (), 2);
      }
      catch (final NumberFormatException ex)
      {
        throw new IEEE754Exception ("Invalid binary number format: " + part.toString ());
      }
    }

    /**
     * Return the part as a string of characters '0' and '1'.
     */
    @Override
    public String toString ()
    {
      return part.toString ();
    }

    /**
     * Validate that the part consists only of '0' and '1'.
     * 
     * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
     */
    private void validate () throws IEEE754Exception
    {
      final int length = part.length ();

      for (int i = 0; i < length; ++i)
      {
        final char bit = part.charAt (i);
        if ((bit != '0') && (bit != '1'))
        {
          throw new IEEE754Exception ("Invalid fraction bit string.");
        }
      }
    }
  }

  /**
   * The IEEE 754 fraction part for a float.
   */
  public static class FloatFraction extends Part
  {
    /**
     * Constructor.
     * 
     * @param bits
     *        the string of character bits '0' and '1'
     * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
     */
    public FloatFraction (final String bits) throws IEEE754Exception
    {
      super (IEEE754Constants.FLOAT_FRACTION_SIZE, bits);
    }
  }

  /**
   * The IEEE 754 fraction part for a double.
   */
  public static class DoubleFraction extends Part
  {
    /**
     * Constructor.
     * 
     * @param bits
     *        the string of character bits '0' and '1'
     * @throws numbercruncher.mathutils.IEEE754.IEEE754Exception
     */
    public DoubleFraction (final String bits) throws IEEE754Exception
    {
      super (IEEE754Constants.DOUBLE_FRACTION_SIZE, bits);
    }
  }
}
