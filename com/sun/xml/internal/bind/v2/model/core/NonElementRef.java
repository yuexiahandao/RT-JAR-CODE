package com.sun.xml.internal.bind.v2.model.core;

public abstract interface NonElementRef<T, C>
{
  public abstract NonElement<T, C> getTarget();

  public abstract PropertyInfo<T, C> getSource();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.NonElementRef
 * JD-Core Version:    0.6.2
 */