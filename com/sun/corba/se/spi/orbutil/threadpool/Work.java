package com.sun.corba.se.spi.orbutil.threadpool;

public abstract interface Work
{
  public abstract void doWork();

  public abstract void setEnqueueTime(long paramLong);

  public abstract long getEnqueueTime();

  public abstract String getName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.threadpool.Work
 * JD-Core Version:    0.6.2
 */