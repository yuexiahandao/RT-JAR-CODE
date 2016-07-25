package com.sun.xml.internal.bind;

import javax.xml.bind.Marshaller;

public abstract interface CycleRecoverable
{
  public abstract Object onCycleDetected(Context paramContext);

  public static abstract interface Context
  {
    public abstract Marshaller getMarshaller();
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.CycleRecoverable
 * JD-Core Version:    0.6.2
 */