package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public abstract interface CSSPrimitiveValue extends CSSValue
{
  public static final short CSS_UNKNOWN = 0;
  public static final short CSS_NUMBER = 1;
  public static final short CSS_PERCENTAGE = 2;
  public static final short CSS_EMS = 3;
  public static final short CSS_EXS = 4;
  public static final short CSS_PX = 5;
  public static final short CSS_CM = 6;
  public static final short CSS_MM = 7;
  public static final short CSS_IN = 8;
  public static final short CSS_PT = 9;
  public static final short CSS_PC = 10;
  public static final short CSS_DEG = 11;
  public static final short CSS_RAD = 12;
  public static final short CSS_GRAD = 13;
  public static final short CSS_MS = 14;
  public static final short CSS_S = 15;
  public static final short CSS_HZ = 16;
  public static final short CSS_KHZ = 17;
  public static final short CSS_DIMENSION = 18;
  public static final short CSS_STRING = 19;
  public static final short CSS_URI = 20;
  public static final short CSS_IDENT = 21;
  public static final short CSS_ATTR = 22;
  public static final short CSS_COUNTER = 23;
  public static final short CSS_RECT = 24;
  public static final short CSS_RGBCOLOR = 25;

  public abstract short getPrimitiveType();

  public abstract void setFloatValue(short paramShort, float paramFloat)
    throws DOMException;

  public abstract float getFloatValue(short paramShort)
    throws DOMException;

  public abstract void setStringValue(short paramShort, String paramString)
    throws DOMException;

  public abstract String getStringValue()
    throws DOMException;

  public abstract Counter getCounterValue()
    throws DOMException;

  public abstract Rect getRectValue()
    throws DOMException;

  public abstract RGBColor getRGBColorValue()
    throws DOMException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.css.CSSPrimitiveValue
 * JD-Core Version:    0.6.2
 */