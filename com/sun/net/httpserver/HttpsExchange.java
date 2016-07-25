package com.sun.net.httpserver;

import javax.net.ssl.SSLSession;

public abstract class HttpsExchange extends HttpExchange
{
  public abstract SSLSession getSSLSession();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.HttpsExchange
 * JD-Core Version:    0.6.2
 */