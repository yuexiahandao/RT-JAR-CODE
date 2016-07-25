/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ class RuntimePerm extends Perm
/*      */ {
/*      */   public RuntimePerm()
/*      */   {
/* 3977 */     super("RuntimePermission", "java.lang.RuntimePermission", new String[] { "createClassLoader", "getClassLoader", "setContextClassLoader", "enableContextClassLoaderOverride", "setSecurityManager", "createSecurityManager", "getenv.<" + PolicyTool.rb.getString("environment.variable.name") + ">", "exitVM", "shutdownHooks", "setFactory", "setIO", "modifyThread", "stopThread", "modifyThreadGroup", "getProtectionDomain", "readFileDescriptor", "writeFileDescriptor", "loadLibrary.<" + PolicyTool.rb.getString("library.name") + ">", "accessClassInPackage.<" + PolicyTool.rb.getString("package.name") + ">", "defineClassInPackage.<" + PolicyTool.rb.getString("package.name") + ">", "accessDeclaredMembers", "queuePrintJob", "getStackTrace", "setDefaultUncaughtExceptionHandler", "preferences", "usePolicy" }, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.RuntimePerm
 * JD-Core Version:    0.6.2
 */