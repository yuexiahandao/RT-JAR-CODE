package com.sun.corba.se.spi.orbutil.threadpool;

public abstract interface WorkQueue
{
  public abstract void addWork(Work paramWork);

  public abstract String getName();

  public abstract long totalWorkItemsAdded();

  public abstract int workItemsInQueue();

  public abstract long averageTimeInQueue();

  public abstract void setThreadPool(ThreadPool paramThreadPool);

  public abstract ThreadPool getThreadPool();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orbutil.threadpool.WorkQueue
 * JD-Core Version:    0.6.2
 */