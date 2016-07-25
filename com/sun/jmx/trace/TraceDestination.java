package com.sun.jmx.trace;

import java.io.IOException;

@Deprecated
public abstract interface TraceDestination
{
  public abstract boolean isSelected(int paramInt1, int paramInt2);

  public abstract boolean send(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3);

  public abstract boolean send(int paramInt1, int paramInt2, String paramString1, String paramString2, Throwable paramThrowable);

  public abstract void reset()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.trace.TraceDestination
 * JD-Core Version:    0.6.2
 */