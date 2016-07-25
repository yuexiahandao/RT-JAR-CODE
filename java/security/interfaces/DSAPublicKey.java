package java.security.interfaces;

import java.math.BigInteger;
import java.security.PublicKey;

public abstract interface DSAPublicKey extends DSAKey, PublicKey
{
  public static final long serialVersionUID = 1234526332779022332L;

  public abstract BigInteger getY();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.interfaces.DSAPublicKey
 * JD-Core Version:    0.6.2
 */