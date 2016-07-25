package java.security;

import java.io.Serializable;

public abstract class SecureRandomSpi
  implements Serializable
{
  private static final long serialVersionUID = -2991854161009191830L;

  protected abstract void engineSetSeed(byte[] paramArrayOfByte);

  protected abstract void engineNextBytes(byte[] paramArrayOfByte);

  protected abstract byte[] engineGenerateSeed(int paramInt);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.SecureRandomSpi
 * JD-Core Version:    0.6.2
 */