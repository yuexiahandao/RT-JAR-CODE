package org.w3c.dom.xpath;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public abstract interface XPathExpression
{
  public abstract Object evaluate(Node paramNode, short paramShort, Object paramObject)
    throws XPathException, DOMException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.xpath.XPathExpression
 * JD-Core Version:    0.6.2
 */