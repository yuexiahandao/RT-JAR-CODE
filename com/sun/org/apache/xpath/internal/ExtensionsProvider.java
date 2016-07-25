package com.sun.org.apache.xpath.internal;

import com.sun.org.apache.xpath.internal.functions.FuncExtFunction;
import java.util.Vector;
import javax.xml.transform.TransformerException;

public abstract interface ExtensionsProvider
{
  public abstract boolean functionAvailable(String paramString1, String paramString2)
    throws TransformerException;

  public abstract boolean elementAvailable(String paramString1, String paramString2)
    throws TransformerException;

  public abstract Object extFunction(String paramString1, String paramString2, Vector paramVector, Object paramObject)
    throws TransformerException;

  public abstract Object extFunction(FuncExtFunction paramFuncExtFunction, Vector paramVector)
    throws TransformerException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.ExtensionsProvider
 * JD-Core Version:    0.6.2
 */