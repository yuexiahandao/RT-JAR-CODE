package javax.net.ssl;

public abstract interface HostnameVerifier
{
  public abstract boolean verify(String paramString, SSLSession paramSSLSession);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.HostnameVerifier
 * JD-Core Version:    0.6.2
 */