package java.net;

import java.security.Principal;
import java.security.cert.Certificate;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;

public abstract class SecureCacheResponse extends CacheResponse
{
  public abstract String getCipherSuite();

  public abstract List<Certificate> getLocalCertificateChain();

  public abstract List<Certificate> getServerCertificateChain()
    throws SSLPeerUnverifiedException;

  public abstract Principal getPeerPrincipal()
    throws SSLPeerUnverifiedException;

  public abstract Principal getLocalPrincipal();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.SecureCacheResponse
 * JD-Core Version:    0.6.2
 */