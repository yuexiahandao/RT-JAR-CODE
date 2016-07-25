package org.w3c.dom.xpath;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public abstract interface XPathResult
{
  public static final short ANY_TYPE = 0;
  public static final short NUMBER_TYPE = 1;
  public static final short STRING_TYPE = 2;
  public static final short BOOLEAN_TYPE = 3;
  public static final short UNORDERED_NODE_ITERATOR_TYPE = 4;
  public static final short ORDERED_NODE_ITERATOR_TYPE = 5;
  public static final short UNORDERED_NODE_SNAPSHOT_TYPE = 6;
  public static final short ORDERED_NODE_SNAPSHOT_TYPE = 7;
  public static final short ANY_UNORDERED_NODE_TYPE = 8;
  public static final short FIRST_ORDERED_NODE_TYPE = 9;

  public abstract short getResultType();

  public abstract double getNumberValue()
    throws XPathException;

  public abstract String getStringValue()
    throws XPathException;

  public abstract boolean getBooleanValue()
    throws XPathException;

  public abstract Node getSingleNodeValue()
    throws XPathException;

  public abstract boolean getInvalidIteratorState();

  public abstract int getSnapshotLength()
    throws XPathException;

  public abstract Node iterateNext()
    throws XPathException, DOMException;

  public abstract Node snapshotItem(int paramInt)
    throws XPathException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.xpath.XPathResult
 * JD-Core Version:    0.6.2
 */