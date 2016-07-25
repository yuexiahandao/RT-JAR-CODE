package com.sun.tracing;

import java.lang.reflect.Method;

public abstract interface Provider
{
  public abstract Probe getProbe(Method paramMethod);

  public abstract void dispose();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.tracing.Provider
 * JD-Core Version:    0.6.2
 */