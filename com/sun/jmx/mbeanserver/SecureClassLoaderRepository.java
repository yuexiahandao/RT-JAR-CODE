/*    */ package com.sun.jmx.mbeanserver;
/*    */ 
/*    */ import javax.management.loading.ClassLoaderRepository;
/*    */ 
/*    */ final class SecureClassLoaderRepository
/*    */   implements ClassLoaderRepository
/*    */ {
/*    */   private final ClassLoaderRepository clr;
/*    */ 
/*    */   public SecureClassLoaderRepository(ClassLoaderRepository paramClassLoaderRepository)
/*    */   {
/* 48 */     this.clr = paramClassLoaderRepository;
/*    */   }
/*    */ 
/*    */   public final Class<?> loadClass(String paramString) throws ClassNotFoundException {
/* 52 */     return this.clr.loadClass(paramString);
/*    */   }
/*    */ 
/*    */   public final Class<?> loadClassWithout(ClassLoader paramClassLoader, String paramString) throws ClassNotFoundException
/*    */   {
/* 57 */     return this.clr.loadClassWithout(paramClassLoader, paramString);
/*    */   }
/*    */ 
/*    */   public final Class<?> loadClassBefore(ClassLoader paramClassLoader, String paramString) throws ClassNotFoundException
/*    */   {
/* 62 */     return this.clr.loadClassBefore(paramClassLoader, paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.SecureClassLoaderRepository
 * JD-Core Version:    0.6.2
 */