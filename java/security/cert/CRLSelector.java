package java.security.cert;

public abstract interface CRLSelector extends Cloneable
{
  public abstract boolean match(CRL paramCRL);

  public abstract Object clone();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CRLSelector
 * JD-Core Version:    0.6.2
 */