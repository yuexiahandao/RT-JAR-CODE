package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSTypeDefinition extends XSObject
{
  public static final short COMPLEX_TYPE = 15;
  public static final short SIMPLE_TYPE = 16;

  public abstract short getTypeCategory();

  public abstract XSTypeDefinition getBaseType();

  public abstract boolean isFinal(short paramShort);

  public abstract short getFinal();

  public abstract boolean getAnonymous();

  public abstract boolean derivedFromType(XSTypeDefinition paramXSTypeDefinition, short paramShort);

  public abstract boolean derivedFrom(String paramString1, String paramString2, short paramShort);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
 * JD-Core Version:    0.6.2
 */