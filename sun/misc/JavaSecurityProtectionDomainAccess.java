package sun.misc;

import java.security.PermissionCollection;
import java.security.ProtectionDomain;

public abstract interface JavaSecurityProtectionDomainAccess
{
  public abstract ProtectionDomainCache getProtectionDomainCache();

  public static abstract interface ProtectionDomainCache
  {
    public abstract void put(ProtectionDomain paramProtectionDomain, PermissionCollection paramPermissionCollection);

    public abstract PermissionCollection get(ProtectionDomain paramProtectionDomain);
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.JavaSecurityProtectionDomainAccess
 * JD-Core Version:    0.6.2
 */