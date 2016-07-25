package java.security;

public abstract interface DomainCombiner
{
  public abstract ProtectionDomain[] combine(ProtectionDomain[] paramArrayOfProtectionDomain1, ProtectionDomain[] paramArrayOfProtectionDomain2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.DomainCombiner
 * JD-Core Version:    0.6.2
 */