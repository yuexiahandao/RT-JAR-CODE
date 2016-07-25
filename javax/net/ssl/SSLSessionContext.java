package javax.net.ssl;

import java.util.Enumeration;

public abstract interface SSLSessionContext
{
  public abstract SSLSession getSession(byte[] paramArrayOfByte);

  public abstract Enumeration<byte[]> getIds();

  public abstract void setSessionTimeout(int paramInt)
    throws IllegalArgumentException;

  public abstract int getSessionTimeout();

  public abstract void setSessionCacheSize(int paramInt)
    throws IllegalArgumentException;

  public abstract int getSessionCacheSize();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.SSLSessionContext
 * JD-Core Version:    0.6.2
 */