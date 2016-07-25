/*     */ package java.lang;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;
/*     */ 
/*     */ public class Runtime
/*     */ {
/*  47 */   private static Runtime currentRuntime = new Runtime();
/*     */ 
/*     */   public static Runtime getRuntime()
/*     */   {
/*  58 */     return currentRuntime;
/*     */   }
/*     */ 
/*     */   public void exit(int paramInt)
/*     */   {
/* 105 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 106 */     if (localSecurityManager != null) {
/* 107 */       localSecurityManager.checkExit(paramInt);
/*     */     }
/* 109 */     Shutdown.exit(paramInt);
/*     */   }
/*     */ 
/*     */   public void addShutdownHook(Thread paramThread)
/*     */   {
/* 207 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 208 */     if (localSecurityManager != null) {
/* 209 */       localSecurityManager.checkPermission(new RuntimePermission("shutdownHooks"));
/*     */     }
/* 211 */     ApplicationShutdownHooks.add(paramThread);
/*     */   }
/*     */ 
/*     */   public boolean removeShutdownHook(Thread paramThread)
/*     */   {
/* 235 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 236 */     if (localSecurityManager != null) {
/* 237 */       localSecurityManager.checkPermission(new RuntimePermission("shutdownHooks"));
/*     */     }
/* 239 */     return ApplicationShutdownHooks.remove(paramThread);
/*     */   }
/*     */ 
/*     */   public void halt(int paramInt)
/*     */   {
/* 271 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 272 */     if (localSecurityManager != null) {
/* 273 */       localSecurityManager.checkExit(paramInt);
/*     */     }
/* 275 */     Shutdown.halt(paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static void runFinalizersOnExit(boolean paramBoolean)
/*     */   {
/* 306 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 307 */     if (localSecurityManager != null) {
/*     */       try {
/* 309 */         localSecurityManager.checkExit(0);
/*     */       } catch (SecurityException localSecurityException) {
/* 311 */         throw new SecurityException("runFinalizersOnExit");
/*     */       }
/*     */     }
/* 314 */     Shutdown.setRunFinalizersOnExit(paramBoolean);
/*     */   }
/*     */ 
/*     */   public Process exec(String paramString)
/*     */     throws IOException
/*     */   {
/* 347 */     return exec(paramString, null, null);
/*     */   }
/*     */ 
/*     */   public Process exec(String paramString, String[] paramArrayOfString)
/*     */     throws IOException
/*     */   {
/* 388 */     return exec(paramString, paramArrayOfString, null);
/*     */   }
/*     */ 
/*     */   public Process exec(String paramString, String[] paramArrayOfString, File paramFile)
/*     */     throws IOException
/*     */   {
/* 443 */     if (paramString.length() == 0) {
/* 444 */       throw new IllegalArgumentException("Empty command");
/*     */     }
/* 446 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
/* 447 */     String[] arrayOfString = new String[localStringTokenizer.countTokens()];
/* 448 */     for (int i = 0; localStringTokenizer.hasMoreTokens(); i++)
/* 449 */       arrayOfString[i] = localStringTokenizer.nextToken();
/* 450 */     return exec(arrayOfString, paramArrayOfString, paramFile);
/*     */   }
/*     */ 
/*     */   public Process exec(String[] paramArrayOfString)
/*     */     throws IOException
/*     */   {
/* 485 */     return exec(paramArrayOfString, null, null);
/*     */   }
/*     */ 
/*     */   public Process exec(String[] paramArrayOfString1, String[] paramArrayOfString2)
/*     */     throws IOException
/*     */   {
/* 528 */     return exec(paramArrayOfString1, paramArrayOfString2, null);
/*     */   }
/*     */ 
/*     */   public Process exec(String[] paramArrayOfString1, String[] paramArrayOfString2, File paramFile)
/*     */     throws IOException
/*     */   {
/* 617 */     return new ProcessBuilder(paramArrayOfString1).environment(paramArrayOfString2).directory(paramFile).start();
/*     */   }
/*     */ 
/*     */   public native int availableProcessors();
/*     */ 
/*     */   public native long freeMemory();
/*     */ 
/*     */   public native long totalMemory();
/*     */ 
/*     */   public native long maxMemory();
/*     */ 
/*     */   public native void gc();
/*     */ 
/*     */   private static native void runFinalization0();
/*     */ 
/*     */   public void runFinalization()
/*     */   {
/* 712 */     runFinalization0();
/*     */   }
/*     */ 
/*     */   public native void traceInstructions(boolean paramBoolean);
/*     */ 
/*     */   public native void traceMethodCalls(boolean paramBoolean);
/*     */ 
/*     */   @CallerSensitive
/*     */   public void load(String paramString)
/*     */   {
/* 783 */     load0(Reflection.getCallerClass(), paramString);
/*     */   }
/*     */ 
/*     */   synchronized void load0(Class paramClass, String paramString) {
/* 787 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 788 */     if (localSecurityManager != null) {
/* 789 */       localSecurityManager.checkLink(paramString);
/*     */     }
/* 791 */     if (!new File(paramString).isAbsolute()) {
/* 792 */       throw new UnsatisfiedLinkError("Expecting an absolute path of the library: " + paramString);
/*     */     }
/*     */ 
/* 795 */     ClassLoader.loadLibrary(paramClass, paramString, true);
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public void loadLibrary(String paramString)
/*     */   {
/* 837 */     loadLibrary0(Reflection.getCallerClass(), paramString);
/*     */   }
/*     */ 
/*     */   synchronized void loadLibrary0(Class paramClass, String paramString) {
/* 841 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 842 */     if (localSecurityManager != null) {
/* 843 */       localSecurityManager.checkLink(paramString);
/*     */     }
/* 845 */     if (paramString.indexOf(File.separatorChar) != -1) {
/* 846 */       throw new UnsatisfiedLinkError("Directory separator should not appear in library name: " + paramString);
/*     */     }
/*     */ 
/* 849 */     ClassLoader.loadLibrary(paramClass, paramString, false);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public InputStream getLocalizedInputStream(InputStream paramInputStream)
/*     */   {
/* 875 */     return paramInputStream;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public OutputStream getLocalizedOutputStream(OutputStream paramOutputStream)
/*     */   {
/* 903 */     return paramOutputStream;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Runtime
 * JD-Core Version:    0.6.2
 */