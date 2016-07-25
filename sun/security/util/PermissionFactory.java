package sun.security.util;

import java.security.Permission;

public abstract interface PermissionFactory<T extends Permission>
{
  public abstract T newPermission(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.PermissionFactory
 * JD-Core Version:    0.6.2
 */