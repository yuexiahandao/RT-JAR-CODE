/*     */ package com.sun.corba.se.impl.orbutil.threadpool;
/*     */ 
/*     */ import com.sun.corba.se.spi.monitoring.LongMonitoredAttributeBase;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoredObject;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoredObjectFactory;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoringFactories;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.Work;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.WorkQueue;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ public class WorkQueueImpl
/*     */   implements WorkQueue
/*     */ {
/*     */   private ThreadPool workerThreadPool;
/*  45 */   private LinkedList theWorkQueue = new LinkedList();
/*  46 */   private long workItemsAdded = 0L;
/*     */ 
/*  49 */   private long workItemsDequeued = 1L;
/*     */ 
/*  51 */   private long totalTimeInQueue = 0L;
/*     */   private String name;
/*     */   private MonitoredObject workqueueMonitoredObject;
/*     */ 
/*     */   public WorkQueueImpl()
/*     */   {
/*  60 */     this.name = "default-workqueue";
/*  61 */     initializeMonitoring();
/*     */   }
/*     */ 
/*     */   public WorkQueueImpl(ThreadPool paramThreadPool) {
/*  65 */     this(paramThreadPool, "default-workqueue");
/*     */   }
/*     */ 
/*     */   public WorkQueueImpl(ThreadPool paramThreadPool, String paramString) {
/*  69 */     this.workerThreadPool = paramThreadPool;
/*  70 */     this.name = paramString;
/*  71 */     initializeMonitoring();
/*     */   }
/*     */ 
/*     */   private void initializeMonitoring()
/*     */   {
/*  76 */     this.workqueueMonitoredObject = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(this.name, "Monitoring for a Work Queue");
/*     */ 
/*  81 */     LongMonitoredAttributeBase local1 = new LongMonitoredAttributeBase("totalWorkItemsAdded", "Total number of Work items added to the Queue")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/*  85 */         return new Long(WorkQueueImpl.this.totalWorkItemsAdded());
/*     */       }
/*     */     };
/*  88 */     this.workqueueMonitoredObject.addAttribute(local1);
/*  89 */     LongMonitoredAttributeBase local2 = new LongMonitoredAttributeBase("workItemsInQueue", "Number of Work items in the Queue to be processed")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/*  93 */         return new Long(WorkQueueImpl.this.workItemsInQueue());
/*     */       }
/*     */     };
/*  96 */     this.workqueueMonitoredObject.addAttribute(local2);
/*  97 */     LongMonitoredAttributeBase local3 = new LongMonitoredAttributeBase("averageTimeInQueue", "Average time a work item waits in the work queue")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/* 101 */         return new Long(WorkQueueImpl.this.averageTimeInQueue());
/*     */       }
/*     */     };
/* 104 */     this.workqueueMonitoredObject.addAttribute(local3);
/*     */   }
/*     */ 
/*     */   MonitoredObject getMonitoredObject()
/*     */   {
/* 111 */     return this.workqueueMonitoredObject;
/*     */   }
/*     */ 
/*     */   public synchronized void addWork(Work paramWork) {
/* 115 */     this.workItemsAdded += 1L;
/* 116 */     paramWork.setEnqueueTime(System.currentTimeMillis());
/* 117 */     this.theWorkQueue.addLast(paramWork);
/* 118 */     ((ThreadPoolImpl)this.workerThreadPool).notifyForAvailableWork(this);
/*     */   }
/*     */ 
/*     */   synchronized Work requestWork(long paramLong)
/*     */     throws TimeoutException, InterruptedException
/*     */   {
/* 124 */     ((ThreadPoolImpl)this.workerThreadPool).incrementNumberOfAvailableThreads();
/*     */     Work localWork;
/* 126 */     if (this.theWorkQueue.size() != 0) {
/* 127 */       localWork = (Work)this.theWorkQueue.removeFirst();
/* 128 */       this.totalTimeInQueue += System.currentTimeMillis() - localWork.getEnqueueTime();
/* 129 */       this.workItemsDequeued += 1L;
/* 130 */       ((ThreadPoolImpl)this.workerThreadPool).decrementNumberOfAvailableThreads();
/* 131 */       return localWork;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 136 */       long l1 = paramLong;
/* 137 */       long l2 = System.currentTimeMillis() + paramLong;
/*     */       do
/*     */       {
/* 141 */         wait(l1);
/*     */ 
/* 143 */         if (this.theWorkQueue.size() != 0) {
/* 144 */           localWork = (Work)this.theWorkQueue.removeFirst();
/* 145 */           this.totalTimeInQueue += System.currentTimeMillis() - localWork.getEnqueueTime();
/* 146 */           this.workItemsDequeued += 1L;
/* 147 */           ((ThreadPoolImpl)this.workerThreadPool).decrementNumberOfAvailableThreads();
/* 148 */           return localWork;
/*     */         }
/*     */ 
/* 151 */         l1 = l2 - System.currentTimeMillis();
/*     */       }
/* 153 */       while (l1 > 0L);
/*     */ 
/* 155 */       ((ThreadPoolImpl)this.workerThreadPool).decrementNumberOfAvailableThreads();
/* 156 */       throw new TimeoutException();
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {
/* 159 */       ((ThreadPoolImpl)this.workerThreadPool).decrementNumberOfAvailableThreads();
/* 160 */       throw localInterruptedException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setThreadPool(ThreadPool paramThreadPool) {
/* 165 */     this.workerThreadPool = paramThreadPool;
/*     */   }
/*     */ 
/*     */   public ThreadPool getThreadPool() {
/* 169 */     return this.workerThreadPool;
/*     */   }
/*     */ 
/*     */   public long totalWorkItemsAdded()
/*     */   {
/* 178 */     return this.workItemsAdded;
/*     */   }
/*     */ 
/*     */   public int workItemsInQueue()
/*     */   {
/* 187 */     return this.theWorkQueue.size();
/*     */   }
/*     */ 
/*     */   public synchronized long averageTimeInQueue() {
/* 191 */     return this.totalTimeInQueue / this.workItemsDequeued;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 195 */     return this.name;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.threadpool.WorkQueueImpl
 * JD-Core Version:    0.6.2
 */