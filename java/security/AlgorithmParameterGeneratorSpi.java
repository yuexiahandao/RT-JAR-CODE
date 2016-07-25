package java.security;

import java.security.spec.AlgorithmParameterSpec;

public abstract class AlgorithmParameterGeneratorSpi
{
  protected abstract void engineInit(int paramInt, SecureRandom paramSecureRandom);

  protected abstract void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
    throws InvalidAlgorithmParameterException;

  protected abstract AlgorithmParameters engineGenerateParameters();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.AlgorithmParameterGeneratorSpi
 * JD-Core Version:    0.6.2
 */