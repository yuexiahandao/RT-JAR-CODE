package javax.xml.crypto;

import java.security.spec.AlgorithmParameterSpec;

public abstract interface AlgorithmMethod
{
  public abstract String getAlgorithm();

  public abstract AlgorithmParameterSpec getParameterSpec();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.AlgorithmMethod
 * JD-Core Version:    0.6.2
 */