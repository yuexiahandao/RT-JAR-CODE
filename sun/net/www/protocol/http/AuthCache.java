package sun.net.www.protocol.http;

public abstract interface AuthCache
{
  public abstract void put(String paramString, AuthCacheValue paramAuthCacheValue);

  public abstract AuthCacheValue get(String paramString1, String paramString2);

  public abstract void remove(String paramString, AuthCacheValue paramAuthCacheValue);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.AuthCache
 * JD-Core Version:    0.6.2
 */