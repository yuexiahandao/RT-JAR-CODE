/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RunnableFuture;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import sun.awt.AppContext;
/*     */ import sun.swing.AccumulativeRunnable;
/*     */ 
/*     */ public abstract class SwingWorker<T, V>
/*     */   implements RunnableFuture<T>
/*     */ {
/*     */   private static final int MAX_WORKER_THREADS = 10;
/*     */   private volatile int progress;
/*     */   private volatile StateValue state;
/*     */   private final FutureTask<T> future;
/*     */   private final PropertyChangeSupport propertyChangeSupport;
/*     */   private AccumulativeRunnable<V> doProcess;
/*     */   private AccumulativeRunnable<Integer> doNotifyProgressChange;
/* 263 */   private final AccumulativeRunnable<Runnable> doSubmit = getDoSubmit();
/*     */ 
/* 814 */   private static final Object DO_SUBMIT_KEY = new StringBuilder("doSubmit");
/*     */ 
/*     */   public SwingWorker()
/*     */   {
/* 292 */     Callable local1 = new Callable()
/*     */     {
/*     */       public T call() throws Exception {
/* 295 */         SwingWorker.this.setState(SwingWorker.StateValue.STARTED);
/* 296 */         return SwingWorker.this.doInBackground();
/*     */       }
/*     */     };
/* 300 */     this.future = new FutureTask(local1)
/*     */     {
/*     */       protected void done() {
/* 303 */         SwingWorker.this.doneEDT();
/* 304 */         SwingWorker.this.setState(SwingWorker.StateValue.DONE);
/*     */       }
/*     */     };
/* 308 */     this.state = StateValue.PENDING;
/* 309 */     this.propertyChangeSupport = new SwingWorkerPropertyChangeSupport(this);
/* 310 */     this.doProcess = null;
/* 311 */     this.doNotifyProgressChange = null;
/*     */   }
/*     */ 
/*     */   protected abstract T doInBackground()
/*     */     throws Exception;
/*     */ 
/*     */   public final void run()
/*     */   {
/* 335 */     this.future.run();
/*     */   }
/*     */ 
/*     */   @SafeVarargs
/*     */   protected final void publish(V[] paramArrayOfV)
/*     */   {
/* 409 */     synchronized (this) {
/* 410 */       if (this.doProcess == null) {
/* 411 */         this.doProcess = new AccumulativeRunnable()
/*     */         {
/*     */           public void run(List<V> paramAnonymousList) {
/* 414 */             SwingWorker.this.process(paramAnonymousList);
/*     */           }
/*     */ 
/*     */           protected void submit() {
/* 418 */             SwingWorker.this.doSubmit.add(new Runnable[] { this });
/*     */           }
/*     */         };
/*     */       }
/*     */     }
/* 423 */     this.doProcess.add(paramArrayOfV);
/*     */   }
/*     */ 
/*     */   protected void process(List<V> paramList)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void done()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected final void setProgress(int paramInt)
/*     */   {
/* 484 */     if ((paramInt < 0) || (paramInt > 100)) {
/* 485 */       throw new IllegalArgumentException("the value should be from 0 to 100");
/*     */     }
/* 487 */     if (this.progress == paramInt) {
/* 488 */       return;
/*     */     }
/* 490 */     int i = this.progress;
/* 491 */     this.progress = paramInt;
/* 492 */     if (!getPropertyChangeSupport().hasListeners("progress")) {
/* 493 */       return;
/*     */     }
/* 495 */     synchronized (this) {
/* 496 */       if (this.doNotifyProgressChange == null) {
/* 497 */         this.doNotifyProgressChange = new AccumulativeRunnable()
/*     */         {
/*     */           public void run(List<Integer> paramAnonymousList)
/*     */           {
/* 501 */             SwingWorker.this.firePropertyChange("progress", paramAnonymousList.get(0), paramAnonymousList.get(paramAnonymousList.size() - 1));
/*     */           }
/*     */ 
/*     */           protected void submit()
/*     */           {
/* 507 */             SwingWorker.this.doSubmit.add(new Runnable[] { this });
/*     */           }
/*     */         };
/*     */       }
/*     */     }
/* 512 */     this.doNotifyProgressChange.add(new Integer[] { Integer.valueOf(i), Integer.valueOf(paramInt) });
/*     */   }
/*     */ 
/*     */   public final int getProgress()
/*     */   {
/* 521 */     return this.progress;
/*     */   }
/*     */ 
/*     */   public final void execute()
/*     */   {
/* 538 */     getWorkersExecutorService().execute(this);
/*     */   }
/*     */ 
/*     */   public final boolean cancel(boolean paramBoolean)
/*     */   {
/* 546 */     return this.future.cancel(paramBoolean);
/*     */   }
/*     */ 
/*     */   public final boolean isCancelled()
/*     */   {
/* 553 */     return this.future.isCancelled();
/*     */   }
/*     */ 
/*     */   public final boolean isDone()
/*     */   {
/* 560 */     return this.future.isDone();
/*     */   }
/*     */ 
/*     */   public final T get()
/*     */     throws InterruptedException, ExecutionException
/*     */   {
/* 602 */     return this.future.get();
/*     */   }
/*     */ 
/*     */   public final T get(long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException, ExecutionException, TimeoutException
/*     */   {
/* 612 */     return this.future.get(paramLong, paramTimeUnit);
/*     */   }
/*     */ 
/*     */   public final void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 631 */     getPropertyChangeSupport().addPropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public final void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 649 */     getPropertyChangeSupport().removePropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public final void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*     */   {
/* 676 */     getPropertyChangeSupport().firePropertyChange(paramString, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public final PropertyChangeSupport getPropertyChangeSupport()
/*     */   {
/* 698 */     return this.propertyChangeSupport;
/*     */   }
/*     */ 
/*     */   public final StateValue getState()
/*     */   {
/* 713 */     if (isDone()) {
/* 714 */       return StateValue.DONE;
/*     */     }
/* 716 */     return this.state;
/*     */   }
/*     */ 
/*     */   private void setState(StateValue paramStateValue)
/*     */   {
/* 725 */     StateValue localStateValue = this.state;
/* 726 */     this.state = paramStateValue;
/* 727 */     firePropertyChange("state", localStateValue, paramStateValue);
/*     */   }
/*     */ 
/*     */   private void doneEDT()
/*     */   {
/* 734 */     Runnable local5 = new Runnable()
/*     */     {
/*     */       public void run() {
/* 737 */         SwingWorker.this.done();
/*     */       }
/*     */     };
/* 740 */     if (SwingUtilities.isEventDispatchThread())
/* 741 */       local5.run();
/*     */     else
/* 743 */       this.doSubmit.add(new Runnable[] { local5 });
/*     */   }
/*     */ 
/*     */   private static synchronized ExecutorService getWorkersExecutorService()
/*     */   {
/* 757 */     AppContext localAppContext = AppContext.getAppContext();
/* 758 */     Object localObject1 = (ExecutorService)localAppContext.get(SwingWorker.class);
/*     */ 
/* 760 */     if (localObject1 == null)
/*     */     {
/* 762 */       ThreadFactory local6 = new ThreadFactory()
/*     */       {
/* 764 */         final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
/*     */ 
/*     */         public Thread newThread(Runnable paramAnonymousRunnable) {
/* 767 */           Thread localThread = this.defaultFactory.newThread(paramAnonymousRunnable);
/*     */ 
/* 769 */           localThread.setName("SwingWorker-" + localThread.getName());
/*     */ 
/* 771 */           localThread.setDaemon(true);
/* 772 */           return localThread;
/*     */         }
/*     */       };
/* 776 */       localObject1 = new ThreadPoolExecutor(10, 10, 10L, TimeUnit.MINUTES, new LinkedBlockingQueue(), local6);
/*     */ 
/* 781 */       localAppContext.put(SwingWorker.class, localObject1);
/*     */ 
/* 785 */       Object localObject2 = localObject1;
/* 786 */       localAppContext.addPropertyChangeListener("disposed", new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent)
/*     */         {
/* 790 */           boolean bool = ((Boolean)paramAnonymousPropertyChangeEvent.getNewValue()).booleanValue();
/* 791 */           if (bool) {
/* 792 */             WeakReference localWeakReference = new WeakReference(this.val$es);
/*     */ 
/* 794 */             final ExecutorService localExecutorService = (ExecutorService)localWeakReference.get();
/*     */ 
/* 796 */             if (localExecutorService != null) {
/* 797 */               AccessController.doPrivileged(new PrivilegedAction()
/*     */               {
/*     */                 public Void run() {
/* 800 */                   localExecutorService.shutdown();
/* 801 */                   return null;
/*     */                 }
/*     */               });
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       });
/*     */     }
/*     */ 
/* 811 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private static AccumulativeRunnable<Runnable> getDoSubmit()
/*     */   {
/* 816 */     synchronized (DO_SUBMIT_KEY) {
/* 817 */       AppContext localAppContext = AppContext.getAppContext();
/* 818 */       Object localObject1 = localAppContext.get(DO_SUBMIT_KEY);
/* 819 */       if (localObject1 == null) {
/* 820 */         localObject1 = new DoSubmitAccumulativeRunnable(null);
/* 821 */         localAppContext.put(DO_SUBMIT_KEY, localObject1);
/*     */       }
/* 823 */       return (AccumulativeRunnable)localObject1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class DoSubmitAccumulativeRunnable extends AccumulativeRunnable<Runnable> implements ActionListener {
/*     */     private static final int DELAY = 33;
/*     */ 
/*     */     protected void run(List<Runnable> paramList) {
/* 831 */       for (Runnable localRunnable : paramList)
/* 832 */         localRunnable.run();
/*     */     }
/*     */ 
/*     */     protected void submit()
/*     */     {
/* 837 */       Timer localTimer = new Timer(33, this);
/* 838 */       localTimer.setRepeats(false);
/* 839 */       localTimer.start();
/*     */     }
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 842 */       run();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum StateValue
/*     */   {
/* 273 */     PENDING, 
/*     */ 
/* 278 */     STARTED, 
/*     */ 
/* 285 */     DONE;
/*     */   }
/*     */ 
/*     */   private class SwingWorkerPropertyChangeSupport extends PropertyChangeSupport
/*     */   {
/*     */     SwingWorkerPropertyChangeSupport(Object arg2)
/*     */     {
/* 849 */       super();
/*     */     }
/*     */ 
/*     */     public void firePropertyChange(final PropertyChangeEvent paramPropertyChangeEvent) {
/* 853 */       if (SwingUtilities.isEventDispatchThread())
/* 854 */         super.firePropertyChange(paramPropertyChangeEvent);
/*     */       else
/* 856 */         SwingWorker.this.doSubmit.add(new Runnable[] { new Runnable()
/*     */         {
/*     */           public void run() {
/* 859 */             SwingWorker.SwingWorkerPropertyChangeSupport.this.firePropertyChange(paramPropertyChangeEvent);
/*     */           }
/*     */         }
/*     */          });
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.SwingWorker
 * JD-Core Version:    0.6.2
 */