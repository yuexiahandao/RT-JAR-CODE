package sun.security.krb5.internal;

import java.util.BitSet;
import sun.security.krb5.EncryptionKey;

public class AuthContext
{
  public HostAddress remoteAddress;
  public int remotePort;
  public HostAddress localAddress;
  public int localPort;
  public EncryptionKey keyBlock;
  public EncryptionKey localSubkey;
  public EncryptionKey remoteSubkey;
  public BitSet authContextFlags;
  public int remoteSeqNumber;
  public int localSeqNumber;
  public Authenticator authenticator;
  public int reqCksumType;
  public int safeCksumType;
  public byte[] initializationVector;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.AuthContext
 * JD-Core Version:    0.6.2
 */