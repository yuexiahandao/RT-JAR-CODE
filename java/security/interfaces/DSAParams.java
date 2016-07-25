package java.security.interfaces;

import java.math.BigInteger;

public abstract interface DSAParams
{
  public abstract BigInteger getP();

  public abstract BigInteger getQ();

  public abstract BigInteger getG();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.interfaces.DSAParams
 * JD-Core Version:    0.6.2
 */