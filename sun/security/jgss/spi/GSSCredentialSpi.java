package sun.security.jgss.spi;

import java.security.Provider;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

public abstract interface GSSCredentialSpi
{
  public abstract Provider getProvider();

  public abstract void dispose()
    throws GSSException;

  public abstract GSSNameSpi getName()
    throws GSSException;

  public abstract int getInitLifetime()
    throws GSSException;

  public abstract int getAcceptLifetime()
    throws GSSException;

  public abstract boolean isInitiatorCredential()
    throws GSSException;

  public abstract boolean isAcceptorCredential()
    throws GSSException;

  public abstract Oid getMechanism();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.spi.GSSCredentialSpi
 * JD-Core Version:    0.6.2
 */