package com.sun.org.apache.xerces.internal.xs;

public abstract interface XSObject
{
  public abstract short getType();

  public abstract String getName();

  public abstract String getNamespace();

  public abstract XSNamespaceItem getNamespaceItem();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSObject
 * JD-Core Version:    0.6.2
 */