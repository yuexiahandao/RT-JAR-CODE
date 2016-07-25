package javax.xml.ws;

public abstract interface AsyncHandler<T>
{
  public abstract void handleResponse(Response<T> paramResponse);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.AsyncHandler
 * JD-Core Version:    0.6.2
 */