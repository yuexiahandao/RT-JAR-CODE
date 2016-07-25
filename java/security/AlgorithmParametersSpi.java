package java.security;

import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;

public abstract class AlgorithmParametersSpi
{
  protected abstract void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws InvalidParameterSpecException;

  protected abstract void engineInit(byte[] paramArrayOfByte)
    throws IOException;

  protected abstract void engineInit(byte[] paramArrayOfByte, String paramString)
    throws IOException;

  protected abstract <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> paramClass)
    throws InvalidParameterSpecException;

  protected abstract byte[] engineGetEncoded()
    throws IOException;

  protected abstract byte[] engineGetEncoded(String paramString)
    throws IOException;

  protected abstract String engineToString();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.AlgorithmParametersSpi
 * JD-Core Version:    0.6.2
 */