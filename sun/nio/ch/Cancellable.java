package sun.nio.ch;

abstract interface Cancellable
{
  public abstract void onCancel(PendingFuture<?, ?> paramPendingFuture);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.Cancellable
 * JD-Core Version:    0.6.2
 */