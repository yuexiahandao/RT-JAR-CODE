/*     */ package com.sun.jmx.snmp.tasks;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class ThreadService
/*     */   implements TaskServer
/*     */ {
/* 230 */   private ArrayList<Runnable> jobList = new ArrayList(0);
/*     */   private ExecutorThread[] threadList;
/* 233 */   private int minThreads = 1;
/* 234 */   private int currThreds = 0;
/* 235 */   private int idle = 0;
/*     */ 
/* 237 */   private boolean terminated = false;
/*     */   private int priority;
/* 239 */   private ThreadGroup threadGroup = new ThreadGroup("ThreadService");
/*     */   private ClassLoader cloader;
/* 242 */   private static long counter = 0L;
/*     */ 
/* 244 */   private int addedJobs = 1;
/* 245 */   private int doneJobs = 1;
/*     */ 
/*     */   public ThreadService(int paramInt)
/*     */   {
/*  41 */     if (paramInt <= 0) {
/*  42 */       throw new IllegalArgumentException("The thread number should bigger than zero.");
/*     */     }
/*     */ 
/*  45 */     this.minThreads = paramInt;
/*  46 */     this.threadList = new ExecutorThread[paramInt];
/*     */ 
/*  48 */     this.priority = Thread.currentThread().getPriority();
/*  49 */     this.cloader = Thread.currentThread().getContextClassLoader();
/*     */   }
/*     */ 
/*     */   public void submitTask(Task paramTask)
/*     */     throws IllegalArgumentException
/*     */   {
/*  67 */     submitTask(paramTask);
/*     */   }
/*     */ 
/*     */   public void submitTask(Runnable paramRunnable)
/*     */     throws IllegalArgumentException
/*     */   {
/*  78 */     stateCheck();
/*     */ 
/*  80 */     if (paramRunnable == null) {
/*  81 */       throw new IllegalArgumentException("No task specified.");
/*     */     }
/*     */ 
/*  84 */     synchronized (this.jobList) {
/*  85 */       this.jobList.add(this.jobList.size(), paramRunnable);
/*     */ 
/*  87 */       this.jobList.notify();
/*     */     }
/*     */ 
/*  90 */     createThread();
/*     */   }
/*     */ 
/*     */   public Runnable removeTask(Runnable paramRunnable) {
/*  94 */     stateCheck();
/*     */ 
/*  96 */     Runnable localRunnable = null;
/*  97 */     synchronized (this.jobList) {
/*  98 */       int i = this.jobList.indexOf(paramRunnable);
/*  99 */       if (i >= 0) {
/* 100 */         localRunnable = (Runnable)this.jobList.remove(i);
/*     */       }
/*     */     }
/* 103 */     if ((localRunnable != null) && ((localRunnable instanceof Task)))
/* 104 */       ((Task)localRunnable).cancel();
/* 105 */     return localRunnable;
/*     */   }
/*     */ 
/*     */   public void removeAll() {
/* 109 */     stateCheck();
/*     */     Object[] arrayOfObject;
/* 112 */     synchronized (this.jobList) {
/* 113 */       arrayOfObject = this.jobList.toArray();
/* 114 */       this.jobList.clear();
/*     */     }
/* 116 */     ??? = arrayOfObject.length;
/* 117 */     for (Object localObject2 = 0; localObject2 < ???; localObject2++) {
/* 118 */       Object localObject3 = arrayOfObject[localObject2];
/* 119 */       if ((localObject3 != null) && ((localObject3 instanceof Task))) ((Task)localObject3).cancel();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void terminate()
/*     */   {
/* 126 */     if (this.terminated == true) {
/* 127 */       return;
/*     */     }
/*     */ 
/* 130 */     this.terminated = true;
/*     */ 
/* 132 */     synchronized (this.jobList) {
/* 133 */       this.jobList.notifyAll();
/*     */     }
/*     */ 
/* 136 */     removeAll();
/*     */ 
/* 138 */     for (int i = 0; i < this.currThreds; i++) {
/*     */       try {
/* 140 */         this.threadList[i].interrupt();
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */     }
/* 146 */     this.threadList = null;
/*     */   }
/*     */ 
/*     */   private void stateCheck()
/*     */     throws IllegalStateException
/*     */   {
/* 210 */     if (this.terminated)
/* 211 */       throw new IllegalStateException("The thread service has been terminated.");
/*     */   }
/*     */ 
/*     */   private void createThread()
/*     */   {
/* 216 */     if (this.idle < 1)
/* 217 */       synchronized (this.threadList) {
/* 218 */         if ((this.jobList.size() > 0) && (this.currThreds < this.minThreads)) {
/* 219 */           ExecutorThread localExecutorThread = new ExecutorThread();
/* 220 */           localExecutorThread.start();
/* 221 */           this.threadList[(this.currThreds++)] = localExecutorThread;
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private class ExecutorThread extends Thread
/*     */   {
/*     */     public ExecutorThread()
/*     */     {
/* 156 */       super("ThreadService-" + ThreadService.access$108());
/* 157 */       setDaemon(true);
/*     */ 
/* 160 */       setPriority(ThreadService.this.priority);
/* 161 */       setContextClassLoader(ThreadService.this.cloader);
/*     */ 
/* 163 */       ThreadService.access$408(ThreadService.this);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 168 */       while (!ThreadService.this.terminated) {
/* 169 */         Runnable localRunnable = null;
/*     */ 
/* 171 */         synchronized (ThreadService.this.jobList) {
/* 172 */           if (ThreadService.this.jobList.size() > 0) {
/* 173 */             localRunnable = (Runnable)ThreadService.this.jobList.remove(0);
/* 174 */             if (ThreadService.this.jobList.size() > 0)
/* 175 */               ThreadService.this.jobList.notify();
/*     */           }
/*     */           else
/*     */           {
/*     */             try {
/* 180 */               ThreadService.this.jobList.wait(); } catch (InterruptedException localInterruptedException) {
/* 181 */               localInterruptedException = 
/* 184 */                 localInterruptedException; } finally {
/* 185 */             }continue;
/*     */           }
/*     */         }
/* 188 */         if (localRunnable != null) {
/*     */           try {
/* 190 */             ThreadService.access$410(ThreadService.this);
/* 191 */             localRunnable.run();
/*     */           }
/*     */           catch (Exception localException) {
/* 194 */             localException.printStackTrace();
/*     */           } finally {
/* 196 */             ThreadService.access$408(ThreadService.this);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 201 */         setPriority(ThreadService.this.priority);
/* 202 */         interrupted();
/* 203 */         setContextClassLoader(ThreadService.this.cloader);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.tasks.ThreadService
 * JD-Core Version:    0.6.2
 */