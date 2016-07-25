package java.util.concurrent;

import java.util.Collection;
import java.util.Queue;

public abstract interface BlockingQueue<E> extends Queue<E>
{
  public abstract boolean add(E paramE);

  public abstract boolean offer(E paramE);

  public abstract void put(E paramE)
    throws InterruptedException;

  public abstract boolean offer(E paramE, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;

  public abstract E take()
    throws InterruptedException;

  public abstract E poll(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;

  public abstract int remainingCapacity();

  public abstract boolean remove(Object paramObject);

  public abstract boolean contains(Object paramObject);

  public abstract int drainTo(Collection<? super E> paramCollection);

  public abstract int drainTo(Collection<? super E> paramCollection, int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.BlockingQueue
 * JD-Core Version:    0.6.2
 */