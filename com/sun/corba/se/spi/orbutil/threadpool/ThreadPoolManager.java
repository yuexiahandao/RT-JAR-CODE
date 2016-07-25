package com.sun.corba.se.spi.orbutil.threadpool;

import java.io.Closeable;

public abstract interface ThreadPoolManager extends Closeable
{
  public abstract ThreadPool getThreadPool(String paramString)
    throws NoSuchThreadPoolException;

  public abstract ThreadPool getThreadPool(int paramInt)
    throws NoSuchThreadPoolException;

  public abstract int getThreadPoolNumericId(String paramString);

  public abstract String getThreadPoolStringId(int paramInt);

  public abstract ThreadPool getDefaultThreadPool();

  public abstract ThreadPoolChooser getThreadPoolChooser(String paramString);

  public abstract ThreadPoolChooser getThreadPoolChooser(int paramInt);

  public abstract void setThreadPoolChooser(String paramString, ThreadPoolChooser paramThreadPoolChooser);

  public abstract int getThreadPoolChooserNumericId(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager
 * JD-Core Version:    0.6.2
 */