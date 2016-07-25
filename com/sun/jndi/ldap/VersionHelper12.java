/*    */ package com.sun.jndi.ldap;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URLClassLoader;
/*    */ import java.security.AccessControlContext;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import sun.misc.JavaLangAccess;
/*    */ import sun.misc.SharedSecrets;
/*    */ 
/*    */ final class VersionHelper12 extends VersionHelper
/*    */ {
/*    */   private static final String TRUST_URL_CODEBASE_PROPERTY = "com.sun.jndi.ldap.object.trustURLCodebase";
/* 43 */   private static final String trustURLCodebase = (String)AccessController.doPrivileged(new PrivilegedAction()
/*    */   {
/*    */     public String run()
/*    */     {
/* 47 */       return System.getProperty("com.sun.jndi.ldap.object.trustURLCodebase", "false");
/*    */     }
/*    */   });
/*    */ 
/*    */   ClassLoader getURLClassLoader(String[] paramArrayOfString)
/*    */     throws MalformedURLException
/*    */   {
/* 57 */     ClassLoader localClassLoader = getContextClassLoader();
/*    */ 
/* 63 */     if ((paramArrayOfString != null) && ("true".equalsIgnoreCase(trustURLCodebase))) {
/* 64 */       return URLClassLoader.newInstance(getUrlArray(paramArrayOfString), localClassLoader);
/*    */     }
/* 66 */     return localClassLoader;
/*    */   }
/*    */ 
/*    */   Class loadClass(String paramString) throws ClassNotFoundException
/*    */   {
/* 71 */     ClassLoader localClassLoader = getContextClassLoader();
/* 72 */     return Class.forName(paramString, true, localClassLoader);
/*    */   }
/*    */ 
/*    */   private ClassLoader getContextClassLoader() {
/* 76 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Object run() {
/* 79 */         return Thread.currentThread().getContextClassLoader();
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   Thread createThread(final Runnable paramRunnable)
/*    */   {
/* 86 */     final AccessControlContext localAccessControlContext = AccessController.getContext();
/*    */ 
/* 89 */     return (Thread)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Thread run() {
/* 92 */         return SharedSecrets.getJavaLangAccess().newThreadWithAcc(paramRunnable, localAccessControlContext);
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.VersionHelper12
 * JD-Core Version:    0.6.2
 */