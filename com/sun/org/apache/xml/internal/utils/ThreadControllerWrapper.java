/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ public class ThreadControllerWrapper
/*     */ {
/*  33 */   private static ThreadController m_tpool = new ThreadController();
/*     */ 
/*     */   public static Thread runThread(Runnable runnable, int priority)
/*     */   {
/*  37 */     return m_tpool.run(runnable, priority);
/*     */   }
/*     */ 
/*     */   public static void waitThread(Thread worker, Runnable task)
/*     */     throws InterruptedException
/*     */   {
/*  43 */     m_tpool.waitThread(worker, task);
/*     */   }
/*     */ 
/*     */   public static class ThreadController
/*     */   {
/*     */     public Thread run(Runnable task, int priority)
/*     */     {
/*  99 */       Thread t = new SafeThread(task);
/*     */ 
/* 101 */       t.start();
/*     */ 
/* 105 */       return t;
/*     */     }
/*     */ 
/*     */     public void waitThread(Thread worker, Runnable task)
/*     */       throws InterruptedException
/*     */     {
/* 122 */       worker.join();
/*     */     }
/*     */ 
/*     */     final class SafeThread extends Thread
/*     */     {
/*  59 */       private volatile boolean ran = false;
/*     */ 
/*     */       public SafeThread(Runnable target) {
/*  62 */         super();
/*     */       }
/*     */ 
/*     */       public final void run() {
/*  66 */         if (Thread.currentThread() != this) {
/*  67 */           throw new IllegalStateException("The run() method in a SafeThread cannot be called from another thread.");
/*     */         }
/*     */ 
/*  70 */         synchronized (this) {
/*  71 */           if (!this.ran) {
/*  72 */             this.ran = true;
/*     */           }
/*     */           else {
/*  75 */             throw new IllegalStateException("The run() method in a SafeThread cannot be called more than once.");
/*     */           }
/*     */         }
/*     */ 
/*  79 */         super.run();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper
 * JD-Core Version:    0.6.2
 */