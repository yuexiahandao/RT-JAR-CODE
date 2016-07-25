package sun.misc;

public class FloatConsts
{
  public static final float POSITIVE_INFINITY = (1.0F / 1.0F);
  public static final float NEGATIVE_INFINITY = (1.0F / -1.0F);
  public static final float NaN = (0.0F / 0.0F);
  public static final float MAX_VALUE = 3.4028235E+38F;
  public static final float MIN_VALUE = 1.4E-45F;
  public static final float MIN_NORMAL = 1.175494E-038F;
  public static final int SIGNIFICAND_WIDTH = 24;
  public static final int MAX_EXPONENT = 127;
  public static final int MIN_EXPONENT = -126;
  public static final int MIN_SUB_EXPONENT = -149;
  public static final int EXP_BIAS = 127;
  public static final int SIGN_BIT_MASK = -2147483648;
  public static final int EXP_BIT_MASK = 2139095040;
  public static final int SIGNIF_BIT_MASK = 8388607;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.FloatConsts
 * JD-Core Version:    0.6.2
 */