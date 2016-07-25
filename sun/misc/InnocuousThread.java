/*    */ package sun.misc;
/*    */ 
/*    */ import java.security.AccessControlContext;
/*    */ import java.security.ProtectionDomain;
/*    */ 
/*    */ public final class InnocuousThread extends Thread
/*    */ {
/*    */   private static final Unsafe UNSAFE;
/*    */   private static final ThreadGroup THREADGROUP;
/*    */   private static final AccessControlContext ACC;
/*    */   private static final long THREADLOCALS;
/*    */   private static final long INHERITABLETHREADLOCALS;
/*    */   private static final long INHERITEDACCESSCONTROLCONTEXT;
/*    */   private volatile boolean hasRun;
/*    */ 
/*    */   public InnocuousThread(Runnable paramRunnable)
/*    */   {
/* 46 */     super(THREADGROUP, paramRunnable, "anInnocuousThread");
/* 47 */     UNSAFE.putOrderedObject(this, INHERITEDACCESSCONTROLCONTEXT, ACC);
/* 48 */     eraseThreadLocals();
/*    */   }
/*    */ 
/*    */   public ClassLoader getContextClassLoader()
/*    */   {
/* 54 */     return ClassLoader.getSystemClassLoader();
/*    */   }
/*    */ 
/*    */   public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler paramUncaughtExceptionHandler)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void setContextClassLoader(ClassLoader paramClassLoader)
/*    */   {
/* 64 */     throw new SecurityException("setContextClassLoader");
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 72 */     if ((Thread.currentThread() == this) && (!this.hasRun)) {
/* 73 */       this.hasRun = true;
/* 74 */       super.run();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void eraseThreadLocals()
/*    */   {
/* 82 */     UNSAFE.putObject(this, THREADLOCALS, null);
/* 83 */     UNSAFE.putObject(this, INHERITABLETHREADLOCALS, null);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     try {
/* 89 */       ACC = new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, null) });
/*    */ 
/* 94 */       UNSAFE = Unsafe.getUnsafe();
/* 95 */       Thread localThread = Thread.class;
/* 96 */       ThreadGroup localThreadGroup1 = ThreadGroup.class;
/*    */ 
/* 98 */       THREADLOCALS = UNSAFE.objectFieldOffset(localThread.getDeclaredField("threadLocals"));
/*    */ 
/* 100 */       INHERITABLETHREADLOCALS = UNSAFE.objectFieldOffset(localThread.getDeclaredField("inheritableThreadLocals"));
/*    */ 
/* 102 */       INHERITEDACCESSCONTROLCONTEXT = UNSAFE.objectFieldOffset(localThread.getDeclaredField("inheritedAccessControlContext"));
/*    */ 
/* 105 */       long l1 = UNSAFE.objectFieldOffset(localThread.getDeclaredField("group"));
/* 106 */       long l2 = UNSAFE.objectFieldOffset(localThreadGroup1.getDeclaredField("parent"));
/* 107 */       Object localObject = (ThreadGroup)UNSAFE.getObject(Thread.currentThread(), l1);
/*    */ 
/* 110 */       while (localObject != null) {
/* 111 */         ThreadGroup localThreadGroup2 = (ThreadGroup)UNSAFE.getObject(localObject, l2);
/* 112 */         if (localThreadGroup2 == null)
/*    */           break;
/* 114 */         localObject = localThreadGroup2;
/*    */       }
/* 116 */       THREADGROUP = new ThreadGroup((ThreadGroup)localObject, "InnocuousThreadGroup");
/*    */     } catch (Exception localException) {
/* 118 */       throw new Error(localException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.InnocuousThread
 * JD-Core Version:    0.6.2
 */