/*     */ package java.lang;
/*     */ 
/*     */ class Shutdown
/*     */ {
/*     */   private static final int RUNNING = 0;
/*     */   private static final int HOOKS = 1;
/*     */   private static final int FINALIZERS = 2;
/*  43 */   private static int state = 0;
/*     */ 
/*  46 */   private static boolean runFinalizersOnExit = false;
/*     */   private static final int MAX_SYSTEM_HOOKS = 10;
/*  54 */   private static final Runnable[] hooks = new Runnable[10];
/*     */ 
/*  57 */   private static int currentRunningHook = 0;
/*     */ 
/*  61 */   private static Object lock = new Lock(null);
/*     */ 
/*  64 */   private static Object haltLock = new Lock(null);
/*     */ 
/*     */   static void setRunFinalizersOnExit(boolean paramBoolean)
/*     */   {
/*  68 */     synchronized (lock) {
/*  69 */       runFinalizersOnExit = paramBoolean;
/*     */     }
/*     */   }
/*     */ 
/*     */   static void add(int paramInt, boolean paramBoolean, Runnable paramRunnable)
/*     */   {
/*  95 */     synchronized (lock) {
/*  96 */       if (hooks[paramInt] != null) {
/*  97 */         throw new InternalError("Shutdown hook at slot " + paramInt + " already registered");
/*     */       }
/*  99 */       if (!paramBoolean) {
/* 100 */         if (state > 0)
/* 101 */           throw new IllegalStateException("Shutdown in progress");
/*     */       }
/* 103 */       else if ((state > 1) || ((state == 1) && (paramInt <= currentRunningHook))) {
/* 104 */         throw new IllegalStateException("Shutdown in progress");
/*     */       }
/*     */ 
/* 107 */       hooks[paramInt] = paramRunnable;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void runHooks()
/*     */   {
/* 114 */     for (int i = 0; i < 10; i++)
/*     */       try
/*     */       {
/*     */         Runnable localRunnable;
/* 117 */         synchronized (lock)
/*     */         {
/* 120 */           currentRunningHook = i;
/* 121 */           localRunnable = hooks[i];
/*     */         }
/* 123 */         if (localRunnable != null) localRunnable.run(); 
/*     */       }
/* 125 */       catch (Throwable localThrowable) { if ((localThrowable instanceof ThreadDeath)) {
/* 126 */           ??? = (ThreadDeath)localThrowable;
/* 127 */           throw ((Throwable)???);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   static void halt(int paramInt)
/*     */   {
/* 138 */     synchronized (haltLock) {
/* 139 */       halt0(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   static native void halt0(int paramInt);
/*     */ 
/*     */   private static native void runAllFinalizers();
/*     */ 
/*     */   private static void sequence()
/*     */   {
/* 161 */     synchronized (lock)
/*     */     {
/* 165 */       if (state != 1) return;
/*     */     }
/* 167 */     runHooks();
/*     */     boolean bool;
/* 169 */     synchronized (lock) {
/* 170 */       state = 2;
/* 171 */       bool = runFinalizersOnExit;
/*     */     }
/* 173 */     if (bool) runAllFinalizers();
/*     */   }
/*     */ 
/*     */   static void exit(int paramInt)
/*     */   {
/* 182 */     boolean bool = false;
/* 183 */     synchronized (lock) {
/* 184 */       if (paramInt != 0) runFinalizersOnExit = false;
/* 185 */       switch (state) {
/*     */       case 0:
/* 187 */         state = 1;
/* 188 */         break;
/*     */       case 1:
/* 190 */         break;
/*     */       case 2:
/* 192 */         if (paramInt != 0)
/*     */         {
/* 194 */           halt(paramInt);
/*     */         }
/*     */         else
/*     */         {
/* 199 */           bool = runFinalizersOnExit;
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 204 */     if (bool) {
/* 205 */       runAllFinalizers();
/* 206 */       halt(paramInt);
/*     */     }
/* 208 */     synchronized (Shutdown.class)
/*     */     {
/* 212 */       sequence();
/* 213 */       halt(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void shutdown()
/*     */   {
/* 223 */     synchronized (lock) {
/* 224 */       switch (state) {
/*     */       case 0:
/* 226 */         state = 1;
/*     */       case 1:
/*     */       case 2:
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 233 */     synchronized (Shutdown.class) {
/* 234 */       sequence();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Lock
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Shutdown
 * JD-Core Version:    0.6.2
 */