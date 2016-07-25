/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import javax.sound.sampled.AudioPermission;
/*     */ import sun.misc.Service;
/*     */ 
/*     */ final class JSSecurityManager
/*     */ {
/*     */   private static boolean hasSecurityManager()
/*     */   {
/*  66 */     return System.getSecurityManager() != null;
/*     */   }
/*     */ 
/*     */   static void checkRecordPermission()
/*     */     throws SecurityException
/*     */   {
/*  72 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  73 */     if (localSecurityManager != null)
/*  74 */       localSecurityManager.checkPermission(new AudioPermission("record"));
/*     */   }
/*     */ 
/*     */   static String getProperty(String paramString)
/*     */   {
/*     */     String str;
/*  80 */     if (hasSecurityManager()) {
/*     */       try
/*     */       {
/*  83 */         PrivilegedAction local1 = new PrivilegedAction() {
/*     */           public Object run() {
/*     */             try {
/*  86 */               return System.getProperty(this.val$propertyName); } catch (Throwable localThrowable) {
/*     */             }
/*  88 */             return null;
/*     */           }
/*     */         };
/*  92 */         str = (String)AccessController.doPrivileged(local1);
/*     */       }
/*     */       catch (Exception localException) {
/*  95 */         str = System.getProperty(paramString);
/*     */       }
/*     */     }
/*     */     else {
/*  99 */       str = System.getProperty(paramString);
/*     */     }
/* 101 */     return str;
/*     */   }
/*     */ 
/*     */   static void loadProperties(Properties paramProperties, final String paramString)
/*     */   {
/* 118 */     if (hasSecurityManager()) {
/*     */       try
/*     */       {
/* 121 */         PrivilegedAction local2 = new PrivilegedAction() {
/*     */           public Object run() {
/* 123 */             JSSecurityManager.loadPropertiesImpl(this.val$properties, paramString);
/* 124 */             return null;
/*     */           }
/*     */         };
/* 127 */         AccessController.doPrivileged(local2);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 132 */         loadPropertiesImpl(paramProperties, paramString);
/*     */       }
/*     */     }
/*     */     else
/* 136 */       loadPropertiesImpl(paramProperties, paramString);
/*     */   }
/*     */ 
/*     */   private static void loadPropertiesImpl(Properties paramProperties, String paramString)
/*     */   {
/* 144 */     String str = System.getProperty("java.home");
/*     */     try {
/* 146 */       if (str == null) {
/* 147 */         throw new Error("Can't find java.home ??");
/*     */       }
/* 149 */       File localFile = new File(str, "lib");
/* 150 */       localFile = new File(localFile, paramString);
/* 151 */       str = localFile.getCanonicalPath();
/* 152 */       FileInputStream localFileInputStream = new FileInputStream(str);
/* 153 */       BufferedInputStream localBufferedInputStream = new BufferedInputStream(localFileInputStream);
/*     */       try {
/* 155 */         paramProperties.load(localBufferedInputStream);
/*     */       } finally {
/* 157 */         if (localFileInputStream != null)
/* 158 */           localFileInputStream.close();
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   static Thread createThread(Runnable paramRunnable, String paramString, boolean paramBoolean1, int paramInt, boolean paramBoolean2)
/*     */   {
/* 176 */     Thread localThread = new Thread(paramRunnable);
/* 177 */     if (paramString != null) {
/* 178 */       localThread.setName(paramString);
/*     */     }
/* 180 */     localThread.setDaemon(paramBoolean1);
/* 181 */     if (paramInt >= 0) {
/* 182 */       localThread.setPriority(paramInt);
/*     */     }
/* 184 */     if (paramBoolean2) {
/* 185 */       localThread.start();
/*     */     }
/* 187 */     return localThread;
/*     */   }
/*     */ 
/*     */   static synchronized List getProviders(Class paramClass) {
/* 191 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 194 */     Iterator localIterator = Service.providers(paramClass);
/*     */ 
/* 198 */     PrivilegedAction local3 = new PrivilegedAction() {
/*     */       public Boolean run() {
/* 200 */         return Boolean.valueOf(this.val$ps.hasNext());
/*     */       }
/*     */     };
/* 204 */     while (((Boolean)AccessController.doPrivileged(local3)).booleanValue())
/*     */     {
/*     */       try
/*     */       {
/* 209 */         Object localObject = localIterator.next();
/* 210 */         if (paramClass.isInstance(localObject))
/*     */         {
/* 215 */           localArrayList.add(0, localObject);
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/*     */       }
/*     */     }
/* 222 */     return localArrayList;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.JSSecurityManager
 * JD-Core Version:    0.6.2
 */