package java.util.concurrent;

public abstract interface RunnableFuture<V> extends Runnable, Future<V>
{
  public abstract void run();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.RunnableFuture
 * JD-Core Version:    0.6.2
 */