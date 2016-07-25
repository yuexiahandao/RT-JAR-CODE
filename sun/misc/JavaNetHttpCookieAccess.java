package sun.misc;

import java.net.HttpCookie;
import java.util.List;

public abstract interface JavaNetHttpCookieAccess
{
  public abstract List<HttpCookie> parse(String paramString);

  public abstract String header(HttpCookie paramHttpCookie);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.JavaNetHttpCookieAccess
 * JD-Core Version:    0.6.2
 */