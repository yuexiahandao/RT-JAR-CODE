/*     */ package sun.rmi.runtime;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.logging.Level;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ 
/*     */ public final class RuntimeUtil
/*     */ {
/*  52 */   private static final Log runtimeLog = Log.getLog("sun.rmi.runtime", null, false);
/*     */ 
/*  56 */   private static final int schedulerThreads = ((Integer)AccessController.doPrivileged(new GetIntegerAction("sun.rmi.runtime.schedulerThreads", 1))).intValue();
/*     */ 
/*  61 */   private static final Permission GET_INSTANCE_PERMISSION = new RuntimePermission("sun.rmi.runtime.RuntimeUtil.getInstance");
/*     */ 
/*  65 */   private static final RuntimeUtil instance = new RuntimeUtil();
/*     */   private final ScheduledThreadPoolExecutor scheduler;
/*     */ 
/*     */   private RuntimeUtil()
/*     */   {
/*  71 */     this.scheduler = new ScheduledThreadPoolExecutor(schedulerThreads, new ThreadFactory()
/*     */     {
/*  74 */       private final AtomicInteger count = new AtomicInteger(0);
/*     */ 
/*     */       public Thread newThread(Runnable paramAnonymousRunnable) {
/*     */         try { return (Thread)AccessController.doPrivileged(new NewThreadAction(paramAnonymousRunnable, "Scheduler(" + this.count.getAndIncrement() + ")", true)); }
/*     */         catch (Throwable localThrowable)
/*     */         {
/*  82 */           RuntimeUtil.runtimeLog.log(Level.WARNING, "scheduler thread factory throws", localThrowable);
/*     */         }
/*  84 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static RuntimeUtil getInstance()
/*     */   {
/* 115 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 116 */     if (localSecurityManager != null) {
/* 117 */       localSecurityManager.checkPermission(GET_INSTANCE_PERMISSION);
/*     */     }
/* 119 */     return instance;
/*     */   }
/*     */ 
/*     */   public ScheduledThreadPoolExecutor getScheduler()
/*     */   {
/* 129 */     return this.scheduler;
/*     */   }
/*     */ 
/*     */   public static class GetInstanceAction
/*     */     implements PrivilegedAction<RuntimeUtil>
/*     */   {
/*     */     public RuntimeUtil run()
/*     */     {
/* 110 */       return RuntimeUtil.access$100();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.runtime.RuntimeUtil
 * JD-Core Version:    0.6.2
 */