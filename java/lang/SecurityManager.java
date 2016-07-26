/*      */
package java.lang;
/*      */ 
/*      */

import java.io.File;
/*      */ import java.io.FileDescriptor;
/*      */ import java.io.FilePermission;
/*      */ import java.net.InetAddress;
/*      */ import java.net.SocketPermission;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.Permission;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.Security;
/*      */ import java.security.SecurityPermission;
/*      */ import java.util.PropertyPermission;
/*      */ import java.util.StringTokenizer;
/*      */ import sun.security.util.SecurityConstants;
/*      */ import sun.security.util.SecurityConstants.AWT;

/*      */
/*      */ public class SecurityManager
/*      */ {
    /*      */
/*      */
    @Deprecated
/*      */ protected boolean inCheck;
    /*  243 */   private boolean initialized = false;
    /*      */
/*  619 */   private static ThreadGroup rootGroup = getRootGroup();
    /*      */
/* 1430 */   private static boolean packageAccessValid = false;
    /*      */   private static String[] packageAccess;
    /* 1432 */   private static final Object packageAccessLock = new Object();
    /*      */
/* 1434 */   private static boolean packageDefinitionValid = false;
    /*      */   private static String[] packageDefinition;
    /* 1436 */   private static final Object packageDefinitionLock = new Object();

    /*      */
/*      */
    private boolean hasAllPermission()
/*      */ {
/*      */
        try
/*      */ {
/*  252 */
            checkPermission(SecurityConstants.ALL_PERMISSION);
/*  253 */
            return true;
        } catch (SecurityException localSecurityException) {
/*      */
        }
/*  255 */
        return false;
/*      */
    }

    /*      */
/*      */
    @Deprecated
/*      */ public boolean getInCheck()
/*      */ {
/*  273 */
        return this.inCheck;
/*      */
    }

    /*      */
/*      */
    public SecurityManager()
/*      */ {
/*  294 */
        synchronized (SecurityManager.class) {
/*  295 */
            SecurityManager localSecurityManager = System.getSecurityManager();
/*  296 */
            if (localSecurityManager != null)
/*      */ {
/*  299 */
                localSecurityManager.checkPermission(new RuntimePermission("createSecurityManager"));
/*      */
            }
/*      */ 
/*  302 */
            this.initialized = true;
/*      */
        }
/*      */
    }

    /*      */
/*      */
    protected native Class[] getClassContext();

    /*      */
/*      */
    @Deprecated
/*      */ protected ClassLoader currentClassLoader()
/*      */ {
/*  357 */
        ClassLoader localClassLoader = currentClassLoader0();
/*  358 */
        if ((localClassLoader != null) && (hasAllPermission()))
/*  359 */ localClassLoader = null;
/*  360 */
        return localClassLoader;
/*      */
    }

    /*      */
/*      */
    private native ClassLoader currentClassLoader0();

    /*      */
/*      */
    @Deprecated
/*      */ protected Class<?> currentLoadedClass()
/*      */ {
/*  403 */
        Class localClass = currentLoadedClass0();
/*  404 */
        if ((localClass != null) && (hasAllPermission()))
/*  405 */ localClass = null;
/*  406 */
        return localClass;
/*      */
    }

    /*      */
/*      */
    @Deprecated
/*      */ protected native int classDepth(String paramString);

    /*      */
/*      */
    @Deprecated
/*      */ protected int classLoaderDepth()
/*      */ {
/*  462 */
        int i = classLoaderDepth0();
/*  463 */
        if (i != -1) {
/*  464 */
            if (hasAllPermission())
/*  465 */ i = -1;
/*      */
            else
/*  467 */         i--;
/*      */
        }
/*  469 */
        return i;
/*      */
    }

    /*      */
/*      */
    private native int classLoaderDepth0();

    /*      */
/*      */
    @Deprecated
/*      */ protected boolean inClass(String paramString)
/*      */ {
/*  487 */
        return classDepth(paramString) >= 0;
/*      */
    }

    /*      */
/*      */
    @Deprecated
/*      */ protected boolean inClassLoader()
/*      */ {
/*  504 */
        return currentClassLoader() != null;
/*      */
    }

    /*      */
/*      */
    public Object getSecurityContext()
/*      */ {
/*  530 */
        return AccessController.getContext();
/*      */
    }

    /*      */
/*      */
    public void checkPermission(Permission paramPermission)
/*      */ {
/*  549 */
        AccessController.checkPermission(paramPermission);
/*      */
    }

    /*      */
/*      */
    public void checkPermission(Permission paramPermission, Object paramObject)
/*      */ {
/*  584 */
        if ((paramObject instanceof AccessControlContext))
/*  585 */ ((AccessControlContext) paramObject).checkPermission(paramPermission);
/*      */
        else
/*  587 */       throw new SecurityException();
/*      */
    }

    /*      */
/*      */
    public void checkCreateClassLoader()
/*      */ {
/*  611 */
        checkPermission(SecurityConstants.CREATE_CLASSLOADER_PERMISSION);
/*      */
    }

    /*      */
/*      */
    private static ThreadGroup getRootGroup()
/*      */ {
/*  622 */
        ThreadGroup localThreadGroup = Thread.currentThread().getThreadGroup();
/*  623 */
        while (localThreadGroup.getParent() != null) {
/*  624 */
            localThreadGroup = localThreadGroup.getParent();
/*      */
        }
/*  626 */
        return localThreadGroup;
/*      */
    }

    /*      */
/*      */
    public void checkAccess(Thread paramThread)
/*      */ {
/*  672 */
        if (paramThread == null) {
/*  673 */
            throw new NullPointerException("thread can't be null");
/*      */
        }
/*  675 */
        if (paramThread.getThreadGroup() == rootGroup)
/*  676 */ checkPermission(SecurityConstants.MODIFY_THREAD_PERMISSION);
/*      */
    }

    /*      */
/*      */
    public void checkAccess(ThreadGroup paramThreadGroup)
/*      */ {
/*  725 */
        if (paramThreadGroup == null) {
/*  726 */
            throw new NullPointerException("thread group can't be null");
/*      */
        }
/*  728 */
        if (paramThreadGroup == rootGroup)
/*  729 */ checkPermission(SecurityConstants.MODIFY_THREADGROUP_PERMISSION);
/*      */
    }

    /*      */
/*      */
    public void checkExit(int paramInt)
/*      */ {
/*  761 */
        checkPermission(new RuntimePermission("exitVM." + paramInt));
/*      */
    }

    /*      */
/*      */
    public void checkExec(String paramString)
/*      */ {
/*  794 */
        File localFile = new File(paramString);
/*  795 */
        if (localFile.isAbsolute()) {
/*  796 */
            checkPermission(new FilePermission(paramString, "execute"));
/*      */
        }
/*      */
        else
/*  799 */       checkPermission(new FilePermission("<<ALL FILES>>", "execute"));
/*      */
    }

    /*      */
/*      */
    public void checkLink(String paramString)
/*      */ {
/*  832 */
        if (paramString == null) {
/*  833 */
            throw new NullPointerException("library can't be null");
/*      */
        }
/*  835 */
        checkPermission(new RuntimePermission("loadLibrary." + paramString));
/*      */
    }

    /*      */
/*      */
    public void checkRead(FileDescriptor paramFileDescriptor)
/*      */ {
/*  861 */
        if (paramFileDescriptor == null) {
/*  862 */
            throw new NullPointerException("file descriptor can't be null");
/*      */
        }
/*  864 */
        checkPermission(new RuntimePermission("readFileDescriptor"));
/*      */
    }

    /*      */
/*      */
    public void checkRead(String paramString)
/*      */ {
/*  888 */
        checkPermission(new FilePermission(paramString, "read"));
/*      */
    }

    /*      */
/*      */
    public void checkRead(String paramString, Object paramObject)
/*      */ {
/*  923 */
        checkPermission(new FilePermission(paramString, "read"), paramObject);
/*      */
    }

    /*      */
/*      */
    public void checkWrite(FileDescriptor paramFileDescriptor)
/*      */ {
/*  951 */
        if (paramFileDescriptor == null) {
/*  952 */
            throw new NullPointerException("file descriptor can't be null");
/*      */
        }
/*  954 */
        checkPermission(new RuntimePermission("writeFileDescriptor"));
/*      */
    }

    /*      */
/*      */
    public void checkWrite(String paramString)
/*      */ {
/*  979 */
        checkPermission(new FilePermission(paramString, "write"));
/*      */
    }

    /*      */
/*      */
    public void checkDelete(String paramString)
/*      */ {
/* 1007 */
        checkPermission(new FilePermission(paramString, "delete"));
/*      */
    }

    /*      */
/*      */
    public void checkConnect(String paramString, int paramInt)
/*      */ {
/* 1041 */
        if (paramString == null) {
/* 1042 */
            throw new NullPointerException("host can't be null");
/*      */
        }
/* 1044 */
        if ((!paramString.startsWith("[")) && (paramString.indexOf(':') != -1)) {
/* 1045 */
            paramString = "[" + paramString + "]";
/*      */
        }
/* 1047 */
        if (paramInt == -1) {
/* 1048 */
            checkPermission(new SocketPermission(paramString, "resolve"));
/*      */
        }
/*      */
        else
/* 1051 */       checkPermission(new SocketPermission(paramString + ":" + paramInt, "connect"));
/*      */
    }

    /*      */
/*      */
    public void checkConnect(String paramString, int paramInt, Object paramObject)
/*      */ {
/* 1096 */
        if (paramString == null) {
/* 1097 */
            throw new NullPointerException("host can't be null");
/*      */
        }
/* 1099 */
        if ((!paramString.startsWith("[")) && (paramString.indexOf(':') != -1)) {
/* 1100 */
            paramString = "[" + paramString + "]";
/*      */
        }
/* 1102 */
        if (paramInt == -1) {
/* 1103 */
            checkPermission(new SocketPermission(paramString, "resolve"), paramObject);
/*      */
        }
/*      */
        else
/*      */ {
/* 1107 */
            checkPermission(new SocketPermission(paramString + ":" + paramInt, "connect"), paramObject);
/*      */
        }
/*      */
    }

    /*      */
/*      */
    public void checkListen(int paramInt)
/*      */ {
/* 1134 */
        checkPermission(new SocketPermission("localhost:" + paramInt, "listen"));
/*      */
    }

    /*      */
/*      */
    public void checkAccept(String paramString, int paramInt)
/*      */ {
/* 1164 */
        if (paramString == null) {
/* 1165 */
            throw new NullPointerException("host can't be null");
/*      */
        }
/* 1167 */
        if ((!paramString.startsWith("[")) && (paramString.indexOf(':') != -1)) {
/* 1168 */
            paramString = "[" + paramString + "]";
/*      */
        }
/* 1170 */
        checkPermission(new SocketPermission(paramString + ":" + paramInt, "accept"));
/*      */
    }

    /*      */
/*      */
    public void checkMulticast(InetAddress paramInetAddress)
/*      */ {
/* 1197 */
        String str = paramInetAddress.getHostAddress();
/* 1198 */
        if ((!str.startsWith("[")) && (str.indexOf(':') != -1)) {
/* 1199 */
            str = "[" + str + "]";
/*      */
        }
/* 1201 */
        checkPermission(new SocketPermission(str, "connect,accept"));
/*      */
    }

    /*      */
/*      */
    @Deprecated
/*      */ public void checkMulticast(InetAddress paramInetAddress, byte paramByte)
/*      */ {
/* 1233 */
        String str = paramInetAddress.getHostAddress();
/* 1234 */
        if ((!str.startsWith("[")) && (str.indexOf(':') != -1)) {
/* 1235 */
            str = "[" + str + "]";
/*      */
        }
/* 1237 */
        checkPermission(new SocketPermission(str, "connect,accept"));
/*      */
    }

    /*      */
/*      */
    public void checkPropertiesAccess()
/*      */ {
/* 1265 */
        checkPermission(new PropertyPermission("*", "read,write"));
/*      */
    }

    /*      */
/*      */
    public void checkPropertyAccess(String paramString)
/*      */ {
/* 1298 */
        checkPermission(new PropertyPermission(paramString, "read"));
/*      */
    }

    /*      */
/*      */
    public boolean checkTopLevelWindow(Object paramObject)
/*      */ {
/* 1336 */
        if (paramObject == null)
/* 1337 */ throw new NullPointerException("window can't be null");
/*      */
        try
/*      */ {
/* 1340 */
            checkPermission(SecurityConstants.AWT.TOPLEVEL_WINDOW_PERMISSION);
/* 1341 */
            return true;
/*      */
        }
/*      */ catch (SecurityException localSecurityException) {
/*      */
        }
/* 1345 */
        return false;
/*      */
    }

    /*      */
/*      */
    public void checkPrintJobAccess()
/*      */ {
/* 1368 */
        checkPermission(new RuntimePermission("queuePrintJob"));
/*      */
    }

    /*      */
/*      */
    public void checkSystemClipboardAccess()
/*      */ {
/* 1390 */
        checkPermission(SecurityConstants.AWT.ACCESS_CLIPBOARD_PERMISSION);
/*      */
    }

    /*      */
/*      */
    public void checkAwtEventQueueAccess()
/*      */ {
/* 1411 */
        checkPermission(SecurityConstants.AWT.CHECK_AWT_EVENTQUEUE_PERMISSION);
/*      */
    }

    /*      */
/*      */
    private static String[] getPackages(String paramString)
/*      */ {
/* 1439 */
        String[] arrayOfString = null;
/* 1440 */
        if ((paramString != null) && (!paramString.equals(""))) {
/* 1441 */
            StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
/*      */ 
/* 1443 */
            int i = localStringTokenizer.countTokens();
/* 1444 */
            if (i > 0) {
/* 1445 */
                arrayOfString = new String[i];
/* 1446 */
                int j = 0;
/* 1447 */
                while (localStringTokenizer.hasMoreElements()) {
/* 1448 */
                    String str = localStringTokenizer.nextToken().trim();
/* 1449 */
                    arrayOfString[(j++)] = str;
/*      */
                }
/*      */
            }
/*      */
        }
/*      */ 
/* 1454 */
        if (arrayOfString == null)
/* 1455 */ arrayOfString = new String[0];
/* 1456 */
        return arrayOfString;
/*      */
    }

    /*      */
/*      */
    public void checkPackageAccess(String paramString)
/*      */ {
/* 1492 */
        if (paramString == null)
/* 1493 */ throw new NullPointerException("package name can't be null");
/*      */
        String[] arrayOfString;
/* 1497 */
        synchronized (packageAccessLock)
/*      */ {
/* 1501 */
            if (!packageAccessValid) {
/* 1502 */
                String str = (String) AccessController.doPrivileged(new PrivilegedAction()
/*      */ {
                    /*      */
                    public String run()
/*      */ {
/* 1506 */
                        return Security.getProperty("package.access");
/*      */
                    }
/*      */
                });
/* 1511 */
                packageAccess = getPackages(str);
/* 1512 */
                packageAccessValid = true;
/*      */
            }
/*      */ 
/* 1517 */
            arrayOfString = packageAccess;
/*      */
        }
/*      */ 
/* 1523 */
        for (int i = 0; i < arrayOfString.length; i++)
/* 1524 */
            if ((paramString.startsWith(arrayOfString[i])) || (arrayOfString[i].equals(paramString + "."))) {
/* 1525 */
                checkPermission(new RuntimePermission("accessClassInPackage." + paramString));
/*      */ 
/* 1527 */
                break;
/*      */
            }
/*      */
    }

    /*      */
/*      */
    public void checkPackageDefinition(String paramString)
/*      */ {
/* 1561 */
        if (paramString == null)
/* 1562 */ throw new NullPointerException("package name can't be null");
/*      */
        String[] arrayOfString;
/* 1566 */
        synchronized (packageDefinitionLock)
/*      */ {
/* 1570 */
            if (!packageDefinitionValid) {
/* 1571 */
                String str = (String) AccessController.doPrivileged(new PrivilegedAction()
/*      */ {
                    /*      */
                    public String run()
/*      */ {
/* 1575 */
                        return Security.getProperty("package.definition");
/*      */
                    }
/*      */
                });
/* 1580 */
                packageDefinition = getPackages(str);
/* 1581 */
                packageDefinitionValid = true;
/*      */
            }
/*      */ 
/* 1585 */
            arrayOfString = packageDefinition;
/*      */
        }
/*      */ 
/* 1591 */
        for (int i = 0; i < arrayOfString.length; i++)
/* 1592 */
            if ((paramString.startsWith(arrayOfString[i])) || (arrayOfString[i].equals(paramString + "."))) {
/* 1593 */
                checkPermission(new RuntimePermission("defineClassInPackage." + paramString));
/*      */ 
/* 1595 */
                break;
/*      */
            }
/*      */
    }

    /*      */
/*      */
    public void checkSetFactory()
/*      */ {
/* 1625 */
        checkPermission(new RuntimePermission("setFactory"));
/*      */
    }

    /*      */
/*      */
    public void checkMemberAccess(Class<?> paramClass, int paramInt)
/*      */ {
/* 1657 */
        if (paramClass == null) {
/* 1658 */
            throw new NullPointerException("class can't be null");
/*      */
        }
/* 1660 */
        if (paramInt != 0) {
/* 1661 */
            Class[] arrayOfClass = getClassContext();
/*      */ 
/* 1673 */
            if ((arrayOfClass.length < 4) || (arrayOfClass[3].getClassLoader() != paramClass.getClassLoader()))
/*      */ {
/* 1675 */
                checkPermission(SecurityConstants.CHECK_MEMBER_ACCESS_PERMISSION);
/*      */
            }
/*      */
        }
/*      */
    }

    /*      */
/*      */
    public void checkSecurityAccess(String paramString)
/*      */ {
/* 1711 */
        checkPermission(new SecurityPermission(paramString));
/*      */
    }

    /*      */
/*      */
    private native Class currentLoadedClass0();

    /*      */
/*      */
    public ThreadGroup getThreadGroup()
/*      */ {
/* 1728 */
        return Thread.currentThread().getThreadGroup();
/*      */
    }
/*      */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.SecurityManager
 * JD-Core Version:    0.6.2
 */