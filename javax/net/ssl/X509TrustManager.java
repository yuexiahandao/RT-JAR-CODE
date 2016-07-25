package javax.net.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public abstract interface X509TrustManager extends TrustManager
{
  public abstract void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException;

  public abstract void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException;

  public abstract X509Certificate[] getAcceptedIssuers();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.X509TrustManager
 * JD-Core Version:    0.6.2
 */