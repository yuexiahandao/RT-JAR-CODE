/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.SystemTray;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.TrayIcon;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.InvocationEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import sun.misc.JavaAWTAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public final class AppContext
/*     */ {
/* 134 */   private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.AppContext");
/*     */ 
/* 142 */   public static final Object EVENT_QUEUE_KEY = new StringBuffer("EventQueue");
/*     */ 
/* 147 */   public static final Object EVENT_QUEUE_LOCK_KEY = new StringBuilder("EventQueue.Lock");
/* 148 */   public static final Object EVENT_QUEUE_COND_KEY = new StringBuilder("EventQueue.Condition");
/*     */ 
/* 152 */   private static final Map<ThreadGroup, AppContext> threadGroup2appContext = Collections.synchronizedMap(new IdentityHashMap());
/*     */ 
/* 168 */   private static volatile AppContext mainAppContext = null;
/*     */ 
/* 171 */   private static final Object getAppContextLock = new GetAppContextLock(null);
/*     */ 
/* 179 */   private final HashMap table = new HashMap();
/*     */   private final ThreadGroup threadGroup;
/* 191 */   private PropertyChangeSupport changeSupport = null;
/*     */   public static final String DISPOSED_PROPERTY_NAME = "disposed";
/*     */   public static final String GUI_DISPOSED = "guidisposed";
/* 202 */   private volatile State state = State.VALID;
/*     */ 
/* 215 */   private static final AtomicInteger numAppContexts = new AtomicInteger(0);
/*     */   private final ClassLoader contextClassLoader;
/* 256 */   private static final ThreadLocal<AppContext> threadAppContext = new ThreadLocal();
/*     */ 
/* 391 */   private long DISPOSAL_TIMEOUT = 5000L;
/*     */ 
/* 397 */   private long THREAD_INTERRUPT_TIMEOUT = 1000L;
/*     */ 
/* 616 */   private MostRecentKeyValue mostRecentKeyValue = null;
/* 617 */   private MostRecentKeyValue shadowMostRecentKeyValue = null;
/*     */ 
/*     */   public static Set<AppContext> getAppContexts()
/*     */   {
/* 159 */     synchronized (threadGroup2appContext) {
/* 160 */       return new HashSet(threadGroup2appContext.values());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isDisposed()
/*     */   {
/* 205 */     return this.state == State.DISPOSED;
/*     */   }
/*     */ 
/*     */   AppContext(ThreadGroup paramThreadGroup)
/*     */   {
/* 236 */     numAppContexts.incrementAndGet();
/*     */ 
/* 238 */     this.threadGroup = paramThreadGroup;
/* 239 */     threadGroup2appContext.put(paramThreadGroup, this);
/*     */ 
/* 241 */     this.contextClassLoader = ((ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public ClassLoader run() {
/* 244 */         return Thread.currentThread().getContextClassLoader();
/*     */       }
/*     */     }));
/* 250 */     ReentrantLock localReentrantLock = new ReentrantLock();
/* 251 */     put(EVENT_QUEUE_LOCK_KEY, localReentrantLock);
/* 252 */     Condition localCondition = localReentrantLock.newCondition();
/* 253 */     put(EVENT_QUEUE_COND_KEY, localCondition);
/*     */   }
/*     */ 
/*     */   private static final void initMainAppContext()
/*     */   {
/* 263 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Void run() {
/* 265 */         Object localObject = Thread.currentThread().getThreadGroup();
/*     */ 
/* 267 */         ThreadGroup localThreadGroup = ((ThreadGroup)localObject).getParent();
/* 268 */         while (localThreadGroup != null)
/*     */         {
/* 270 */           localObject = localThreadGroup;
/* 271 */           localThreadGroup = ((ThreadGroup)localObject).getParent();
/*     */         }
/*     */ 
/* 274 */         AppContext.access$102(SunToolkit.createNewAppContext((ThreadGroup)localObject));
/* 275 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static final AppContext getAppContext()
/*     */   {
/* 293 */     if ((numAppContexts.get() == 1) && (mainAppContext != null)) {
/* 294 */       return mainAppContext;
/*     */     }
/*     */ 
/* 297 */     AppContext localAppContext = (AppContext)threadAppContext.get();
/*     */ 
/* 299 */     if (null == localAppContext) {
/* 300 */       localAppContext = (AppContext)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public AppContext run()
/*     */         {
/* 307 */           ThreadGroup localThreadGroup1 = Thread.currentThread().getThreadGroup();
/* 308 */           ThreadGroup localThreadGroup2 = localThreadGroup1;
/*     */ 
/* 314 */           synchronized (AppContext.getAppContextLock) {
/* 315 */             if (AppContext.numAppContexts.get() == 0) {
/* 316 */               if ((System.getProperty("javaplugin.version") == null) && (System.getProperty("javawebstart.version") == null))
/*     */               {
/* 318 */                 AppContext.access$400();
/* 319 */               } else if ((System.getProperty("javafx.version") != null) && (localThreadGroup2.getParent() != null))
/*     */               {
/* 322 */                 SunToolkit.createNewAppContext();
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 327 */           ??? = (AppContext)AppContext.threadGroup2appContext.get(localThreadGroup2);
/* 328 */           while (??? == null) {
/* 329 */             localThreadGroup2 = localThreadGroup2.getParent();
/* 330 */             if (localThreadGroup2 == null)
/*     */             {
/* 333 */               localObject2 = System.getSecurityManager();
/* 334 */               if (localObject2 != null) {
/* 335 */                 ThreadGroup localThreadGroup3 = ((SecurityManager)localObject2).getThreadGroup();
/* 336 */                 if (localThreadGroup3 != null)
/*     */                 {
/* 342 */                   return (AppContext)AppContext.threadGroup2appContext.get(localThreadGroup3);
/*     */                 }
/*     */               }
/* 345 */               return null;
/*     */             }
/* 347 */             ??? = (AppContext)AppContext.threadGroup2appContext.get(localThreadGroup2);
/*     */           }
/*     */ 
/* 353 */           for (Object localObject2 = localThreadGroup1; localObject2 != localThreadGroup2; localObject2 = ((ThreadGroup)localObject2).getParent()) {
/* 354 */             AppContext.threadGroup2appContext.put(localObject2, ???);
/*     */           }
/*     */ 
/* 358 */           AppContext.threadAppContext.set(???);
/*     */ 
/* 360 */           return ???;
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/* 365 */     return localAppContext;
/*     */   }
/*     */ 
/*     */   public static final boolean isMainContext(AppContext paramAppContext)
/*     */   {
/* 376 */     return (paramAppContext != null) && (paramAppContext == mainAppContext);
/*     */   }
/*     */ 
/*     */   private static final AppContext getExecutionAppContext() {
/* 380 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 381 */     if ((localSecurityManager != null) && ((localSecurityManager instanceof AWTSecurityManager)))
/*     */     {
/* 384 */       AWTSecurityManager localAWTSecurityManager = (AWTSecurityManager)localSecurityManager;
/* 385 */       AppContext localAppContext = localAWTSecurityManager.getAppContext();
/* 386 */       return localAppContext;
/*     */     }
/* 388 */     return null;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */     throws IllegalThreadStateException
/*     */   {
/* 415 */     if (this.threadGroup.parentOf(Thread.currentThread().getThreadGroup())) {
/* 416 */       throw new IllegalThreadStateException("Current Thread is contained within AppContext to be disposed.");
/*     */     }
/*     */ 
/* 421 */     synchronized (this) {
/* 422 */       if (this.state != State.VALID) {
/* 423 */         return;
/*     */       }
/*     */ 
/* 426 */       this.state = State.BEING_DISPOSED;
/*     */     }
/*     */ 
/* 429 */     ??? = this.changeSupport;
/* 430 */     if (??? != null) {
/* 431 */       ((PropertyChangeSupport)???).firePropertyChange("disposed", false, true);
/*     */     }
/*     */ 
/* 437 */     final Object localObject2 = new Object();
/*     */ 
/* 439 */     Object localObject3 = new Runnable() {
/*     */       public void run() {
/* 441 */         Window[] arrayOfWindow1 = Window.getOwnerlessWindows();
/* 442 */         for (Window localWindow : arrayOfWindow1) {
/*     */           try {
/* 444 */             localWindow.dispose();
/*     */           } catch (Throwable localThrowable) {
/* 446 */             AppContext.log.finer("exception occured while disposing app context", localThrowable);
/*     */           }
/*     */         }
/* 449 */         AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public Object run() {
/* 451 */             if ((!GraphicsEnvironment.isHeadless()) && (SystemTray.isSupported()))
/*     */             {
/* 453 */               SystemTray localSystemTray = SystemTray.getSystemTray();
/* 454 */               TrayIcon[] arrayOfTrayIcon1 = localSystemTray.getTrayIcons();
/* 455 */               for (TrayIcon localTrayIcon : arrayOfTrayIcon1) {
/* 456 */                 localSystemTray.remove(localTrayIcon);
/*     */               }
/*     */             }
/* 459 */             return null;
/*     */           }
/*     */         });
/* 463 */         if (this.val$changeSupport != null) {
/* 464 */           this.val$changeSupport.firePropertyChange("guidisposed", false, true);
/*     */         }
/* 466 */         synchronized (localObject2) {
/* 467 */           localObject2.notifyAll();
/*     */         }
/*     */       }
/*     */     };
/* 471 */     synchronized (localObject2) {
/* 472 */       SunToolkit.postEvent(this, new InvocationEvent(Toolkit.getDefaultToolkit(), (Runnable)localObject3));
/*     */       try
/*     */       {
/* 475 */         localObject2.wait(this.DISPOSAL_TIMEOUT);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException1)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 483 */     localObject3 = new Runnable() {
/* 484 */       public void run() { synchronized (localObject2) {
/* 485 */           localObject2.notifyAll();
/*     */         }
/*     */       }
/*     */     };
/* 488 */     synchronized (localObject2) {
/* 489 */       SunToolkit.postEvent(this, new InvocationEvent(Toolkit.getDefaultToolkit(), (Runnable)localObject3));
/*     */       try
/*     */       {
/* 492 */         localObject2.wait(this.DISPOSAL_TIMEOUT);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException2) {
/*     */       }
/*     */     }
/* 497 */     synchronized (this) {
/* 498 */       this.state = State.DISPOSED;
/*     */     }
/*     */ 
/* 502 */     this.threadGroup.interrupt();
/*     */ 
/* 510 */     long l1 = System.currentTimeMillis();
/* 511 */     long l2 = l1 + this.THREAD_INTERRUPT_TIMEOUT;
/* 512 */     while ((this.threadGroup.activeCount() > 0) && (System.currentTimeMillis() < l2))
/*     */       try
/*     */       {
/* 515 */         Thread.sleep(10L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException3)
/*     */       {
/*     */       }
/* 520 */     this.threadGroup.stop();
/*     */ 
/* 525 */     l1 = System.currentTimeMillis();
/* 526 */     l2 = l1 + this.THREAD_INTERRUPT_TIMEOUT;
/* 527 */     while ((this.threadGroup.activeCount() > 0) && (System.currentTimeMillis() < l2))
/*     */       try
/*     */       {
/* 530 */         Thread.sleep(10L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException4)
/*     */       {
/*     */       }
/* 535 */     int i = this.threadGroup.activeGroupCount();
/* 536 */     if (i > 0) {
/* 537 */       ThreadGroup[] arrayOfThreadGroup = new ThreadGroup[i];
/* 538 */       i = this.threadGroup.enumerate(arrayOfThreadGroup);
/* 539 */       for (int j = 0; j < i; j++) {
/* 540 */         threadGroup2appContext.remove(arrayOfThreadGroup[j]);
/*     */       }
/*     */     }
/* 543 */     threadGroup2appContext.remove(this.threadGroup);
/*     */ 
/* 545 */     threadAppContext.set(null);
/*     */     try
/*     */     {
/* 549 */       this.threadGroup.destroy();
/*     */     }
/*     */     catch (IllegalThreadStateException localIllegalThreadStateException)
/*     */     {
/*     */     }
/* 554 */     synchronized (this.table) {
/* 555 */       this.table.clear();
/*     */     }
/*     */ 
/* 558 */     numAppContexts.decrementAndGet();
/*     */ 
/* 560 */     this.mostRecentKeyValue = null;
/*     */   }
/*     */ 
/*     */   static void stopEventDispatchThreads()
/*     */   {
/* 597 */     for (AppContext localAppContext : getAppContexts())
/* 598 */       if (!localAppContext.isDisposed())
/*     */       {
/* 601 */         PostShutdownEventRunnable localPostShutdownEventRunnable = new PostShutdownEventRunnable(localAppContext);
/*     */ 
/* 604 */         if (localAppContext != getAppContext())
/*     */         {
/* 607 */           CreateThreadAction localCreateThreadAction = new CreateThreadAction(localAppContext, localPostShutdownEventRunnable);
/* 608 */           Thread localThread = (Thread)AccessController.doPrivileged(localCreateThreadAction);
/* 609 */           localThread.start();
/*     */         } else {
/* 611 */           localPostShutdownEventRunnable.run();
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject)
/*     */   {
/* 634 */     synchronized (this.table)
/*     */     {
/* 641 */       MostRecentKeyValue localMostRecentKeyValue1 = this.mostRecentKeyValue;
/* 642 */       if ((localMostRecentKeyValue1 != null) && (localMostRecentKeyValue1.key == paramObject)) {
/* 643 */         return localMostRecentKeyValue1.value;
/*     */       }
/*     */ 
/* 646 */       Object localObject1 = this.table.get(paramObject);
/* 647 */       if (this.mostRecentKeyValue == null) {
/* 648 */         this.mostRecentKeyValue = new MostRecentKeyValue(paramObject, localObject1);
/* 649 */         this.shadowMostRecentKeyValue = new MostRecentKeyValue(paramObject, localObject1);
/*     */       } else {
/* 651 */         MostRecentKeyValue localMostRecentKeyValue2 = this.mostRecentKeyValue;
/* 652 */         this.shadowMostRecentKeyValue.setPair(paramObject, localObject1);
/* 653 */         this.mostRecentKeyValue = this.shadowMostRecentKeyValue;
/* 654 */         this.shadowMostRecentKeyValue = localMostRecentKeyValue2;
/*     */       }
/* 656 */       return localObject1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object put(Object paramObject1, Object paramObject2)
/*     */   {
/* 678 */     synchronized (this.table) {
/* 679 */       MostRecentKeyValue localMostRecentKeyValue = this.mostRecentKeyValue;
/* 680 */       if ((localMostRecentKeyValue != null) && (localMostRecentKeyValue.key == paramObject1))
/* 681 */         localMostRecentKeyValue.value = paramObject2;
/* 682 */       return this.table.put(paramObject1, paramObject2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object remove(Object paramObject)
/*     */   {
/* 697 */     synchronized (this.table) {
/* 698 */       MostRecentKeyValue localMostRecentKeyValue = this.mostRecentKeyValue;
/* 699 */       if ((localMostRecentKeyValue != null) && (localMostRecentKeyValue.key == paramObject))
/* 700 */         localMostRecentKeyValue.value = null;
/* 701 */       return this.table.remove(paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ThreadGroup getThreadGroup()
/*     */   {
/* 711 */     return this.threadGroup;
/*     */   }
/*     */ 
/*     */   public ClassLoader getContextClassLoader()
/*     */   {
/* 721 */     return this.contextClassLoader;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 730 */     return getClass().getName() + "[threadGroup=" + this.threadGroup.getName() + "]";
/*     */   }
/*     */ 
/*     */   public synchronized PropertyChangeListener[] getPropertyChangeListeners()
/*     */   {
/* 748 */     if (this.changeSupport == null) {
/* 749 */       return new PropertyChangeListener[0];
/*     */     }
/* 751 */     return this.changeSupport.getPropertyChangeListeners();
/*     */   }
/*     */ 
/*     */   public synchronized void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 780 */     if (paramPropertyChangeListener == null) {
/* 781 */       return;
/*     */     }
/* 783 */     if (this.changeSupport == null) {
/* 784 */       this.changeSupport = new PropertyChangeSupport(this);
/*     */     }
/* 786 */     this.changeSupport.addPropertyChangeListener(paramString, paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public synchronized void removePropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 806 */     if ((paramPropertyChangeListener == null) || (this.changeSupport == null)) {
/* 807 */       return;
/*     */     }
/* 809 */     this.changeSupport.removePropertyChangeListener(paramString, paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public synchronized PropertyChangeListener[] getPropertyChangeListeners(String paramString)
/*     */   {
/* 827 */     if (this.changeSupport == null) {
/* 828 */       return new PropertyChangeListener[0];
/*     */     }
/* 830 */     return this.changeSupport.getPropertyChangeListeners(paramString);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 835 */     SharedSecrets.setJavaAWTAccess(new JavaAWTAccess() {
/*     */       public Object get(Object paramAnonymousObject) {
/* 837 */         AppContext localAppContext = AppContext.getAppContext();
/* 838 */         return localAppContext == null ? null : localAppContext.get(paramAnonymousObject);
/*     */       }
/*     */       public void put(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/* 841 */         AppContext localAppContext = AppContext.getAppContext();
/* 842 */         if (localAppContext != null)
/* 843 */           localAppContext.put(paramAnonymousObject1, paramAnonymousObject2);
/*     */       }
/*     */ 
/*     */       public void remove(Object paramAnonymousObject) {
/* 847 */         AppContext localAppContext = AppContext.getAppContext();
/* 848 */         if (localAppContext != null)
/* 849 */           localAppContext.remove(paramAnonymousObject);
/*     */       }
/*     */ 
/*     */       public boolean isDisposed() {
/* 853 */         AppContext localAppContext = AppContext.getAppContext();
/* 854 */         return localAppContext == null ? true : localAppContext.isDisposed();
/*     */       }
/*     */       public boolean isMainAppContext() {
/* 857 */         return (AppContext.numAppContexts.get() == 1) && (AppContext.mainAppContext != null);
/*     */       }
/*     */ 
/*     */       private boolean hasRootThreadGroup(final AppContext paramAnonymousAppContext) {
/* 861 */         return ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Boolean run() {
/* 864 */             return Boolean.valueOf(paramAnonymousAppContext.threadGroup.getParent() == null);
/*     */           }
/*     */         })).booleanValue();
/*     */       }
/*     */ 
/*     */       public Object getAppletContext()
/*     */       {
/* 884 */         if (AppContext.numAppContexts.get() == 0) return null;
/*     */ 
/* 887 */         AppContext localAppContext = AppContext.access$900();
/*     */ 
/* 897 */         if (AppContext.numAppContexts.get() > 0)
/*     */         {
/* 904 */           localAppContext = localAppContext != null ? localAppContext : AppContext.getAppContext();
/*     */         }
/*     */ 
/* 914 */         int i = (localAppContext == null) || (AppContext.mainAppContext == localAppContext) || ((AppContext.mainAppContext == null) && (hasRootThreadGroup(localAppContext))) ? 1 : 0;
/*     */ 
/* 918 */         return i != 0 ? null : localAppContext;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static final class CreateThreadAction
/*     */     implements PrivilegedAction
/*     */   {
/*     */     private final AppContext appContext;
/*     */     private final Runnable runnable;
/*     */ 
/*     */     public CreateThreadAction(AppContext paramAppContext, Runnable paramRunnable)
/*     */     {
/* 583 */       this.appContext = paramAppContext;
/* 584 */       this.runnable = paramRunnable;
/*     */     }
/*     */ 
/*     */     public Object run() {
/* 588 */       Thread localThread = new Thread(this.appContext.getThreadGroup(), this.runnable);
/* 589 */       localThread.setContextClassLoader(this.appContext.getContextClassLoader());
/* 590 */       localThread.setPriority(6);
/* 591 */       localThread.setDaemon(true);
/* 592 */       return localThread;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class GetAppContextLock
/*     */   {
/*     */   }
/*     */ 
/*     */   static final class PostShutdownEventRunnable
/*     */     implements Runnable
/*     */   {
/*     */     private final AppContext appContext;
/*     */ 
/*     */     public PostShutdownEventRunnable(AppContext paramAppContext)
/*     */     {
/* 567 */       this.appContext = paramAppContext;
/*     */     }
/*     */ 
/*     */     public void run() {
/* 571 */       EventQueue localEventQueue = (EventQueue)this.appContext.get(AppContext.EVENT_QUEUE_KEY);
/* 572 */       if (localEventQueue != null)
/* 573 */         localEventQueue.postEvent(AWTAutoShutdown.getShutdownEvent());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static enum State
/*     */   {
/* 197 */     VALID, 
/* 198 */     BEING_DISPOSED, 
/* 199 */     DISPOSED;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.AppContext
 * JD-Core Version:    0.6.2
 */