package java.security.interfaces;

import java.math.BigInteger;

public abstract interface RSAPrivateCrtKey extends RSAPrivateKey
{
  public static final long serialVersionUID = -5682214253527700368L;

  public abstract BigInteger getPublicExponent();

  public abstract BigInteger getPrimeP();

  public abstract BigInteger getPrimeQ();

  public abstract BigInteger getPrimeExponentP();

  public abstract BigInteger getPrimeExponentQ();

  public abstract BigInteger getCrtCoefficient();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.interfaces.RSAPrivateCrtKey
 * JD-Core Version:    0.6.2
 */