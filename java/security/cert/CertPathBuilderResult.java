package java.security.cert;

public abstract interface CertPathBuilderResult extends Cloneable
{
  public abstract CertPath getCertPath();

  public abstract Object clone();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertPathBuilderResult
 * JD-Core Version:    0.6.2
 */