/*     */ package com.sun.beans.finder;
/*     */ 
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public final class ClassFinder
/*     */ {
/*     */   public static Class<?> findClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/*  59 */     ReflectUtil.checkPackageAccess(paramString);
/*     */     try {
/*  61 */       ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*  62 */       if (localClassLoader == null)
/*     */       {
/*  64 */         localClassLoader = ClassLoader.getSystemClassLoader();
/*     */       }
/*  66 */       if (localClassLoader != null)
/*  67 */         return Class.forName(paramString, false, localClassLoader);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/*     */     }
/*     */     catch (SecurityException localSecurityException)
/*     */     {
/*     */     }
/*  75 */     return Class.forName(paramString);
/*     */   }
/*     */ 
/*     */   public static Class<?> findClass(String paramString, ClassLoader paramClassLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/* 100 */     ReflectUtil.checkPackageAccess(paramString);
/* 101 */     if (paramClassLoader != null)
/*     */       try {
/* 103 */         return Class.forName(paramString, false, paramClassLoader);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException)
/*     */       {
/*     */       }
/*     */       catch (SecurityException localSecurityException) {
/*     */       }
/* 110 */     return findClass(paramString);
/*     */   }
/*     */ 
/*     */   public static Class<?> resolveClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 137 */     return resolveClass(paramString, null);
/*     */   }
/*     */ 
/*     */   public static Class<?> resolveClass(String paramString, ClassLoader paramClassLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/* 169 */     Class localClass = PrimitiveTypeMap.getType(paramString);
/* 170 */     return localClass == null ? findClass(paramString, paramClassLoader) : localClass;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.ClassFinder
 * JD-Core Version:    0.6.2
 */