package java.util.concurrent;

public abstract interface CompletionService<V>
{
  public abstract Future<V> submit(Callable<V> paramCallable);

  public abstract Future<V> submit(Runnable paramRunnable, V paramV);

  public abstract Future<V> take()
    throws InterruptedException;

  public abstract Future<V> poll();

  public abstract Future<V> poll(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.CompletionService
 * JD-Core Version:    0.6.2
 */