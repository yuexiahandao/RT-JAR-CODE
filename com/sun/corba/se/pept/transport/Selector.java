package com.sun.corba.se.pept.transport;

public abstract interface Selector
{
  public abstract void setTimeout(long paramLong);

  public abstract long getTimeout();

  public abstract void registerInterestOps(EventHandler paramEventHandler);

  public abstract void registerForEvent(EventHandler paramEventHandler);

  public abstract void unregisterForEvent(EventHandler paramEventHandler);

  public abstract void close();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.pept.transport.Selector
 * JD-Core Version:    0.6.2
 */