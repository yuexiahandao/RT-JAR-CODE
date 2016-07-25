package java.util.concurrent.locks;

import java.util.concurrent.TimeUnit;

public abstract interface Lock
{
  public abstract void lock();

  public abstract void lockInterruptibly()
    throws InterruptedException;

  public abstract boolean tryLock();

  public abstract boolean tryLock(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;

  public abstract void unlock();

  public abstract Condition newCondition();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.locks.Lock
 * JD-Core Version:    0.6.2
 */