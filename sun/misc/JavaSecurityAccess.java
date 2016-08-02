package sun.misc;

import java.security.AccessControlContext;
import java.security.PrivilegedAction;

/**
 * 做交集特权
 */
public abstract interface JavaSecurityAccess {
    public abstract <T> T doIntersectionPrivilege(PrivilegedAction<T> paramPrivilegedAction, AccessControlContext paramAccessControlContext1, AccessControlContext paramAccessControlContext2);

    public abstract <T> T doIntersectionPrivilege(PrivilegedAction<T> paramPrivilegedAction, AccessControlContext paramAccessControlContext);
}
