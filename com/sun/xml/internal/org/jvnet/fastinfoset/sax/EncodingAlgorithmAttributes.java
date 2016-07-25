package com.sun.xml.internal.org.jvnet.fastinfoset.sax;

import org.xml.sax.Attributes;

public abstract interface EncodingAlgorithmAttributes extends Attributes
{
  public abstract String getAlgorithmURI(int paramInt);

  public abstract int getAlgorithmIndex(int paramInt);

  public abstract Object getAlgorithmData(int paramInt);

  public abstract String getAlpababet(int paramInt);

  public abstract boolean getToIndex(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes
 * JD-Core Version:    0.6.2
 */