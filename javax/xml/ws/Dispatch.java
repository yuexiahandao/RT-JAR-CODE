package javax.xml.ws;

import java.util.concurrent.Future;

public abstract interface Dispatch<T> extends BindingProvider
{
  public abstract T invoke(T paramT);

  public abstract Response<T> invokeAsync(T paramT);

  public abstract Future<?> invokeAsync(T paramT, AsyncHandler<T> paramAsyncHandler);

  public abstract void invokeOneWay(T paramT);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.Dispatch
 * JD-Core Version:    0.6.2
 */