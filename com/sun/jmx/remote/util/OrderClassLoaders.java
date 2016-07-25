/*    */ package com.sun.jmx.remote.util;
/*    */ 
/*    */ import sun.reflect.misc.ReflectUtil;
/*    */ 
/*    */ public class OrderClassLoaders extends ClassLoader
/*    */ {
/*    */   private ClassLoader cl2;
/*    */ 
/*    */   public OrderClassLoaders(ClassLoader paramClassLoader1, ClassLoader paramClassLoader2)
/*    */   {
/* 32 */     super(paramClassLoader1);
/*    */ 
/* 34 */     this.cl2 = paramClassLoader2;
/*    */   }
/*    */ 
/*    */   protected Class<?> loadClass(String paramString, boolean paramBoolean) throws ClassNotFoundException {
/* 38 */     ReflectUtil.checkPackageAccess(paramString);
/*    */     try {
/* 40 */       return super.loadClass(paramString, paramBoolean);
/*    */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 42 */       if (this.cl2 != null) {
/* 43 */         return this.cl2.loadClass(paramString);
/*    */       }
/* 45 */       throw localClassNotFoundException;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.util.OrderClassLoaders
 * JD-Core Version:    0.6.2
 */