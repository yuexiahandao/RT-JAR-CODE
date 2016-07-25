package com.sun.tracing;

public abstract interface Probe
{
  public abstract boolean isEnabled();

  public abstract void trigger(Object[] paramArrayOfObject);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.tracing.Probe
 * JD-Core Version:    0.6.2
 */