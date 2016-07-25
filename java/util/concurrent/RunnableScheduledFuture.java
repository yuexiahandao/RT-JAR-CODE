package java.util.concurrent;

public abstract interface RunnableScheduledFuture<V> extends RunnableFuture<V>, ScheduledFuture<V>
{
  public abstract boolean isPeriodic();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.RunnableScheduledFuture
 * JD-Core Version:    0.6.2
 */