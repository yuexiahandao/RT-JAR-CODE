package com.sun.xml.internal.ws.api.pipe;

public abstract interface FiberContextSwitchInterceptor
{
  public abstract <R, P> R execute(Fiber paramFiber, P paramP, Work<R, P> paramWork);

  public static abstract interface Work<R, P>
  {
    public abstract R execute(P paramP);
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor
 * JD-Core Version:    0.6.2
 */