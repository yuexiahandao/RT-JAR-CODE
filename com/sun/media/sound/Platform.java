/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ final class Platform
/*     */ {
/*     */   private static final String libNameMain = "jsound";
/*     */   private static final String libNameALSA = "jsoundalsa";
/*     */   private static final String libNameDSound = "jsoundds";
/*     */   public static final int LIB_MAIN = 1;
/*     */   public static final int LIB_ALSA = 2;
/*     */   public static final int LIB_DSOUND = 4;
/*  56 */   private static int loadedLibs = 0;
/*     */   public static final int FEATURE_MIDIIO = 1;
/*     */   public static final int FEATURE_PORTS = 2;
/*     */   public static final int FEATURE_DIRECT_AUDIO = 3;
/*     */   private static boolean signed8;
/*     */   private static boolean bigEndian;
/*     */   private static String javahome;
/*     */   private static String classpath;
/*     */ 
/*     */   static void initialize()
/*     */   {
/*     */   }
/*     */ 
/*     */   static boolean isBigEndian()
/*     */   {
/* 120 */     return bigEndian;
/*     */   }
/*     */ 
/*     */   static boolean isSigned8()
/*     */   {
/* 129 */     return signed8;
/*     */   }
/*     */ 
/*     */   static String getJavahome()
/*     */   {
/* 139 */     return javahome;
/*     */   }
/*     */ 
/*     */   static String getClasspath()
/*     */   {
/* 148 */     return classpath;
/*     */   }
/*     */ 
/*     */   private static void loadLibraries()
/*     */   {
/*     */     try
/*     */     {
/* 162 */       AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Void run() {
/* 165 */           System.loadLibrary("jsound");
/* 166 */           return null;
/*     */         }
/*     */       });
/* 170 */       loadedLibs |= 1;
/*     */     }
/*     */     catch (SecurityException localSecurityException) {
/* 173 */       throw localSecurityException;
/*     */     }
/*     */ 
/* 178 */     String str1 = nGetExtraLibraries();
/*     */ 
/* 180 */     StringTokenizer localStringTokenizer = new StringTokenizer(str1);
/* 181 */     while (localStringTokenizer.hasMoreTokens()) {
/* 182 */       String str2 = localStringTokenizer.nextToken();
/*     */       try {
/* 184 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Void run() {
/* 187 */             System.loadLibrary(this.val$lib);
/* 188 */             return null;
/*     */           }
/*     */         });
/* 192 */         if (str2.equals("jsoundalsa")) {
/* 193 */           loadedLibs |= 2;
/*     */         }
/* 195 */         else if (str2.equals("jsoundds"))
/* 196 */           loadedLibs |= 4;
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static boolean isMidiIOEnabled()
/*     */   {
/* 209 */     return isFeatureLibLoaded(1);
/*     */   }
/*     */ 
/*     */   static boolean isPortsEnabled() {
/* 213 */     return isFeatureLibLoaded(2);
/*     */   }
/*     */ 
/*     */   static boolean isDirectAudioEnabled() {
/* 217 */     return isFeatureLibLoaded(3);
/*     */   }
/*     */ 
/*     */   private static boolean isFeatureLibLoaded(int paramInt)
/*     */   {
/* 222 */     int i = nGetLibraryForFeature(paramInt);
/* 223 */     boolean bool = (i != 0) && ((loadedLibs & i) == i);
/*     */ 
/* 225 */     return bool;
/*     */   }
/*     */ 
/*     */   private static native boolean nIsBigEndian();
/*     */ 
/*     */   private static native boolean nIsSigned8();
/*     */ 
/*     */   private static native String nGetExtraLibraries();
/*     */ 
/*     */   private static native int nGetLibraryForFeature(int paramInt);
/*     */ 
/*     */   private static void readProperties()
/*     */   {
/* 240 */     bigEndian = nIsBigEndian();
/* 241 */     signed8 = nIsSigned8();
/* 242 */     javahome = JSSecurityManager.getProperty("java.home");
/* 243 */     classpath = JSSecurityManager.getProperty("java.class.path");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  91 */     loadLibraries();
/*  92 */     readProperties();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.Platform
 * JD-Core Version:    0.6.2
 */