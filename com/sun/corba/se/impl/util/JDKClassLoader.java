/*     */ package com.sun.corba.se.impl.util;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import sun.corba.Bridge;
/*     */ 
/*     */ class JDKClassLoader
/*     */ {
/*  48 */   private static final JDKClassLoaderCache classCache = new JDKClassLoaderCache(null);
/*     */ 
/*  51 */   private static final Bridge bridge = (Bridge)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Object run()
/*     */     {
/*  55 */       return Bridge.get();
/*     */     }
/*     */   });
/*     */ 
/*     */   static Class loadClass(Class paramClass, String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/*  64 */     if (paramString == null) {
/*  65 */       throw new NullPointerException();
/*     */     }
/*  67 */     if (paramString.length() == 0)
/*  68 */       throw new ClassNotFoundException();
/*     */     ClassLoader localClassLoader;
/*  82 */     if (paramClass != null)
/*  83 */       localClassLoader = paramClass.getClassLoader();
/*     */     else {
/*  85 */       localClassLoader = bridge.getLatestUserDefinedLoader();
/*     */     }
/*     */ 
/*  88 */     Object localObject = classCache.createKey(paramString, localClassLoader);
/*     */ 
/*  90 */     if (classCache.knownToFail(localObject)) {
/*  91 */       throw new ClassNotFoundException(paramString);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  97 */       return Class.forName(paramString, false, localClassLoader);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 102 */       classCache.recordFailure(localObject);
/* 103 */       throw localClassNotFoundException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class JDKClassLoaderCache
/*     */   {
/*     */     private final Map cache;
/* 143 */     private static final Object KNOWN_TO_FAIL = new Object();
/*     */ 
/*     */     private JDKClassLoaderCache()
/*     */     {
/* 137 */       this.cache = Collections.synchronizedMap(new WeakHashMap());
/*     */     }
/*     */ 
/*     */     public final void recordFailure(Object paramObject)
/*     */     {
/* 117 */       this.cache.put(paramObject, KNOWN_TO_FAIL);
/*     */     }
/*     */ 
/*     */     public final Object createKey(String paramString, ClassLoader paramClassLoader)
/*     */     {
/* 127 */       return new CacheKey(paramString, paramClassLoader);
/*     */     }
/*     */ 
/*     */     public final boolean knownToFail(Object paramObject)
/*     */     {
/* 133 */       return this.cache.get(paramObject) == KNOWN_TO_FAIL;
/*     */     }
/*     */ 
/*     */     private static class CacheKey
/*     */     {
/*     */       String className;
/*     */       ClassLoader loader;
/*     */ 
/*     */       public CacheKey(String paramString, ClassLoader paramClassLoader)
/*     */       {
/* 153 */         this.className = paramString;
/* 154 */         this.loader = paramClassLoader;
/*     */       }
/*     */ 
/*     */       public int hashCode()
/*     */       {
/* 160 */         if (this.loader == null) {
/* 161 */           return this.className.hashCode();
/*     */         }
/* 163 */         return this.className.hashCode() ^ this.loader.hashCode();
/*     */       }
/*     */ 
/*     */       public boolean equals(Object paramObject)
/*     */       {
/*     */         try
/*     */         {
/* 170 */           if (paramObject == null) {
/* 171 */             return false;
/*     */           }
/* 173 */           CacheKey localCacheKey = (CacheKey)paramObject;
/*     */ 
/* 183 */           return (this.className.equals(localCacheKey.className)) && (this.loader == localCacheKey.loader);
/*     */         }
/*     */         catch (ClassCastException localClassCastException) {
/*     */         }
/* 187 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.util.JDKClassLoader
 * JD-Core Version:    0.6.2
 */