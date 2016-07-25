package com.sun.org.apache.xpath.internal;

import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;

public abstract interface WhitespaceStrippingElementMatcher
{
  public abstract boolean shouldStripWhiteSpace(XPathContext paramXPathContext, Element paramElement)
    throws TransformerException;

  public abstract boolean canStripWhiteSpace();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.WhitespaceStrippingElementMatcher
 * JD-Core Version:    0.6.2
 */