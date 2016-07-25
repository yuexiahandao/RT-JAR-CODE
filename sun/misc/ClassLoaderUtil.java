/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import java.util.jar.JarFile;
/*     */ 
/*     */ public class ClassLoaderUtil
/*     */ {
/*     */   public static void releaseLoader(URLClassLoader paramURLClassLoader)
/*     */   {
/*  53 */     releaseLoader(paramURLClassLoader, null);
/*     */   }
/*     */ 
/*     */   public static List<IOException> releaseLoader(URLClassLoader paramURLClassLoader, List<String> paramList)
/*     */   {
/*  71 */     LinkedList localLinkedList = new LinkedList();
/*     */     try
/*     */     {
/*  76 */       if (paramList != null) {
/*  77 */         paramList.clear();
/*     */       }
/*     */ 
/*  80 */       URLClassPath localURLClassPath = SharedSecrets.getJavaNetAccess().getURLClassPath(paramURLClassLoader);
/*     */ 
/*  82 */       ArrayList localArrayList = localURLClassPath.loaders;
/*  83 */       Stack localStack = localURLClassPath.urls;
/*  84 */       HashMap localHashMap = localURLClassPath.lmap;
/*     */ 
/*  94 */       synchronized (localStack) {
/*  95 */         localStack.clear();
/*     */       }
/*     */ 
/* 102 */       synchronized (localHashMap) {
/* 103 */         localHashMap.clear();
/*     */       }
/*     */ 
/* 129 */       synchronized (localURLClassPath) {
/* 130 */         for (Iterator localIterator = localArrayList.iterator(); localIterator.hasNext(); ) { Object localObject3 = localIterator.next();
/* 131 */           if (localObject3 != null)
/*     */           {
/* 137 */             if ((localObject3 instanceof URLClassPath.JarLoader)) {
/* 138 */               URLClassPath.JarLoader localJarLoader = (URLClassPath.JarLoader)localObject3;
/* 139 */               JarFile localJarFile = localJarLoader.getJarFile();
/*     */               try {
/* 141 */                 if (localJarFile != null) {
/* 142 */                   localJarFile.close();
/* 143 */                   if (paramList != null) {
/* 144 */                     paramList.add(localJarFile.getName());
/*     */                   }
/*     */ 
/*     */                 }
/*     */ 
/*     */               }
/*     */               catch (IOException localIOException1)
/*     */               {
/* 153 */                 String str1 = localJarFile == null ? "filename not available" : localJarFile.getName();
/* 154 */                 String str2 = "Error closing JAR file: " + str1;
/* 155 */                 IOException localIOException2 = new IOException(str2);
/* 156 */                 localIOException2.initCause(localIOException1);
/* 157 */                 localLinkedList.add(localIOException2);
/*     */               }
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 165 */         localArrayList.clear();
/*     */       }
/*     */     } catch (Throwable localThrowable) {
/* 168 */       throw new RuntimeException(localThrowable);
/*     */     }
/* 170 */     return localLinkedList;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.ClassLoaderUtil
 * JD-Core Version:    0.6.2
 */