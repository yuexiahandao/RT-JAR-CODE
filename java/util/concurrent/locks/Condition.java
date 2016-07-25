package java.util.concurrent.locks;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract interface Condition
{
  public abstract void await()
    throws InterruptedException;

  public abstract void awaitUninterruptibly();

  public abstract long awaitNanos(long paramLong)
    throws InterruptedException;

  public abstract boolean await(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;

  public abstract boolean awaitUntil(Date paramDate)
    throws InterruptedException;

  public abstract void signal();

  public abstract void signalAll();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.locks.Condition
 * JD-Core Version:    0.6.2
 */