/*     */ package com.sun.xml.internal.ws.api.pipe;
/*     */ 
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ public class Engine
/*     */ {
/*     */   private volatile Executor threadPool;
/*     */   public final String id;
/*     */ 
/*     */   public Engine(String id, Executor threadPool)
/*     */   {
/*  47 */     this(id);
/*  48 */     this.threadPool = threadPool;
/*     */   }
/*     */ 
/*     */   public Engine(String id) {
/*  52 */     this.id = id;
/*     */   }
/*     */ 
/*     */   public void setExecutor(Executor threadPool) {
/*  56 */     this.threadPool = threadPool;
/*     */   }
/*     */ 
/*     */   void addRunnable(Fiber fiber) {
/*  60 */     if (this.threadPool == null) {
/*  61 */       synchronized (this) {
/*  62 */         this.threadPool = Executors.newCachedThreadPool(new DaemonThreadFactory());
/*     */       }
/*     */     }
/*  65 */     this.threadPool.execute(fiber);
/*     */   }
/*     */ 
/*     */   public Fiber createFiber()
/*     */   {
/*  78 */     return new Fiber(this);
/*     */   }
/*  82 */   private static class DaemonThreadFactory implements ThreadFactory { static final AtomicInteger poolNumber = new AtomicInteger(1);
/*     */     final ThreadGroup group;
/*  84 */     final AtomicInteger threadNumber = new AtomicInteger(1);
/*     */     final String namePrefix;
/*     */ 
/*  88 */     DaemonThreadFactory() { SecurityManager s = System.getSecurityManager();
/*  89 */       this.group = (s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup());
/*     */ 
/*  91 */       this.namePrefix = ("jaxws-engine-" + poolNumber.getAndIncrement() + "-thread-");
/*     */     }
/*     */ 
/*     */     public Thread newThread(Runnable r)
/*     */     {
/*  97 */       Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
/*     */ 
/* 100 */       if (!t.isDaemon())
/* 101 */         t.setDaemon(true);
/* 102 */       if (t.getPriority() != 5)
/* 103 */         t.setPriority(5);
/* 104 */       return t;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.Engine
 * JD-Core Version:    0.6.2
 */