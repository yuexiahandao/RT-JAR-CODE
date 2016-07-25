package com.sun.org.apache.xerces.internal.xs.datatypes;

import java.util.List;

public abstract interface ObjectList extends List
{
  public abstract int getLength();

  public abstract boolean contains(Object paramObject);

  public abstract Object item(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList
 * JD-Core Version:    0.6.2
 */