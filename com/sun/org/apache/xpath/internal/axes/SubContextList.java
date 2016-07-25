package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xpath.internal.XPathContext;

public abstract interface SubContextList
{
  public abstract int getLastPos(XPathContext paramXPathContext);

  public abstract int getProximityPosition(XPathContext paramXPathContext);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.SubContextList
 * JD-Core Version:    0.6.2
 */