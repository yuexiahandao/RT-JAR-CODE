package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSImplementation
{
  public abstract StringList getRecognizedVersions();

  public abstract XSLoader createXSLoader(StringList paramStringList)
    throws XSException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSImplementation
 * JD-Core Version:    0.6.2
 */