package java.security;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public abstract class KeyFactorySpi
{
  protected abstract PublicKey engineGeneratePublic(KeySpec paramKeySpec)
    throws InvalidKeySpecException;

  protected abstract PrivateKey engineGeneratePrivate(KeySpec paramKeySpec)
    throws InvalidKeySpecException;

  protected abstract <T extends KeySpec> T engineGetKeySpec(Key paramKey, Class<T> paramClass)
    throws InvalidKeySpecException;

  protected abstract Key engineTranslateKey(Key paramKey)
    throws InvalidKeyException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.KeyFactorySpi
 * JD-Core Version:    0.6.2
 */