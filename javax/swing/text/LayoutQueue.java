/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class LayoutQueue
/*     */ {
/*  39 */   private static final Object DEFAULT_QUEUE = new Object();
/*     */   private Vector<Runnable> tasks;
/*     */   private Thread worker;
/*     */ 
/*     */   public LayoutQueue()
/*     */   {
/*  48 */     this.tasks = new Vector();
/*     */   }
/*     */ 
/*     */   public static LayoutQueue getDefaultQueue()
/*     */   {
/*  55 */     AppContext localAppContext = AppContext.getAppContext();
/*  56 */     synchronized (DEFAULT_QUEUE) {
/*  57 */       LayoutQueue localLayoutQueue = (LayoutQueue)localAppContext.get(DEFAULT_QUEUE);
/*  58 */       if (localLayoutQueue == null) {
/*  59 */         localLayoutQueue = new LayoutQueue();
/*  60 */         localAppContext.put(DEFAULT_QUEUE, localLayoutQueue);
/*     */       }
/*  62 */       return localLayoutQueue;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void setDefaultQueue(LayoutQueue paramLayoutQueue)
/*     */   {
/*  72 */     synchronized (DEFAULT_QUEUE) {
/*  73 */       AppContext.getAppContext().put(DEFAULT_QUEUE, paramLayoutQueue);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void addTask(Runnable paramRunnable)
/*     */   {
/*  82 */     if (this.worker == null) {
/*  83 */       this.worker = new LayoutThread();
/*  84 */       this.worker.start();
/*     */     }
/*  86 */     this.tasks.addElement(paramRunnable);
/*  87 */     notifyAll();
/*     */   }
/*     */ 
/*     */   protected synchronized Runnable waitForWork()
/*     */   {
/*  94 */     while (this.tasks.size() == 0) {
/*     */       try {
/*  96 */         wait();
/*     */       } catch (InterruptedException localInterruptedException) {
/*  98 */         return null;
/*     */       }
/*     */     }
/* 101 */     Runnable localRunnable = (Runnable)this.tasks.firstElement();
/* 102 */     this.tasks.removeElementAt(0);
/* 103 */     return localRunnable;
/*     */   }
/*     */ 
/*     */   class LayoutThread extends Thread
/*     */   {
/*     */     LayoutThread()
/*     */     {
/* 112 */       super();
/* 113 */       setPriority(1);
/*     */     }
/*     */ 
/*     */     public void run() {
/*     */       Runnable localRunnable;
/*     */       do {
/* 119 */         localRunnable = LayoutQueue.this.waitForWork();
/* 120 */         if (localRunnable != null)
/* 121 */           localRunnable.run();
/*     */       }
/* 123 */       while (localRunnable != null);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.LayoutQueue
 * JD-Core Version:    0.6.2
 */