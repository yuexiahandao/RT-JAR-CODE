package java.security.interfaces;

import java.math.BigInteger;
import java.security.PrivateKey;

public abstract interface ECPrivateKey extends PrivateKey, ECKey
{
  public static final long serialVersionUID = -7896394956925609184L;

  public abstract BigInteger getS();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.interfaces.ECPrivateKey
 * JD-Core Version:    0.6.2
 */