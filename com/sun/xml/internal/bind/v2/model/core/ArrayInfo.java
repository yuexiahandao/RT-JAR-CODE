package com.sun.xml.internal.bind.v2.model.core;

public abstract interface ArrayInfo<T, C> extends NonElement<T, C>
{
  public abstract NonElement<T, C> getItemType();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.ArrayInfo
 * JD-Core Version:    0.6.2
 */