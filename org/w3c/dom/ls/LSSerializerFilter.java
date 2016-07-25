package org.w3c.dom.ls;

import org.w3c.dom.traversal.NodeFilter;

public abstract interface LSSerializerFilter extends NodeFilter
{
  public abstract int getWhatToShow();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.w3c.dom.ls.LSSerializerFilter
 * JD-Core Version:    0.6.2
 */