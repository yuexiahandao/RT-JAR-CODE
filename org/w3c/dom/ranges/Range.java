package org.w3c.dom.ranges;

import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

public abstract interface Range
{
  public static final short START_TO_START = 0;
  public static final short START_TO_END = 1;
  public static final short END_TO_END = 2;
  public static final short END_TO_START = 3;

  public abstract Node getStartContainer()
    throws DOMException;

  public abstract int getStartOffset()
    throws DOMException;

  public abstract Node getEndContainer()
    throws DOMException;

  public abstract int getEndOffset()
    throws DOMException;

  public abstract boolean getCollapsed()
    throws DOMException;

  public abstract Node getCommonAncestorContainer()
    throws DOMException;

  public abstract void setStart(Node paramNode, int paramInt)
    throws RangeException, DOMException;

  public abstract void setEnd(Node paramNode, int paramInt)
    throws RangeException, DOMException;

  public abstract void setStartBefore(Node paramNode)
    throws RangeException, DOMException;

  public abstract void setStartAfter(Node paramNode)
    throws RangeException, DOMException;

  public abstract void setEndBefore(Node paramNode)
    throws RangeException, DOMException;

  public abstract void setEndAfter(Node paramNode)
    throws RangeException, DOMException;

  public abstract void collapse(boolean paramBoolean)
    throws DOMException;

  public abstract void selectNode(Node paramNode)
    throws RangeException, DOMException;

  public abstract void selectNodeContents(Node paramNode)
    throws RangeException, DOMException;

  public abstract short compareBoundaryPoints(short paramShort, Range paramRange)
    throws DOMException;

  public abstract void deleteContents()
    throws DOMException;

  public abstract DocumentFragment extractContents()
    throws DOMException;

  public abstract DocumentFragment cloneContents()
    throws DOMException;

  public abstract void insertNode(Node paramNode)
    throws DOMException, RangeException;

  public abstract void surroundContents(Node paramNode)
    throws DOMException, RangeException;

  public abstract Range cloneRange()
    throws DOMException;

  public abstract String toString()
    throws DOMException;

  public abstract void detach()
    throws DOMException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.ranges.Range
 * JD-Core Version:    0.6.2
 */