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
