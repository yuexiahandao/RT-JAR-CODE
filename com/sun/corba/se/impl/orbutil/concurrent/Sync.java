package com.sun.corba.se.impl.orbutil.concurrent;

public abstract interface Sync
{
  public static final long ONE_SECOND = 1000L;
  public static final long ONE_MINUTE = 60000L;
  public static final long ONE_HOUR = 3600000L;
  public static final long ONE_DAY = 86400000L;
  public static final long ONE_WEEK = 604800000L;
  public static final long ONE_YEAR = 31556952000L;
  public static final long ONE_CENTURY = 3155695200000L;

  public abstract void acquire()
    throws InterruptedException;

  public abstract boolean attempt(long paramLong)
    throws InterruptedException;

  public abstract void release();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.concurrent.Sync
 * JD-Core Version:    0.6.2
 */