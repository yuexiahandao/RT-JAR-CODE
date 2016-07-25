package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSAnnotation extends XSObject
{
  public static final short W3C_DOM_ELEMENT = 1;
  public static final short SAX_CONTENTHANDLER = 2;
  public static final short W3C_DOM_DOCUMENT = 3;

  public abstract boolean writeAnnotation(Object paramObject, short paramShort);

  public abstract String getAnnotationString();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSAnnotation
 * JD-Core Version:    0.6.2
 */