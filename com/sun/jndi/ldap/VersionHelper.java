/*    */ package com.sun.jndi.ldap;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ 
/*    */ abstract class VersionHelper
/*    */ {
/* 33 */   private static VersionHelper helper = null;
/*    */ 
/*    */   static VersionHelper getVersionHelper()
/*    */   {
/* 60 */     return helper;
/*    */   }
/*    */ 
/*    */   abstract ClassLoader getURLClassLoader(String[] paramArrayOfString)
/*    */     throws MalformedURLException;
/*    */ 
/*    */   protected static URL[] getUrlArray(String[] paramArrayOfString) throws MalformedURLException
/*    */   {
/* 68 */     URL[] arrayOfURL = new URL[paramArrayOfString.length];
/* 69 */     for (int i = 0; i < arrayOfURL.length; i++) {
/* 70 */       arrayOfURL[i] = new URL(paramArrayOfString[i]);
/*    */     }
/* 72 */     return arrayOfURL;
/*    */   }
/*    */ 
/*    */   abstract Class loadClass(String paramString)
/*    */     throws ClassNotFoundException;
/*    */ 
/*    */   abstract Thread createThread(Runnable paramRunnable);
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 39 */       Class.forName("java.net.URLClassLoader");
/* 40 */       Class.forName("java.security.PrivilegedAction");
/* 41 */       helper = (VersionHelper)Class.forName("com.sun.jndi.ldap.VersionHelper12").newInstance();
/*    */     }
/*    */     catch (Exception localException1)
/*    */     {
/*    */     }
/*    */ 
/* 48 */     if (helper == null)
/*    */       try {
/* 50 */         helper = (VersionHelper)Class.forName("com.sun.jndi.ldap.VersionHelper11").newInstance();
/*    */       }
/*    */       catch (Exception localException2)
/*    */       {
/*    */       }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.VersionHelper
 * JD-Core Version:    0.6.2
 */