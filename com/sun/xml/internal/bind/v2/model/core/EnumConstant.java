package com.sun.xml.internal.bind.v2.model.core;

public abstract interface EnumConstant<T, C>
{
  public abstract EnumLeafInfo<T, C> getEnclosingClass();

  public abstract String getLexicalValue();

  public abstract String getName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.EnumConstant
 * JD-Core Version:    0.6.2
 */