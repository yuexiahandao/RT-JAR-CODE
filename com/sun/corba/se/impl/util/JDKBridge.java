/*     */ package com.sun.corba.se.impl.util;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.GetPropertyAction;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.rmi.server.RMIClassLoader;
/*     */ import java.security.AccessController;
/*     */ 
/*     */ public class JDKBridge
/*     */ {
/*     */   private static final String LOCAL_CODEBASE_KEY = "java.rmi.server.codebase";
/*     */   private static final String USE_CODEBASE_ONLY_KEY = "java.rmi.server.useCodebaseOnly";
/* 123 */   private static String localCodebase = null;
/*     */   private static boolean useCodebaseOnly;
/*     */ 
/*     */   public static String getLocalCodebase()
/*     */   {
/*  59 */     return localCodebase;
/*     */   }
/*     */ 
/*     */   public static boolean useCodebaseOnly()
/*     */   {
/*  67 */     return useCodebaseOnly;
/*     */   }
/*     */ 
/*     */   public static Class loadClass(String paramString1, String paramString2, ClassLoader paramClassLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/*  85 */     if (paramClassLoader == null)
/*  86 */       return loadClassM(paramString1, paramString2, useCodebaseOnly);
/*     */     try
/*     */     {
/*  89 */       return loadClassM(paramString1, paramString2, useCodebaseOnly); } catch (ClassNotFoundException localClassNotFoundException) {
/*     */     }
/*  91 */     return paramClassLoader.loadClass(paramString1);
/*     */   }
/*     */ 
/*     */   public static Class loadClass(String paramString1, String paramString2)
/*     */     throws ClassNotFoundException
/*     */   {
/* 107 */     return loadClass(paramString1, paramString2, null);
/*     */   }
/*     */ 
/*     */   public static Class loadClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 118 */     return loadClass(paramString, null, null);
/*     */   }
/*     */ 
/*     */   public static final void main(String[] paramArrayOfString)
/*     */   {
/* 131 */     System.out.println("1.2 VM");
/*     */   }
/*     */ 
/*     */   public static synchronized void setCodebaseProperties()
/*     */   {
/* 153 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("java.rmi.server.codebase"));
/*     */ 
/* 156 */     if ((str != null) && (str.trim().length() > 0)) {
/* 157 */       localCodebase = str;
/*     */     }
/*     */ 
/* 160 */     str = (String)AccessController.doPrivileged(new GetPropertyAction("java.rmi.server.useCodebaseOnly"));
/*     */ 
/* 163 */     if ((str != null) && (str.trim().length() > 0))
/* 164 */       useCodebaseOnly = Boolean.valueOf(str).booleanValue();
/*     */   }
/*     */ 
/*     */   public static synchronized void setLocalCodebase(String paramString)
/*     */   {
/* 173 */     localCodebase = paramString;
/*     */   }
/*     */ 
/*     */   private static Class loadClassM(String paramString1, String paramString2, boolean paramBoolean)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 182 */       return JDKClassLoader.loadClass(null, paramString1);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*     */       try {
/* 185 */         if ((!paramBoolean) && (paramString2 != null)) {
/* 186 */           return RMIClassLoader.loadClass(paramString2, paramString1);
/*     */         }
/*     */ 
/* 189 */         return RMIClassLoader.loadClass(paramString1);
/*     */       }
/*     */       catch (MalformedURLException localMalformedURLException) {
/* 192 */         paramString1 = paramString1 + ": " + localMalformedURLException.toString();
/*     */       }
/*     */     }
/* 195 */     throw new ClassNotFoundException(paramString1);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 127 */     setCodebaseProperties();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.util.JDKBridge
 * JD-Core Version:    0.6.2
 */