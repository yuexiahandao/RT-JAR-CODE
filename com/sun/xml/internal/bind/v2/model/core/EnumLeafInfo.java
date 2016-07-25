package com.sun.xml.internal.bind.v2.model.core;

public abstract interface EnumLeafInfo<T, C> extends LeafInfo<T, C>
{
  public abstract C getClazz();

  public abstract NonElement<T, C> getBaseType();

  public abstract Iterable<? extends EnumConstant> getConstants();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo
 * JD-Core Version:    0.6.2
 */