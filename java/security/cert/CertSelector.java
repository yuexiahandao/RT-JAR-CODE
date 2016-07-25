package java.security.cert;

public abstract interface CertSelector extends Cloneable
{
  public abstract boolean match(Certificate paramCertificate);

  public abstract Object clone();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertSelector
 * JD-Core Version:    0.6.2
 */