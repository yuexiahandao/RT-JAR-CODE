package java.security;

import java.util.Set;

public abstract interface AlgorithmConstraints
{
  public abstract boolean permits(Set<CryptoPrimitive> paramSet, String paramString, AlgorithmParameters paramAlgorithmParameters);

  public abstract boolean permits(Set<CryptoPrimitive> paramSet, Key paramKey);

  public abstract boolean permits(Set<CryptoPrimitive> paramSet, String paramString, Key paramKey, AlgorithmParameters paramAlgorithmParameters);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.AlgorithmConstraints
 * JD-Core Version:    0.6.2
 */