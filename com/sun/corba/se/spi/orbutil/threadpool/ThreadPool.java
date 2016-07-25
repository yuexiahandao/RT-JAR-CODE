package com.sun.corba.se.spi.orbutil.threadpool;

import java.io.Closeable;

public abstract interface ThreadPool extends Closeable
{
  public abstract WorkQueue getAnyWorkQueue();

  public abstract WorkQueue getWorkQueue(int paramInt)
    throws NoSuchWorkQueueException;

  public abstract int numberOfWorkQueues();

  public abstract int minimumNumberOfThreads();

  public abstract int maximumNumberOfThreads();

  public abstract long idleTimeoutForThreads();

  public abstract int currentNumberOfThreads();

  public abstract int numberOfAvailableThreads();

  public abstract int numberOfBusyThreads();

  public abstract long currentProcessedCount();

  public abstract long averageWorkCompletionTime();

  public abstract String getName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.threadpool.ThreadPool
 * JD-Core Version:    0.6.2
 */