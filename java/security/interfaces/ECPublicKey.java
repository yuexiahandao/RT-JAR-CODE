package java.security.interfaces;

import java.security.PublicKey;
import java.security.spec.ECPoint;

public abstract interface ECPublicKey extends PublicKey, ECKey
{
  public static final long serialVersionUID = -3314988629879632826L;

  public abstract ECPoint getW();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.interfaces.ECPublicKey
 * JD-Core Version:    0.6.2
 */