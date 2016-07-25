package javax.xml.ws.spi.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class HttpExchange
{
  public static final String REQUEST_CIPHER_SUITE = "javax.xml.ws.spi.http.request.cipher.suite";
  public static final String REQUEST_KEY_SIZE = "javax.xml.ws.spi.http.request.key.size";
  public static final String REQUEST_X509CERTIFICATE = "javax.xml.ws.spi.http.request.cert.X509Certificate";

  public abstract Map<String, List<String>> getRequestHeaders();

  public abstract String getRequestHeader(String paramString);

  public abstract Map<String, List<String>> getResponseHeaders();

  public abstract void addResponseHeader(String paramString1, String paramString2);

  public abstract String getRequestURI();

  public abstract String getContextPath();

  public abstract String getRequestMethod();

  public abstract HttpContext getHttpContext();

  public abstract void close()
    throws IOException;

  public abstract InputStream getRequestBody()
    throws IOException;

  public abstract OutputStream getResponseBody()
    throws IOException;

  public abstract void setStatus(int paramInt);

  public abstract InetSocketAddress getRemoteAddress();

  public abstract InetSocketAddress getLocalAddress();

  public abstract String getProtocol();

  public abstract String getScheme();

  public abstract String getPathInfo();

  public abstract String getQueryString();

  public abstract Object getAttribute(String paramString);

  public abstract Set<String> getAttributeNames();

  public abstract Principal getUserPrincipal();

  public abstract boolean isUserInRole(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.spi.http.HttpExchange
 * JD-Core Version:    0.6.2
 */