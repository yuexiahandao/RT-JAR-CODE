package java.net;

import java.io.IOException;
import java.io.OutputStream;

public abstract class CacheRequest
{
  public abstract OutputStream getBody()
    throws IOException;

  public abstract void abort();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.CacheRequest
 * JD-Core Version:    0.6.2
 */