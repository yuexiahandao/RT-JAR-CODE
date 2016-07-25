package com.sun.org.apache.xerces.internal.xs;

import java.util.List;

public abstract interface StringList extends List
{
  public abstract int getLength();

  public abstract boolean contains(String paramString);

  public abstract String item(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.StringList
 * JD-Core Version:    0.6.2
 */