package java.security.interfaces;

import java.math.BigInteger;
import java.security.PrivateKey;

public abstract interface DSAPrivateKey extends DSAKey, PrivateKey
{
  public static final long serialVersionUID = 7776497482533790279L;

  public abstract BigInteger getX();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.interfaces.DSAPrivateKey
 * JD-Core Version:    0.6.2
 */