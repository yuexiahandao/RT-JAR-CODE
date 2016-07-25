/*     */ package sun.misc;
/*     */ 
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class VM
/*     */ {
/*  43 */   private static boolean suspended = false;
/*     */ 
/*     */   @Deprecated
/*     */   public static final int STATE_GREEN = 1;
/*     */ 
/*     */   @Deprecated
/*     */   public static final int STATE_YELLOW = 2;
/*     */ 
/*     */   @Deprecated
/*     */   public static final int STATE_RED = 3;
/* 149 */   private static volatile boolean booted = false;
/* 150 */   private static final Object lock = new Object();
/*     */ 
/* 187 */   private static long directMemory = 67108864L;
/*     */   private static boolean pageAlignDirectMemory;
/* 217 */   private static boolean defaultAllowArraySyntax = false;
/* 218 */   private static boolean allowArraySyntax = defaultAllowArraySyntax;
/*     */ 
/* 234 */   private static boolean allowGetCallerClass = true;
/*     */ 
/* 267 */   private static final Properties savedProps = new Properties();
/*     */ 
/* 339 */   private static volatile int finalRefCount = 0;
/*     */ 
/* 342 */   private static volatile int peakFinalRefCount = 0;
/*     */   private static final int JVMTI_THREAD_STATE_ALIVE = 1;
/*     */   private static final int JVMTI_THREAD_STATE_TERMINATED = 2;
/*     */   private static final int JVMTI_THREAD_STATE_RUNNABLE = 4;
/*     */   private static final int JVMTI_THREAD_STATE_BLOCKED_ON_MONITOR_ENTER = 1024;
/*     */   private static final int JVMTI_THREAD_STATE_WAITING_INDEFINITELY = 16;
/*     */   private static final int JVMTI_THREAD_STATE_WAITING_WITH_TIMEOUT = 32;
/*     */ 
/*  48 */   @Deprecated
/*     */   public static boolean threadsSuspended() { return suspended; }
/*     */ 
/*     */   public static boolean allowThreadSuspension(ThreadGroup paramThreadGroup, boolean paramBoolean)
/*     */   {
/*  52 */     return paramThreadGroup.allowThreadSuspension(paramBoolean);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static boolean suspendThreads()
/*     */   {
/*  58 */     suspended = true;
/*  59 */     return true;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static void unsuspendThreads()
/*     */   {
/*  66 */     suspended = false;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static void unsuspendSomeThreads()
/*     */   {
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static final int getState()
/*     */   {
/*  92 */     return 1;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static void registerVMNotification(VMNotification paramVMNotification)
/*     */   {
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static void asChange(int paramInt1, int paramInt2)
/*     */   {
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static void asChange_otherthread(int paramInt1, int paramInt2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static void booted()
/*     */   {
/* 158 */     synchronized (lock) {
/* 159 */       booted = true;
/* 160 */       lock.notifyAll();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean isBooted() {
/* 165 */     return booted;
/*     */   }
/*     */ 
/*     */   public static void awaitBooted()
/*     */     throws InterruptedException
/*     */   {
/* 172 */     synchronized (lock) {
/* 173 */       while (!booted)
/* 174 */         lock.wait();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static long maxDirectMemory()
/*     */   {
/* 194 */     return directMemory;
/*     */   }
/*     */ 
/*     */   public static boolean isDirectMemoryPageAligned()
/*     */   {
/* 205 */     return pageAlignDirectMemory;
/*     */   }
/*     */ 
/*     */   public static boolean allowArraySyntax()
/*     */   {
/* 231 */     return allowArraySyntax;
/*     */   }
/*     */ 
/*     */   public static boolean allowGetCallerClass()
/*     */   {
/* 242 */     return allowGetCallerClass;
/*     */   }
/*     */ 
/*     */   public static String getSavedProperty(String paramString)
/*     */   {
/* 258 */     if (savedProps.isEmpty()) {
/* 259 */       throw new IllegalStateException("Should be non-empty if initialized");
/*     */     }
/* 261 */     return savedProps.getProperty(paramString);
/*     */   }
/*     */ 
/*     */   public static void saveAndRemoveProperties(Properties paramProperties)
/*     */   {
/* 274 */     if (booted) {
/* 275 */       throw new IllegalStateException("System initialization has completed");
/*     */     }
/* 277 */     savedProps.putAll(paramProperties);
/*     */ 
/* 284 */     String str = (String)paramProperties.remove("sun.nio.MaxDirectMemorySize");
/* 285 */     if (str != null) {
/* 286 */       if (str.equals("-1"))
/*     */       {
/* 288 */         directMemory = Runtime.getRuntime().maxMemory();
/*     */       } else {
/* 290 */         long l = Long.parseLong(str);
/* 291 */         if (l > -1L) {
/* 292 */           directMemory = l;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 297 */     str = (String)paramProperties.remove("sun.nio.PageAlignDirectMemory");
/* 298 */     if ("true".equals(str)) {
/* 299 */       pageAlignDirectMemory = true;
/*     */     }
/*     */ 
/* 304 */     str = paramProperties.getProperty("sun.lang.ClassLoader.allowArraySyntax");
/* 305 */     allowArraySyntax = str == null ? defaultAllowArraySyntax : Boolean.parseBoolean(str);
/*     */ 
/* 312 */     str = paramProperties.getProperty("jdk.reflect.allowGetCallerClass");
/* 313 */     allowGetCallerClass = (str == null) || (str.isEmpty()) || (Boolean.parseBoolean(str)) || (Boolean.valueOf(paramProperties.getProperty("jdk.logging.allowStackWalkSearch")).booleanValue());
/*     */ 
/* 320 */     paramProperties.remove("java.lang.Integer.IntegerCache.high");
/*     */ 
/* 323 */     paramProperties.remove("sun.zip.disableMemoryMapping");
/*     */ 
/* 326 */     paramProperties.remove("sun.java.launcher.diag");
/*     */   }
/*     */ 
/*     */   public static void initializeOSEnvironment()
/*     */   {
/* 333 */     if (!booted)
/* 334 */       OSEnvironment.initialize();
/*     */   }
/*     */ 
/*     */   public static int getFinalRefCount()
/*     */   {
/* 350 */     return finalRefCount;
/*     */   }
/*     */ 
/*     */   public static int getPeakFinalRefCount()
/*     */   {
/* 359 */     return peakFinalRefCount;
/*     */   }
/*     */ 
/*     */   public static void addFinalRefCount(int paramInt)
/*     */   {
/* 371 */     finalRefCount += paramInt;
/* 372 */     if (finalRefCount > peakFinalRefCount)
/* 373 */       peakFinalRefCount = finalRefCount;
/*     */   }
/*     */ 
/*     */   public static Thread.State toThreadState(int paramInt)
/*     */   {
/* 381 */     if ((paramInt & 0x4) != 0)
/* 382 */       return Thread.State.RUNNABLE;
/* 383 */     if ((paramInt & 0x400) != 0)
/* 384 */       return Thread.State.BLOCKED;
/* 385 */     if ((paramInt & 0x10) != 0)
/* 386 */       return Thread.State.WAITING;
/* 387 */     if ((paramInt & 0x20) != 0)
/* 388 */       return Thread.State.TIMED_WAITING;
/* 389 */     if ((paramInt & 0x2) != 0)
/* 390 */       return Thread.State.TERMINATED;
/* 391 */     if ((paramInt & 0x1) == 0) {
/* 392 */       return Thread.State.NEW;
/*     */     }
/* 394 */     return Thread.State.RUNNABLE;
/*     */   }
/*     */ 
/*     */   public static native ClassLoader latestUserDefinedLoader();
/*     */ 
/*     */   private static native void initialize();
/*     */ 
/*     */   static
/*     */   {
/* 416 */     initialize();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.VM
 * JD-Core Version:    0.6.2
 */