package java.security.interfaces;

import java.math.BigInteger;
import java.security.PublicKey;

public abstract interface RSAPublicKey extends PublicKey, RSAKey
{
  public static final long serialVersionUID = -8727434096241101194L;

  public abstract BigInteger getPublicExponent();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.interfaces.RSAPublicKey
 * JD-Core Version:    0.6.2
 */