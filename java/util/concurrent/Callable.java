package java.util.concurrent;

public abstract interface Callable<V>
{
  public abstract V call()
    throws Exception;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.Callable
 * JD-Core Version:    0.6.2
 */