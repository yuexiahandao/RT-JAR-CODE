package com.sun.org.apache.xerces.internal.xs.datatypes;

import com.sun.org.apache.xerces.internal.xs.XSException;
import java.util.List;

public abstract interface ByteList extends List
{
  public abstract int getLength();

  public abstract boolean contains(byte paramByte);

  public abstract byte item(int paramInt)
    throws XSException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xs.datatypes.ByteList
 * JD-Core Version:    0.6.2
 */