package java.net;

import java.util.List;

public abstract interface CookieStore
{
  public abstract void add(URI paramURI, HttpCookie paramHttpCookie);

  public abstract List<HttpCookie> get(URI paramURI);

  public abstract List<HttpCookie> getCookies();

  public abstract List<URI> getURIs();

  public abstract boolean remove(URI paramURI, HttpCookie paramHttpCookie);

  public abstract boolean removeAll();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.CookieStore
 * JD-Core Version:    0.6.2
 */