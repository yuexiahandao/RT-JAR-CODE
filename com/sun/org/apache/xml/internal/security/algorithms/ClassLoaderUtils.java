/*     */ package com.sun.org.apache.xml.internal.security.algorithms;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ final class ClassLoaderUtils
/*     */ {
/*  45 */   private static final Logger log = Logger.getLogger(ClassLoaderUtils.class.getName());
/*     */ 
/*     */   static URL getResource(String paramString, Class<?> paramClass)
/*     */   {
/*  64 */     URL localURL = Thread.currentThread().getContextClassLoader().getResource(paramString);
/*  65 */     if ((localURL == null) && (paramString.startsWith("/")))
/*     */     {
/*  67 */       localURL = Thread.currentThread().getContextClassLoader().getResource(paramString.substring(1));
/*     */     }
/*     */ 
/*  73 */     ClassLoader localClassLoader1 = ClassLoaderUtils.class.getClassLoader();
/*  74 */     if (localClassLoader1 == null) {
/*  75 */       localClassLoader1 = ClassLoader.getSystemClassLoader();
/*     */     }
/*  77 */     if (localURL == null) {
/*  78 */       localURL = localClassLoader1.getResource(paramString);
/*     */     }
/*  80 */     if ((localURL == null) && (paramString.startsWith("/")))
/*     */     {
/*  82 */       localURL = localClassLoader1.getResource(paramString.substring(1));
/*     */     }
/*     */ 
/*  85 */     if (localURL == null) {
/*  86 */       ClassLoader localClassLoader2 = paramClass.getClassLoader();
/*     */ 
/*  88 */       if (localClassLoader2 != null) {
/*  89 */         localURL = localClassLoader2.getResource(paramString);
/*     */       }
/*     */     }
/*     */ 
/*  93 */     if (localURL == null) {
/*  94 */       localURL = paramClass.getResource(paramString);
/*     */     }
/*     */ 
/*  97 */     if ((localURL == null) && (paramString != null) && (paramString.charAt(0) != '/')) {
/*  98 */       return getResource('/' + paramString, paramClass);
/*     */     }
/*     */ 
/* 101 */     return localURL;
/*     */   }
/*     */ 
/*     */   static List<URL> getResources(String paramString, Class<?> paramClass)
/*     */   {
/* 117 */     ArrayList localArrayList = new ArrayList();
/* 118 */     Object localObject1 = new Enumeration() {
/*     */       public boolean hasMoreElements() {
/* 120 */         return false;
/*     */       }
/*     */       public URL nextElement() {
/* 123 */         return null;
/*     */       }
/*     */     };
/*     */     try
/*     */     {
/* 128 */       localObject1 = Thread.currentThread().getContextClassLoader().getResources(paramString);
/*     */     } catch (IOException localIOException1) {
/* 130 */       if (log.isLoggable(Level.FINE)) {
/* 131 */         log.log(Level.FINE, localIOException1.getMessage(), localIOException1);
/*     */       }
/*     */     }
/*     */ 
/* 135 */     if ((!((Enumeration)localObject1).hasMoreElements()) && (paramString.startsWith("/"))) {
/*     */       try
/*     */       {
/* 138 */         localObject1 = Thread.currentThread().getContextClassLoader().getResources(paramString.substring(1));
/*     */       }
/*     */       catch (IOException localIOException2)
/*     */       {
/* 143 */         if (log.isLoggable(Level.FINE)) {
/* 144 */           log.log(Level.FINE, localIOException2.getMessage(), localIOException2);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 150 */     ClassLoader localClassLoader = ClassLoaderUtils.class.getClassLoader();
/* 151 */     if (localClassLoader == null) {
/* 152 */       localClassLoader = ClassLoader.getSystemClassLoader();
/*     */     }
/* 154 */     if (!((Enumeration)localObject1).hasMoreElements()) {
/*     */       try {
/* 156 */         localObject1 = localClassLoader.getResources(paramString);
/*     */       } catch (IOException localIOException3) {
/* 158 */         if (log.isLoggable(Level.FINE)) {
/* 159 */           log.log(Level.FINE, localIOException3.getMessage(), localIOException3);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 164 */     if ((!((Enumeration)localObject1).hasMoreElements()) && (paramString.startsWith("/")))
/*     */       try
/*     */       {
/* 167 */         localObject1 = localClassLoader.getResources(paramString.substring(1));
/*     */       } catch (IOException localIOException4) {
/* 169 */         if (log.isLoggable(Level.FINE))
/* 170 */           log.log(Level.FINE, localIOException4.getMessage(), localIOException4);
/*     */       }
/*     */     Object localObject2;
/* 176 */     if (!((Enumeration)localObject1).hasMoreElements()) {
/* 177 */       localObject2 = paramClass.getClassLoader();
/*     */ 
/* 179 */       if (localObject2 != null) {
/*     */         try {
/* 181 */           localObject1 = ((ClassLoader)localObject2).getResources(paramString);
/*     */         } catch (IOException localIOException5) {
/* 183 */           if (log.isLoggable(Level.FINE)) {
/* 184 */             log.log(Level.FINE, localIOException5.getMessage(), localIOException5);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 191 */     if (!((Enumeration)localObject1).hasMoreElements()) {
/* 192 */       localObject2 = paramClass.getResource(paramString);
/* 193 */       if (localObject2 != null) {
/* 194 */         localArrayList.add(localObject2);
/*     */       }
/*     */     }
/* 197 */     while (((Enumeration)localObject1).hasMoreElements()) {
/* 198 */       localArrayList.add(((Enumeration)localObject1).nextElement());
/*     */     }
/*     */ 
/* 202 */     if ((localArrayList.isEmpty()) && (paramString != null) && (paramString.charAt(0) != '/')) {
/* 203 */       return getResources('/' + paramString, paramClass);
/*     */     }
/* 205 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   static InputStream getResourceAsStream(String paramString, Class<?> paramClass)
/*     */   {
/* 217 */     URL localURL = getResource(paramString, paramClass);
/*     */     try
/*     */     {
/* 220 */       return localURL != null ? localURL.openStream() : null;
/*     */     } catch (IOException localIOException) {
/* 222 */       if (log.isLoggable(Level.FINE))
/* 223 */         log.log(Level.FINE, localIOException.getMessage(), localIOException);
/*     */     }
/* 225 */     return null;
/*     */   }
/*     */ 
/*     */   static Class<?> loadClass(String paramString, Class<?> paramClass)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 246 */       ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */ 
/* 248 */       if (localClassLoader != null)
/* 249 */         return localClassLoader.loadClass(paramString);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 252 */       if (log.isLoggable(Level.FINE)) {
/* 253 */         log.log(Level.FINE, localClassNotFoundException.getMessage(), localClassNotFoundException);
/*     */       }
/*     */     }
/*     */ 
/* 257 */     return loadClass2(paramString, paramClass);
/*     */   }
/*     */ 
/*     */   private static Class<?> loadClass2(String paramString, Class<?> paramClass) throws ClassNotFoundException
/*     */   {
/*     */     try {
/* 263 */       return Class.forName(paramString);
/*     */     } catch (ClassNotFoundException localClassNotFoundException1) {
/*     */       try {
/* 266 */         if (ClassLoaderUtils.class.getClassLoader() != null)
/* 267 */           return ClassLoaderUtils.class.getClassLoader().loadClass(paramString);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException2) {
/* 270 */         if ((paramClass != null) && (paramClass.getClassLoader() != null)) {
/* 271 */           return paramClass.getClassLoader().loadClass(paramString);
/*     */         }
/*     */       }
/* 274 */       if (log.isLoggable(Level.FINE)) {
/* 275 */         log.log(Level.FINE, localClassNotFoundException1.getMessage(), localClassNotFoundException1);
/*     */       }
/* 277 */       throw localClassNotFoundException1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.algorithms.ClassLoaderUtils
 * JD-Core Version:    0.6.2
 */