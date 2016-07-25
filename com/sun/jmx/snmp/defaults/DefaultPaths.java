/*     */ package com.sun.jmx.snmp.defaults;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ 
/*     */ public class DefaultPaths
/*     */ {
/*     */   private static final String INSTALL_PATH_RESOURCE_NAME = "com/sun/jdmk/defaults/install.path";
/*     */   private static String etcDir;
/*     */   private static String tmpDir;
/*     */   private static String installDir;
/*     */ 
/*     */   public static String getInstallDir()
/*     */   {
/*  63 */     if (installDir == null) {
/*  64 */       return useRessourceFile();
/*     */     }
/*  66 */     return installDir;
/*     */   }
/*     */ 
/*     */   public static String getInstallDir(String paramString)
/*     */   {
/*  80 */     if (installDir == null) {
/*  81 */       if (paramString == null) {
/*  82 */         return getInstallDir();
/*     */       }
/*  84 */       return getInstallDir() + File.separator + paramString;
/*     */     }
/*     */ 
/*  87 */     if (paramString == null) {
/*  88 */       return installDir;
/*     */     }
/*  90 */     return installDir + File.separator + paramString;
/*     */   }
/*     */ 
/*     */   public static void setInstallDir(String paramString)
/*     */   {
/* 101 */     installDir = paramString;
/*     */   }
/*     */ 
/*     */   public static String getEtcDir()
/*     */   {
/* 115 */     if (etcDir == null) {
/* 116 */       return getInstallDir("etc");
/*     */     }
/* 118 */     return etcDir;
/*     */   }
/*     */ 
/*     */   public static String getEtcDir(String paramString)
/*     */   {
/* 134 */     if (etcDir == null) {
/* 135 */       if (paramString == null) {
/* 136 */         return getEtcDir();
/*     */       }
/* 138 */       return getEtcDir() + File.separator + paramString;
/*     */     }
/*     */ 
/* 141 */     if (paramString == null) {
/* 142 */       return etcDir;
/*     */     }
/* 144 */     return etcDir + File.separator + paramString;
/*     */   }
/*     */ 
/*     */   public static void setEtcDir(String paramString)
/*     */   {
/* 155 */     etcDir = paramString;
/*     */   }
/*     */ 
/*     */   public static String getTmpDir()
/*     */   {
/* 169 */     if (tmpDir == null) {
/* 170 */       return getInstallDir("tmp");
/*     */     }
/* 172 */     return tmpDir;
/*     */   }
/*     */ 
/*     */   public static String getTmpDir(String paramString)
/*     */   {
/* 188 */     if (tmpDir == null) {
/* 189 */       if (paramString == null) {
/* 190 */         return getTmpDir();
/*     */       }
/* 192 */       return getTmpDir() + File.separator + paramString;
/*     */     }
/*     */ 
/* 195 */     if (paramString == null) {
/* 196 */       return tmpDir;
/*     */     }
/* 198 */     return tmpDir + File.separator + paramString;
/*     */   }
/*     */ 
/*     */   public static void setTmpDir(String paramString)
/*     */   {
/* 209 */     tmpDir = paramString;
/*     */   }
/*     */ 
/*     */   private static String useRessourceFile()
/*     */   {
/* 217 */     InputStream localInputStream = null;
/* 218 */     BufferedReader localBufferedReader = null;
/*     */     try {
/* 220 */       localInputStream = DefaultPaths.class.getClassLoader().getResourceAsStream("com/sun/jdmk/defaults/install.path");
/*     */ 
/* 222 */       if (localInputStream == null) return null;
/* 223 */       localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
/* 224 */       installDir = localBufferedReader.readLine();
/*     */     } catch (Exception localException2) {
/*     */     }
/*     */     finally {
/*     */       try {
/* 229 */         if (localInputStream != null) localInputStream.close();
/* 230 */         if (localBufferedReader != null) localBufferedReader.close(); 
/*     */       } catch (Exception localException5) {  }
/*     */ 
/*     */     }
/* 233 */     return installDir;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.defaults.DefaultPaths
 * JD-Core Version:    0.6.2
 */