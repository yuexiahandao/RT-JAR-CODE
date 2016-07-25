/*     */ package com.sun.corba.se.impl.orbutil.threadpool;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.monitoring.LongMonitoredAttributeBase;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoredObject;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoredObjectFactory;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoringFactories;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoringManager;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoringManagerFactory;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.NoSuchWorkQueueException;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.Work;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.WorkQueue;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ 
/*     */ public class ThreadPoolImpl
/*     */   implements ThreadPool
/*     */ {
/*  61 */   private static AtomicInteger threadCounter = new AtomicInteger(0);
/*  62 */   private static final ORBUtilSystemException wrapper = ORBUtilSystemException.get("rpc.transport");
/*     */   private WorkQueue workQueue;
/*  75 */   private int availableWorkerThreads = 0;
/*     */ 
/*  78 */   private int currentThreadCount = 0;
/*     */ 
/*  81 */   private int minWorkerThreads = 0;
/*     */ 
/*  84 */   private int maxWorkerThreads = 0;
/*     */   private long inactivityTimeout;
/*  90 */   private boolean boundedThreadPool = false;
/*     */ 
/*  95 */   private AtomicLong processedCount = new AtomicLong(1L);
/*     */ 
/*  99 */   private AtomicLong totalTimeTaken = new AtomicLong(0L);
/*     */   private String name;
/*     */   private MonitoredObject threadpoolMonitoredObject;
/*     */   private ThreadGroup threadGroup;
/* 110 */   Object workersLock = new Object();
/* 111 */   List<WorkerThread> workers = new ArrayList();
/*     */ 
/*     */   public ThreadPoolImpl(ThreadGroup paramThreadGroup, String paramString)
/*     */   {
/* 117 */     this.inactivityTimeout = 120000L;
/* 118 */     this.maxWorkerThreads = 2147483647;
/* 119 */     this.workQueue = new WorkQueueImpl(this);
/* 120 */     this.threadGroup = paramThreadGroup;
/* 121 */     this.name = paramString;
/* 122 */     initializeMonitoring();
/*     */   }
/*     */ 
/*     */   public ThreadPoolImpl(String paramString)
/*     */   {
/* 130 */     this(Thread.currentThread().getThreadGroup(), paramString);
/*     */   }
/*     */ 
/*     */   public ThreadPoolImpl(int paramInt1, int paramInt2, long paramLong, String paramString)
/*     */   {
/* 139 */     this.minWorkerThreads = paramInt1;
/* 140 */     this.maxWorkerThreads = paramInt2;
/* 141 */     this.inactivityTimeout = paramLong;
/* 142 */     this.boundedThreadPool = true;
/* 143 */     this.workQueue = new WorkQueueImpl(this);
/* 144 */     this.name = paramString;
/* 145 */     for (int i = 0; i < this.minWorkerThreads; i++) {
/* 146 */       createWorkerThread();
/*     */     }
/* 148 */     initializeMonitoring();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 155 */     ArrayList localArrayList = null;
/* 156 */     synchronized (this.workersLock) {
/* 157 */       localArrayList = new ArrayList(this.workers);
/*     */     }
/*     */ 
/* 160 */     for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) { WorkerThread localWorkerThread = (WorkerThread)((Iterator)???).next();
/* 161 */       localWorkerThread.close();
/* 162 */       while (localWorkerThread.getState() != Thread.State.TERMINATED) {
/*     */         try {
/* 164 */           localWorkerThread.join();
/*     */         } catch (InterruptedException localInterruptedException) {
/* 166 */           wrapper.interruptedJoinCallWhileClosingThreadPool(localInterruptedException, localWorkerThread, this);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 171 */     this.threadGroup = null;
/*     */   }
/*     */ 
/*     */   private void initializeMonitoring()
/*     */   {
/* 178 */     MonitoredObject localMonitoredObject1 = MonitoringFactories.getMonitoringManagerFactory().createMonitoringManager("orb", null).getRootMonitoredObject();
/*     */ 
/* 183 */     MonitoredObject localMonitoredObject2 = localMonitoredObject1.getChild("threadpool");
/*     */ 
/* 185 */     if (localMonitoredObject2 == null) {
/* 186 */       localMonitoredObject2 = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject("threadpool", "Monitoring for all ThreadPool instances");
/*     */ 
/* 190 */       localMonitoredObject1.addChild(localMonitoredObject2);
/*     */     }
/* 192 */     this.threadpoolMonitoredObject = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(this.name, "Monitoring for a ThreadPool");
/*     */ 
/* 197 */     localMonitoredObject2.addChild(this.threadpoolMonitoredObject);
/*     */ 
/* 199 */     LongMonitoredAttributeBase local1 = new LongMonitoredAttributeBase("currentNumberOfThreads", "Current number of total threads in the ThreadPool")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/* 203 */         return new Long(ThreadPoolImpl.this.currentNumberOfThreads());
/*     */       }
/*     */     };
/* 206 */     this.threadpoolMonitoredObject.addAttribute(local1);
/* 207 */     LongMonitoredAttributeBase local2 = new LongMonitoredAttributeBase("numberOfAvailableThreads", "Current number of total threads in the ThreadPool")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/* 211 */         return new Long(ThreadPoolImpl.this.numberOfAvailableThreads());
/*     */       }
/*     */     };
/* 214 */     this.threadpoolMonitoredObject.addAttribute(local2);
/* 215 */     LongMonitoredAttributeBase local3 = new LongMonitoredAttributeBase("numberOfBusyThreads", "Number of busy threads in the ThreadPool")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/* 219 */         return new Long(ThreadPoolImpl.this.numberOfBusyThreads());
/*     */       }
/*     */     };
/* 222 */     this.threadpoolMonitoredObject.addAttribute(local3);
/* 223 */     LongMonitoredAttributeBase local4 = new LongMonitoredAttributeBase("averageWorkCompletionTime", "Average elapsed time taken to complete a work item by the ThreadPool")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/* 227 */         return new Long(ThreadPoolImpl.this.averageWorkCompletionTime());
/*     */       }
/*     */     };
/* 230 */     this.threadpoolMonitoredObject.addAttribute(local4);
/* 231 */     LongMonitoredAttributeBase local5 = new LongMonitoredAttributeBase("currentProcessedCount", "Number of Work items processed by the ThreadPool")
/*     */     {
/*     */       public Object getValue()
/*     */       {
/* 235 */         return new Long(ThreadPoolImpl.this.currentProcessedCount());
/*     */       }
/*     */     };
/* 238 */     this.threadpoolMonitoredObject.addAttribute(local5);
/*     */ 
/* 242 */     this.threadpoolMonitoredObject.addChild(((WorkQueueImpl)this.workQueue).getMonitoredObject());
/*     */   }
/*     */ 
/*     */   MonitoredObject getMonitoredObject()
/*     */   {
/* 249 */     return this.threadpoolMonitoredObject;
/*     */   }
/*     */ 
/*     */   public WorkQueue getAnyWorkQueue()
/*     */   {
/* 254 */     return this.workQueue;
/*     */   }
/*     */ 
/*     */   public WorkQueue getWorkQueue(int paramInt)
/*     */     throws NoSuchWorkQueueException
/*     */   {
/* 260 */     if (paramInt != 0)
/* 261 */       throw new NoSuchWorkQueueException();
/* 262 */     return this.workQueue;
/*     */   }
/*     */ 
/*     */   void notifyForAvailableWork(WorkQueue paramWorkQueue)
/*     */   {
/* 271 */     synchronized (paramWorkQueue) {
/* 272 */       if (this.availableWorkerThreads < paramWorkQueue.workItemsInQueue())
/* 273 */         createWorkerThread();
/*     */       else
/* 275 */         paramWorkQueue.notify();
/*     */     }
/*     */   }
/*     */ 
/*     */   private Thread createWorkerThreadHelper(String paramString)
/*     */   {
/* 318 */     WorkerThread localWorkerThread = new WorkerThread(this.threadGroup, paramString);
/* 319 */     synchronized (this.workersLock) {
/* 320 */       this.workers.add(localWorkerThread);
/*     */     }
/*     */ 
/* 329 */     localWorkerThread.setDaemon(true);
/*     */ 
/* 331 */     wrapper.workerThreadCreated(localWorkerThread, localWorkerThread.getContextClassLoader());
/*     */ 
/* 333 */     localWorkerThread.start();
/* 334 */     return null;
/*     */   }
/*     */ 
/*     */   void createWorkerThread()
/*     */   {
/* 343 */     final String str = getName();
/* 344 */     synchronized (this.workQueue) {
/*     */       try {
/* 346 */         if (System.getSecurityManager() == null) {
/* 347 */           createWorkerThreadHelper(str);
/*     */         }
/*     */         else {
/* 350 */           AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Object run() {
/* 353 */               return ThreadPoolImpl.this.createWorkerThreadHelper(str);
/*     */             }
/*     */           });
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/* 361 */         decrementCurrentNumberOfThreads();
/* 362 */         wrapper.workerThreadCreationFailure(localThrowable);
/*     */       } finally {
/* 364 */         incrementCurrentNumberOfThreads();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int minimumNumberOfThreads() {
/* 370 */     return this.minWorkerThreads;
/*     */   }
/*     */ 
/*     */   public int maximumNumberOfThreads() {
/* 374 */     return this.maxWorkerThreads;
/*     */   }
/*     */ 
/*     */   public long idleTimeoutForThreads() {
/* 378 */     return this.inactivityTimeout;
/*     */   }
/*     */ 
/*     */   public int currentNumberOfThreads() {
/* 382 */     synchronized (this.workQueue) {
/* 383 */       return this.currentThreadCount;
/*     */     }
/*     */   }
/*     */ 
/*     */   void decrementCurrentNumberOfThreads() {
/* 388 */     synchronized (this.workQueue) {
/* 389 */       this.currentThreadCount -= 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   void incrementCurrentNumberOfThreads() {
/* 394 */     synchronized (this.workQueue) {
/* 395 */       this.currentThreadCount += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int numberOfAvailableThreads() {
/* 400 */     synchronized (this.workQueue) {
/* 401 */       return this.availableWorkerThreads;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int numberOfBusyThreads() {
/* 406 */     synchronized (this.workQueue) {
/* 407 */       return this.currentThreadCount - this.availableWorkerThreads;
/*     */     }
/*     */   }
/*     */ 
/*     */   public long averageWorkCompletionTime() {
/* 412 */     synchronized (this.workQueue) {
/* 413 */       return this.totalTimeTaken.get() / this.processedCount.get();
/*     */     }
/*     */   }
/*     */ 
/*     */   public long currentProcessedCount() {
/* 418 */     synchronized (this.workQueue) {
/* 419 */       return this.processedCount.get();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 424 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int numberOfWorkQueues()
/*     */   {
/* 431 */     return 1;
/*     */   }
/*     */ 
/*     */   private static synchronized int getUniqueThreadId()
/*     */   {
/* 436 */     return threadCounter.incrementAndGet();
/*     */   }
/*     */ 
/*     */   void decrementNumberOfAvailableThreads()
/*     */   {
/* 445 */     synchronized (this.workQueue) {
/* 446 */       this.availableWorkerThreads -= 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   void incrementNumberOfAvailableThreads()
/*     */   {
/* 456 */     synchronized (this.workQueue) {
/* 457 */       this.availableWorkerThreads += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class WorkerThread extends Thread
/*     */     implements Closeable
/*     */   {
/*     */     private Work currentWork;
/* 465 */     private int threadId = 0;
/* 466 */     private volatile boolean closeCalled = false;
/*     */     private String threadPoolName;
/* 469 */     private StringBuffer workerThreadName = new StringBuffer();
/*     */ 
/*     */     WorkerThread(ThreadGroup paramString, String arg3) {
/* 472 */       super("Idle");
/*     */       String str;
/* 474 */       this.threadPoolName = str;
/* 475 */       setName(composeWorkerThreadName(str, "Idle"));
/*     */     }
/*     */ 
/*     */     public synchronized void close() {
/* 479 */       this.closeCalled = true;
/* 480 */       interrupt();
/*     */     }
/*     */ 
/*     */     private void resetClassLoader()
/*     */     {
/*     */     }
/*     */ 
/*     */     private void performWork() {
/* 488 */       long l1 = System.currentTimeMillis();
/*     */       try {
/* 490 */         this.currentWork.doWork();
/*     */       } catch (Throwable localThrowable) {
/* 492 */         ThreadPoolImpl.wrapper.workerThreadDoWorkThrowable(this, localThrowable);
/*     */       }
/* 494 */       long l2 = System.currentTimeMillis() - l1;
/* 495 */       ThreadPoolImpl.this.totalTimeTaken.addAndGet(l2);
/* 496 */       ThreadPoolImpl.this.processedCount.incrementAndGet();
/*     */     }
/*     */ 
/*     */     public void run() {
/*     */       try {
/* 501 */         while (!this.closeCalled) {
/*     */           try {
/* 503 */             this.currentWork = ((WorkQueueImpl)ThreadPoolImpl.this.workQueue).requestWork(ThreadPoolImpl.this.inactivityTimeout);
/*     */ 
/* 505 */             if (this.currentWork == null)
/* 506 */               continue;
/*     */           } catch (InterruptedException localInterruptedException) {
/* 508 */             ThreadPoolImpl.wrapper.workQueueThreadInterrupted(localInterruptedException, getName(), Boolean.valueOf(this.closeCalled));
/*     */ 
/* 511 */             continue;
/*     */           } catch (Throwable localThrowable) {
/* 513 */             ThreadPoolImpl.wrapper.workerThreadThrowableFromRequestWork(this, localThrowable, ThreadPoolImpl.this.workQueue.getName());
/*     */           }
/*     */ 
/* 516 */           continue;
/*     */ 
/* 519 */           performWork();
/*     */ 
/* 523 */           this.currentWork = null;
/*     */ 
/* 525 */           resetClassLoader();
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable) {
/* 529 */         ThreadPoolImpl.wrapper.workerThreadCaughtUnexpectedThrowable(this, ???);
/*     */       } finally {
/* 531 */         synchronized (ThreadPoolImpl.this.workersLock) {
/* 532 */           ThreadPoolImpl.this.workers.remove(this);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private String composeWorkerThreadName(String paramString1, String paramString2) {
/* 538 */       this.workerThreadName.setLength(0);
/* 539 */       this.workerThreadName.append("p: ").append(paramString1);
/* 540 */       this.workerThreadName.append("; w: ").append(paramString2);
/* 541 */       return this.workerThreadName.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl
 * JD-Core Version:    0.6.2
 */