/*    */ package com.sun.jmx.remote.util;
/*    */ 
/*    */ import javax.management.loading.ClassLoaderRepository;
/*    */ 
/*    */ public class ClassLoaderWithRepository extends ClassLoader
/*    */ {
/*    */   private ClassLoaderRepository repository;
/*    */   private ClassLoader cl2;
/*    */ 
/*    */   public ClassLoaderWithRepository(ClassLoaderRepository paramClassLoaderRepository, ClassLoader paramClassLoader)
/*    */   {
/* 34 */     if (paramClassLoaderRepository == null) throw new IllegalArgumentException("Null ClassLoaderRepository object.");
/*    */ 
/* 37 */     this.repository = paramClassLoaderRepository;
/* 38 */     this.cl2 = paramClassLoader;
/*    */   }
/*    */ 
/*    */   protected Class<?> findClass(String paramString) throws ClassNotFoundException {
/*    */     try {
/* 43 */       return this.repository.loadClass(paramString);
/*    */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 45 */       if (this.cl2 != null) {
/* 46 */         return this.cl2.loadClass(paramString);
/*    */       }
/* 48 */       throw localClassNotFoundException;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.util.ClassLoaderWithRepository
 * JD-Core Version:    0.6.2
 */