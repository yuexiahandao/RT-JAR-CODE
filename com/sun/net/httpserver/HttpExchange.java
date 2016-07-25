package com.sun.net.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

public abstract class HttpExchange
{
  public abstract Headers getRequestHeaders();

  public abstract Headers getResponseHeaders();

  public abstract URI getRequestURI();

  public abstract String getRequestMethod();

  public abstract HttpContext getHttpContext();

  public abstract void close();

  public abstract InputStream getRequestBody();

  public abstract OutputStream getResponseBody();

  public abstract void sendResponseHeaders(int paramInt, long paramLong)
    throws IOException;

  public abstract InetSocketAddress getRemoteAddress();

  public abstract int getResponseCode();

  public abstract InetSocketAddress getLocalAddress();

  public abstract String getProtocol();

  public abstract Object getAttribute(String paramString);

  public abstract void setAttribute(String paramString, Object paramObject);

  public abstract void setStreams(InputStream paramInputStream, OutputStream paramOutputStream);

  public abstract HttpPrincipal getPrincipal();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.HttpExchange
 * JD-Core Version:    0.6.2
 */