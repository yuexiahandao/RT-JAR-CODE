package com.sun.org.apache.xalan.internal.xsltc.compiler;

import org.xml.sax.InputSource;

public abstract interface SourceLoader
{
  public abstract InputSource loadSource(String paramString1, String paramString2, XSLTC paramXSLTC);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.SourceLoader
 * JD-Core Version:    0.6.2
 */