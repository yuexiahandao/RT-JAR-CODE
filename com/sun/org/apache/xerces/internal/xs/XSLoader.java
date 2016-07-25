package com.sun.org.apache.xerces.internal.xs;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.ls.LSInput;

public abstract interface XSLoader
{
  public abstract DOMConfiguration getConfig();

  public abstract XSModel loadURIList(StringList paramStringList);

  public abstract XSModel loadInputList(LSInputList paramLSInputList);

  public abstract XSModel loadURI(String paramString);

  public abstract XSModel load(LSInput paramLSInput);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.XSLoader
 * JD-Core Version:    0.6.2
 */