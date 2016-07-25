package com.sun.org.apache.xerces.internal.xs;

public abstract interface ElementPSVI extends ItemPSVI
{
  public abstract XSElementDeclaration getElementDeclaration();

  public abstract XSNotationDeclaration getNotation();

  public abstract boolean getNil();

  public abstract XSModel getSchemaInformation();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.ElementPSVI
 * JD-Core Version:    0.6.2
 */