package java.util.concurrent;

import java.util.Deque;
import java.util.Iterator;

public abstract interface BlockingDeque<E> extends BlockingQueue<E>, Deque<E>
{
  public abstract void addFirst(E paramE);

  public abstract void addLast(E paramE);

  public abstract boolean offerFirst(E paramE);

  public abstract boolean offerLast(E paramE);

  public abstract void putFirst(E paramE)
    throws InterruptedException;

  public abstract void putLast(E paramE)
    throws InterruptedException;

  public abstract boolean offerFirst(E paramE, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;

  public abstract boolean offerLast(E paramE, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;

  public abstract E takeFirst()
    throws InterruptedException;

  public abstract E takeLast()
    throws InterruptedException;

  public abstract E pollFirst(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;

  public abstract E pollLast(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;

  public abstract boolean removeFirstOccurrence(Object paramObject);

  public abstract boolean removeLastOccurrence(Object paramObject);

  public abstract boolean add(E paramE);

  public abstract boolean offer(E paramE);

  public abstract void put(E paramE)
    throws InterruptedException;

  public abstract boolean offer(E paramE, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;

  public abstract E remove();

  public abstract E poll();

  public abstract E take()
    throws InterruptedException;

  public abstract E poll(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;

  public abstract E element();

  public abstract E peek();

  public abstract boolean remove(Object paramObject);

  public abstract boolean contains(Object paramObject);

  public abstract int size();

  public abstract Iterator<E> iterator();

  public abstract void push(E paramE);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.BlockingDeque
 * JD-Core Version:    0.6.2
 */