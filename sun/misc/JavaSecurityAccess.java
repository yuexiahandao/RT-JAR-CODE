package sun.misc;

import java.security.AccessControlContext;
import java.security.PrivilegedAction;

public abstract interface JavaSecurityAccess
{
  public abstract <T> T doIntersectionPrivilege(PrivilegedAction<T> paramPrivilegedAction, AccessControlContext paramAccessControlContext1, AccessControlContext paramAccessControlContext2);

  public abstract <T> T doIntersectionPrivilege(PrivilegedAction<T> paramPrivilegedAction, AccessControlContext paramAccessControlContext);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.JavaSecurityAccess
 * JD-Core Version:    0.6.2
 */