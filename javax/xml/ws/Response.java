package javax.xml.ws;

import java.util.Map;
import java.util.concurrent.Future;

public abstract interface Response<T> extends Future<T>
{
  public abstract Map<String, Object> getContext();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.Response
 * JD-Core Version:    0.6.2
 */