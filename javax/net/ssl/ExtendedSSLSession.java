package javax.net.ssl;

public abstract class ExtendedSSLSession
  implements SSLSession
{
  public abstract String[] getLocalSupportedSignatureAlgorithms();

  public abstract String[] getPeerSupportedSignatureAlgorithms();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.ExtendedSSLSession
 * JD-Core Version:    0.6.2
 */