package com.sun.org.apache.xalan.internal.xsltc.runtime.output;

abstract interface OutputBuffer
{
  public abstract String close();

  public abstract OutputBuffer append(char paramChar);

  public abstract OutputBuffer append(String paramString);

  public abstract OutputBuffer append(char[] paramArrayOfChar, int paramInt1, int paramInt2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.runtime.output.OutputBuffer
 * JD-Core Version:    0.6.2
 */