package java.nio.channels;

public abstract interface CompletionHandler<V, A>
{
  public abstract void completed(V paramV, A paramA);

  public abstract void failed(Throwable paramThrowable, A paramA);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.CompletionHandler
 * JD-Core Version:    0.6.2
 */