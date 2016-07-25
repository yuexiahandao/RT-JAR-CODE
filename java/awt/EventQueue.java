/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.InputEvent;
/*      */ import java.awt.event.InputMethodEvent;
/*      */ import java.awt.event.InvocationEvent;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.PaintEvent;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.peer.ComponentPeer;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.EmptyStackException;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.EventQueueAccessor;
/*      */ import sun.awt.AWTAccessor.InvocationEventAccessor;
/*      */ import sun.awt.AWTAutoShutdown;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.EventQueueItem;
/*      */ import sun.awt.PeerEvent;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.awt.dnd.SunDropTargetEvent;
/*      */ import sun.misc.JavaSecurityAccess;
/*      */ import sun.misc.SharedSecrets;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public class EventQueue
/*      */ {
/*  104 */   private static final AtomicInteger threadInitNumber = new AtomicInteger(0);
/*      */   private static final int LOW_PRIORITY = 0;
/*      */   private static final int NORM_PRIORITY = 1;
/*      */   private static final int HIGH_PRIORITY = 2;
/*      */   private static final int ULTIMATE_PRIORITY = 3;
/*      */   private static final int NUM_PRIORITIES = 4;
/*  121 */   private Queue[] queues = new Queue[4];
/*      */   private EventQueue nextQueue;
/*      */   private EventQueue previousQueue;
/*      */   private final Lock pushPopLock;
/*      */   private final Condition pushPopCond;
/*  148 */   private static final Runnable dummyRunnable = new Runnable() {
/*  148 */     public void run() {  }  } ;
/*      */   private EventDispatchThread dispatchThread;
/*  155 */   private final ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
/*      */ 
/*  157 */   private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*      */ 
/*  163 */   private long mostRecentEventTime = System.currentTimeMillis();
/*      */   private WeakReference currentEvent;
/*      */   private volatile int waitForID;
/*      */   private final AppContext appContext;
/*  182 */   private final String name = "AWT-EventQueue-" + threadInitNumber.getAndIncrement();
/*      */   private static volatile PlatformLogger eventLog;
/*      */   private static final int PAINT = 0;
/*      */   private static final int UPDATE = 1;
/*      */   private static final int MOVE = 2;
/*      */   private static final int DRAG = 3;
/*      */   private static final int PEER = 4;
/*      */   private static final int CACHE_LENGTH = 5;
/*  661 */   private static final JavaSecurityAccess javaSecurityAccess = SharedSecrets.getJavaSecurityAccess();
/*      */ 
/*      */   private static final PlatformLogger getEventLog()
/*      */   {
/*  187 */     if (eventLog == null) {
/*  188 */       eventLog = PlatformLogger.getLogger("java.awt.event.EventQueue");
/*      */     }
/*  190 */     return eventLog;
/*      */   }
/*      */ 
/*      */   public EventQueue()
/*      */   {
/*  228 */     for (int i = 0; i < 4; i++) {
/*  229 */       this.queues[i] = new Queue();
/*      */     }
/*      */ 
/*  240 */     this.appContext = AppContext.getAppContext();
/*  241 */     this.pushPopLock = ((Lock)this.appContext.get(AppContext.EVENT_QUEUE_LOCK_KEY));
/*  242 */     this.pushPopCond = ((Condition)this.appContext.get(AppContext.EVENT_QUEUE_COND_KEY));
/*      */   }
/*      */ 
/*      */   public void postEvent(AWTEvent paramAWTEvent)
/*      */   {
/*  256 */     SunToolkit.flushPendingEvents(this.appContext);
/*  257 */     postEventPrivate(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   private final void postEventPrivate(AWTEvent paramAWTEvent)
/*      */   {
/*  270 */     paramAWTEvent.isPosted = true;
/*  271 */     this.pushPopLock.lock();
/*      */     try {
/*  273 */       if (this.nextQueue != null)
/*      */       {
/*  275 */         this.nextQueue.postEventPrivate(paramAWTEvent);
/*      */       }
/*      */       else {
/*  278 */         if (this.dispatchThread == null) {
/*  279 */           if (paramAWTEvent.getSource() == AWTAutoShutdown.getInstance()) {
/*      */             return;
/*      */           }
/*  282 */           initDispatchThread();
/*      */         }
/*      */ 
/*  285 */         postEvent(paramAWTEvent, getPriority(paramAWTEvent));
/*      */       }
/*      */     } finally { this.pushPopLock.unlock(); }
/*      */   }
/*      */ 
/*      */   private static int getPriority(AWTEvent paramAWTEvent)
/*      */   {
/*  292 */     if ((paramAWTEvent instanceof PeerEvent)) {
/*  293 */       PeerEvent localPeerEvent = (PeerEvent)paramAWTEvent;
/*  294 */       if ((localPeerEvent.getFlags() & 0x2) != 0L) {
/*  295 */         return 3;
/*      */       }
/*  297 */       if ((localPeerEvent.getFlags() & 1L) != 0L) {
/*  298 */         return 2;
/*      */       }
/*  300 */       if ((localPeerEvent.getFlags() & 0x4) != 0L) {
/*  301 */         return 0;
/*      */       }
/*      */     }
/*  304 */     int i = paramAWTEvent.getID();
/*  305 */     if ((i >= 800) && (i <= 801)) {
/*  306 */       return 0;
/*      */     }
/*  308 */     return 1;
/*      */   }
/*      */ 
/*      */   private void postEvent(AWTEvent paramAWTEvent, int paramInt)
/*      */   {
/*  320 */     if (coalesceEvent(paramAWTEvent, paramInt)) {
/*  321 */       return;
/*      */     }
/*      */ 
/*  324 */     EventQueueItem localEventQueueItem = new EventQueueItem(paramAWTEvent);
/*      */ 
/*  326 */     cacheEQItem(localEventQueueItem);
/*      */ 
/*  328 */     int i = paramAWTEvent.getID() == this.waitForID ? 1 : 0;
/*      */ 
/*  330 */     if (this.queues[paramInt].head == null) {
/*  331 */       boolean bool = noEvents();
/*  332 */       this.queues[paramInt].head = (this.queues[paramInt].tail = localEventQueueItem);
/*      */ 
/*  334 */       if (bool) {
/*  335 */         if (paramAWTEvent.getSource() != AWTAutoShutdown.getInstance()) {
/*  336 */           AWTAutoShutdown.getInstance().notifyThreadBusy(this.dispatchThread);
/*      */         }
/*  338 */         this.pushPopCond.signalAll();
/*  339 */       } else if (i != 0) {
/*  340 */         this.pushPopCond.signalAll();
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  345 */       this.queues[paramInt].tail.next = localEventQueueItem;
/*  346 */       this.queues[paramInt].tail = localEventQueueItem;
/*  347 */       if (i != 0)
/*  348 */         this.pushPopCond.signalAll();
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean coalescePaintEvent(PaintEvent paramPaintEvent)
/*      */   {
/*  354 */     ComponentPeer localComponentPeer = ((Component)paramPaintEvent.getSource()).peer;
/*  355 */     if (localComponentPeer != null) {
/*  356 */       localComponentPeer.coalescePaintEvent(paramPaintEvent);
/*      */     }
/*  358 */     EventQueueItem[] arrayOfEventQueueItem = ((Component)paramPaintEvent.getSource()).eventCache;
/*  359 */     if (arrayOfEventQueueItem == null) {
/*  360 */       return false;
/*      */     }
/*  362 */     int i = eventToCacheIndex(paramPaintEvent);
/*      */ 
/*  364 */     if ((i != -1) && (arrayOfEventQueueItem[i] != null)) {
/*  365 */       PaintEvent localPaintEvent = mergePaintEvents(paramPaintEvent, (PaintEvent)arrayOfEventQueueItem[i].event);
/*  366 */       if (localPaintEvent != null) {
/*  367 */         arrayOfEventQueueItem[i].event = localPaintEvent;
/*  368 */         return true;
/*      */       }
/*      */     }
/*  371 */     return false;
/*      */   }
/*      */ 
/*      */   private PaintEvent mergePaintEvents(PaintEvent paramPaintEvent1, PaintEvent paramPaintEvent2) {
/*  375 */     Rectangle localRectangle1 = paramPaintEvent1.getUpdateRect();
/*  376 */     Rectangle localRectangle2 = paramPaintEvent2.getUpdateRect();
/*  377 */     if (localRectangle2.contains(localRectangle1)) {
/*  378 */       return paramPaintEvent2;
/*      */     }
/*  380 */     if (localRectangle1.contains(localRectangle2)) {
/*  381 */       return paramPaintEvent1;
/*      */     }
/*  383 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean coalesceMouseEvent(MouseEvent paramMouseEvent) {
/*  387 */     if ((paramMouseEvent instanceof SunDropTargetEvent))
/*      */     {
/*  389 */       return false;
/*      */     }
/*  391 */     EventQueueItem[] arrayOfEventQueueItem = ((Component)paramMouseEvent.getSource()).eventCache;
/*  392 */     if (arrayOfEventQueueItem == null) {
/*  393 */       return false;
/*      */     }
/*  395 */     int i = eventToCacheIndex(paramMouseEvent);
/*  396 */     if ((i != -1) && (arrayOfEventQueueItem[i] != null)) {
/*  397 */       arrayOfEventQueueItem[i].event = paramMouseEvent;
/*  398 */       return true;
/*      */     }
/*  400 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean coalescePeerEvent(PeerEvent paramPeerEvent) {
/*  404 */     EventQueueItem[] arrayOfEventQueueItem = ((Component)paramPeerEvent.getSource()).eventCache;
/*  405 */     if (arrayOfEventQueueItem == null) {
/*  406 */       return false;
/*      */     }
/*  408 */     int i = eventToCacheIndex(paramPeerEvent);
/*  409 */     if ((i != -1) && (arrayOfEventQueueItem[i] != null)) {
/*  410 */       paramPeerEvent = paramPeerEvent.coalesceEvents((PeerEvent)arrayOfEventQueueItem[i].event);
/*  411 */       if (paramPeerEvent != null) {
/*  412 */         arrayOfEventQueueItem[i].event = paramPeerEvent;
/*  413 */         return true;
/*      */       }
/*  415 */       arrayOfEventQueueItem[i] = null;
/*      */     }
/*      */ 
/*  418 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean coalesceOtherEvent(AWTEvent paramAWTEvent, int paramInt)
/*      */   {
/*  429 */     int i = paramAWTEvent.getID();
/*  430 */     Component localComponent = (Component)paramAWTEvent.getSource();
/*  431 */     for (EventQueueItem localEventQueueItem = this.queues[paramInt].head; 
/*  432 */       localEventQueueItem != null; localEventQueueItem = localEventQueueItem.next)
/*      */     {
/*  435 */       if ((localEventQueueItem.event.getSource() == localComponent) && (localEventQueueItem.event.getID() == i)) {
/*  436 */         AWTEvent localAWTEvent = localComponent.coalesceEvents(localEventQueueItem.event, paramAWTEvent);
/*      */ 
/*  438 */         if (localAWTEvent != null) {
/*  439 */           localEventQueueItem.event = localAWTEvent;
/*  440 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  444 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean coalesceEvent(AWTEvent paramAWTEvent, int paramInt) {
/*  448 */     if (!(paramAWTEvent.getSource() instanceof Component)) {
/*  449 */       return false;
/*      */     }
/*  451 */     if ((paramAWTEvent instanceof PeerEvent)) {
/*  452 */       return coalescePeerEvent((PeerEvent)paramAWTEvent);
/*      */     }
/*      */ 
/*  455 */     if ((((Component)paramAWTEvent.getSource()).isCoalescingEnabled()) && (coalesceOtherEvent(paramAWTEvent, paramInt)))
/*      */     {
/*  458 */       return true;
/*      */     }
/*  460 */     if ((paramAWTEvent instanceof PaintEvent)) {
/*  461 */       return coalescePaintEvent((PaintEvent)paramAWTEvent);
/*      */     }
/*  463 */     if ((paramAWTEvent instanceof MouseEvent)) {
/*  464 */       return coalesceMouseEvent((MouseEvent)paramAWTEvent);
/*      */     }
/*  466 */     return false;
/*      */   }
/*      */ 
/*      */   private void cacheEQItem(EventQueueItem paramEventQueueItem) {
/*  470 */     if ((paramEventQueueItem.event instanceof SunDropTargetEvent))
/*      */     {
/*  472 */       return;
/*      */     }
/*  474 */     int i = eventToCacheIndex(paramEventQueueItem.event);
/*  475 */     if ((i != -1) && ((paramEventQueueItem.event.getSource() instanceof Component))) {
/*  476 */       Component localComponent = (Component)paramEventQueueItem.event.getSource();
/*  477 */       if (localComponent.eventCache == null) {
/*  478 */         localComponent.eventCache = new EventQueueItem[5];
/*      */       }
/*  480 */       localComponent.eventCache[i] = paramEventQueueItem;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void uncacheEQItem(EventQueueItem paramEventQueueItem) {
/*  485 */     int i = eventToCacheIndex(paramEventQueueItem.event);
/*  486 */     if ((i != -1) && ((paramEventQueueItem.event.getSource() instanceof Component))) {
/*  487 */       Component localComponent = (Component)paramEventQueueItem.event.getSource();
/*  488 */       if (localComponent.eventCache == null) {
/*  489 */         return;
/*      */       }
/*  491 */       localComponent.eventCache[i] = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int eventToCacheIndex(AWTEvent paramAWTEvent)
/*      */   {
/*  503 */     switch (paramAWTEvent.getID()) {
/*      */     case 800:
/*  505 */       return 0;
/*      */     case 801:
/*  507 */       return 1;
/*      */     case 503:
/*  509 */       return 2;
/*      */     case 506:
/*  511 */       return 3;
/*      */     }
/*  513 */     return (paramAWTEvent instanceof PeerEvent) ? 4 : -1;
/*      */   }
/*      */ 
/*      */   private boolean noEvents()
/*      */   {
/*  523 */     for (int i = 0; i < 4; i++) {
/*  524 */       if (this.queues[i].head != null) {
/*  525 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*  529 */     return true;
/*      */   }
/*      */ 
/*      */   public AWTEvent getNextEvent()
/*      */     throws InterruptedException
/*      */   {
/*      */     while (true)
/*      */     {
/*  547 */       SunToolkit.flushPendingEvents(this.appContext);
/*  548 */       this.pushPopLock.lock();
/*      */       try {
/*  550 */         AWTEvent localAWTEvent1 = getNextEventPrivate();
/*  551 */         if (localAWTEvent1 != null) {
/*  552 */           return localAWTEvent1;
/*      */         }
/*  554 */         AWTAutoShutdown.getInstance().notifyThreadFree(this.dispatchThread);
/*  555 */         this.pushPopCond.await();
/*      */       } finally {
/*  557 */         this.pushPopLock.unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   AWTEvent getNextEventPrivate()
/*      */     throws InterruptedException
/*      */   {
/*  566 */     for (int i = 3; i >= 0; i--) {
/*  567 */       if (this.queues[i].head != null) {
/*  568 */         EventQueueItem localEventQueueItem = this.queues[i].head;
/*  569 */         this.queues[i].head = localEventQueueItem.next;
/*  570 */         if (localEventQueueItem.next == null) {
/*  571 */           this.queues[i].tail = null;
/*      */         }
/*  573 */         uncacheEQItem(localEventQueueItem);
/*  574 */         return localEventQueueItem.event;
/*      */       }
/*      */     }
/*  577 */     return null;
/*      */   }
/*      */ 
/*      */   AWTEvent getNextEvent(int paramInt)
/*      */     throws InterruptedException
/*      */   {
/*      */     while (true)
/*      */     {
/*  587 */       SunToolkit.flushPendingEvents(this.appContext);
/*  588 */       this.pushPopLock.lock();
/*      */       try {
/*  590 */         for (int i = 0; i < 4; i++) {
/*  591 */           EventQueueItem localEventQueueItem1 = this.queues[i].head; EventQueueItem localEventQueueItem2 = null;
/*  592 */           for (; localEventQueueItem1 != null; localEventQueueItem1 = localEventQueueItem1.next)
/*      */           {
/*  594 */             if (localEventQueueItem1.event.getID() == paramInt) {
/*  595 */               if (localEventQueueItem2 == null)
/*  596 */                 this.queues[i].head = localEventQueueItem1.next;
/*      */               else {
/*  598 */                 localEventQueueItem2.next = localEventQueueItem1.next;
/*      */               }
/*  600 */               if (this.queues[i].tail == localEventQueueItem1) {
/*  601 */                 this.queues[i].tail = localEventQueueItem2;
/*      */               }
/*  603 */               uncacheEQItem(localEventQueueItem1);
/*  604 */               return localEventQueueItem1.event;
/*      */             }
/*  592 */             localEventQueueItem2 = localEventQueueItem1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  608 */         this.waitForID = paramInt;
/*  609 */         this.pushPopCond.await();
/*  610 */         this.waitForID = 0;
/*      */       } finally {
/*  612 */         this.pushPopLock.unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public AWTEvent peekEvent()
/*      */   {
/*  623 */     this.pushPopLock.lock();
/*      */     try {
/*  625 */       for (int i = 3; i >= 0; i--)
/*  626 */         if (this.queues[i].head != null)
/*  627 */           return this.queues[i].head.event;
/*      */     }
/*      */     finally
/*      */     {
/*  631 */       this.pushPopLock.unlock();
/*      */     }
/*      */ 
/*  634 */     return null;
/*      */   }
/*      */ 
/*      */   public AWTEvent peekEvent(int paramInt)
/*      */   {
/*  644 */     this.pushPopLock.lock();
/*      */     try {
/*  646 */       for (int i = 3; i >= 0; i--) {
/*  647 */         for (EventQueueItem localEventQueueItem = this.queues[i].head; 
/*  648 */           localEventQueueItem != null; localEventQueueItem = localEventQueueItem.next)
/*  649 */           if (localEventQueueItem.event.getID() == paramInt)
/*  650 */             return localEventQueueItem.event;
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/*  655 */       this.pushPopLock.unlock();
/*      */     }
/*      */ 
/*  658 */     return null;
/*      */   }
/*      */ 
/*      */   protected void dispatchEvent(final AWTEvent paramAWTEvent)
/*      */   {
/*  703 */     final Object localObject = paramAWTEvent.getSource();
/*  704 */     final PrivilegedAction local3 = new PrivilegedAction() {
/*      */       public Void run() {
/*  706 */         EventQueue.this.dispatchEventImpl(paramAWTEvent, localObject);
/*  707 */         return null;
/*      */       }
/*      */     };
/*  711 */     AccessControlContext localAccessControlContext1 = AccessController.getContext();
/*  712 */     AccessControlContext localAccessControlContext2 = getAccessControlContextFrom(localObject);
/*  713 */     final AccessControlContext localAccessControlContext3 = paramAWTEvent.getAccessControlContext();
/*  714 */     if (localAccessControlContext2 == null)
/*  715 */       javaSecurityAccess.doIntersectionPrivilege(local3, localAccessControlContext1, localAccessControlContext3);
/*      */     else
/*  717 */       javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction()
/*      */       {
/*      */         public Void run() {
/*  720 */           EventQueue.javaSecurityAccess.doIntersectionPrivilege(local3, localAccessControlContext3);
/*  721 */           return null;
/*      */         }
/*      */       }
/*      */       , localAccessControlContext1, localAccessControlContext2);
/*      */   }
/*      */ 
/*      */   private static AccessControlContext getAccessControlContextFrom(Object paramObject)
/*      */   {
/*  728 */     return (paramObject instanceof TrayIcon) ? ((TrayIcon)paramObject).getAccessControlContext() : (paramObject instanceof MenuComponent) ? ((MenuComponent)paramObject).getAccessControlContext() : (paramObject instanceof Component) ? ((Component)paramObject).getAccessControlContext() : null;
/*      */   }
/*      */ 
/*      */   private void dispatchEventImpl(AWTEvent paramAWTEvent, Object paramObject)
/*      */   {
/*  741 */     paramAWTEvent.isPosted = true;
/*  742 */     if ((paramAWTEvent instanceof ActiveEvent))
/*      */     {
/*  744 */       setCurrentEventAndMostRecentTimeImpl(paramAWTEvent);
/*  745 */       ((ActiveEvent)paramAWTEvent).dispatch();
/*  746 */     } else if ((paramObject instanceof Component)) {
/*  747 */       ((Component)paramObject).dispatchEvent(paramAWTEvent);
/*  748 */       paramAWTEvent.dispatched();
/*  749 */     } else if ((paramObject instanceof MenuComponent)) {
/*  750 */       ((MenuComponent)paramObject).dispatchEvent(paramAWTEvent);
/*  751 */     } else if ((paramObject instanceof TrayIcon)) {
/*  752 */       ((TrayIcon)paramObject).dispatchEvent(paramAWTEvent);
/*  753 */     } else if ((paramObject instanceof AWTAutoShutdown)) {
/*  754 */       if (noEvents()) {
/*  755 */         this.dispatchThread.stopDispatching();
/*      */       }
/*      */     }
/*  758 */     else if (getEventLog().isLoggable(500)) {
/*  759 */       getEventLog().fine("Unable to dispatch event: " + paramAWTEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static long getMostRecentEventTime()
/*      */   {
/*  793 */     return Toolkit.getEventQueue().getMostRecentEventTimeImpl();
/*      */   }
/*      */   private long getMostRecentEventTimeImpl() {
/*  796 */     this.pushPopLock.lock();
/*      */     try {
/*  798 */       return Thread.currentThread() == this.dispatchThread ? this.mostRecentEventTime : System.currentTimeMillis();
/*      */     }
/*      */     finally
/*      */     {
/*  802 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   long getMostRecentEventTimeEx()
/*      */   {
/*  810 */     this.pushPopLock.lock();
/*      */     try {
/*  812 */       return this.mostRecentEventTime;
/*      */     } finally {
/*  814 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static AWTEvent getCurrentEvent()
/*      */   {
/*  831 */     return Toolkit.getEventQueue().getCurrentEventImpl();
/*      */   }
/*      */   private AWTEvent getCurrentEventImpl() {
/*  834 */     this.pushPopLock.lock();
/*      */     try {
/*  836 */       return Thread.currentThread() == this.dispatchThread ? (AWTEvent)this.currentEvent.get() : null;
/*      */     }
/*      */     finally
/*      */     {
/*  840 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void push(EventQueue paramEventQueue)
/*      */   {
/*  856 */     if (getEventLog().isLoggable(500)) {
/*  857 */       getEventLog().fine("EventQueue.push(" + paramEventQueue + ")");
/*      */     }
/*      */ 
/*  860 */     this.pushPopLock.lock();
/*      */     try {
/*  862 */       EventQueue localEventQueue = this;
/*  863 */       while (localEventQueue.nextQueue != null) {
/*  864 */         localEventQueue = localEventQueue.nextQueue;
/*      */       }
/*      */ 
/*  867 */       if ((localEventQueue.dispatchThread != null) && (localEventQueue.dispatchThread.getEventQueue() == this))
/*      */       {
/*  870 */         paramEventQueue.dispatchThread = localEventQueue.dispatchThread;
/*  871 */         localEventQueue.dispatchThread.setEventQueue(paramEventQueue);
/*      */       }
/*      */ 
/*  875 */       while (localEventQueue.peekEvent() != null) {
/*      */         try
/*      */         {
/*  878 */           paramEventQueue.postEventPrivate(localEventQueue.getNextEventPrivate());
/*      */         } catch (InterruptedException localInterruptedException) {
/*  880 */           if (getEventLog().isLoggable(500)) {
/*  881 */             getEventLog().fine("Interrupted push", localInterruptedException);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  890 */       localEventQueue.postEventPrivate(new InvocationEvent(localEventQueue, dummyRunnable));
/*      */ 
/*  892 */       paramEventQueue.previousQueue = localEventQueue;
/*  893 */       localEventQueue.nextQueue = paramEventQueue;
/*      */ 
/*  895 */       if (this.appContext.get(AppContext.EVENT_QUEUE_KEY) == localEventQueue) {
/*  896 */         this.appContext.put(AppContext.EVENT_QUEUE_KEY, paramEventQueue);
/*      */       }
/*      */ 
/*  899 */       this.pushPopCond.signalAll();
/*      */     } finally {
/*  901 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void pop()
/*      */     throws EmptyStackException
/*      */   {
/*  919 */     if (getEventLog().isLoggable(500)) {
/*  920 */       getEventLog().fine("EventQueue.pop(" + this + ")");
/*      */     }
/*      */ 
/*  923 */     this.pushPopLock.lock();
/*      */     try {
/*  925 */       EventQueue localEventQueue1 = this;
/*  926 */       while (localEventQueue1.nextQueue != null) {
/*  927 */         localEventQueue1 = localEventQueue1.nextQueue;
/*      */       }
/*  929 */       EventQueue localEventQueue2 = localEventQueue1.previousQueue;
/*  930 */       if (localEventQueue2 == null) {
/*  931 */         throw new EmptyStackException();
/*      */       }
/*      */ 
/*  934 */       localEventQueue1.previousQueue = null;
/*  935 */       localEventQueue2.nextQueue = null;
/*      */ 
/*  938 */       while (localEventQueue1.peekEvent() != null) {
/*      */         try {
/*  940 */           localEventQueue2.postEventPrivate(localEventQueue1.getNextEventPrivate());
/*      */         } catch (InterruptedException localInterruptedException) {
/*  942 */           if (getEventLog().isLoggable(500)) {
/*  943 */             getEventLog().fine("Interrupted pop", localInterruptedException);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  948 */       if ((localEventQueue1.dispatchThread != null) && (localEventQueue1.dispatchThread.getEventQueue() == this))
/*      */       {
/*  951 */         localEventQueue2.dispatchThread = localEventQueue1.dispatchThread;
/*  952 */         localEventQueue1.dispatchThread.setEventQueue(localEventQueue2);
/*      */       }
/*      */ 
/*  955 */       if (this.appContext.get(AppContext.EVENT_QUEUE_KEY) == this) {
/*  956 */         this.appContext.put(AppContext.EVENT_QUEUE_KEY, localEventQueue2);
/*      */       }
/*      */ 
/*  961 */       localEventQueue1.postEventPrivate(new InvocationEvent(localEventQueue1, dummyRunnable));
/*      */ 
/*  963 */       this.pushPopCond.signalAll();
/*      */     } finally {
/*  965 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public SecondaryLoop createSecondaryLoop()
/*      */   {
/*  985 */     return createSecondaryLoop(null, null, 0L);
/*      */   }
/*      */ 
/*      */   SecondaryLoop createSecondaryLoop(Conditional paramConditional, EventFilter paramEventFilter, long paramLong) {
/*  989 */     this.pushPopLock.lock();
/*      */     try
/*      */     {
/*      */       Object localObject1;
/*  991 */       if (this.nextQueue != null)
/*      */       {
/*  993 */         return this.nextQueue.createSecondaryLoop(paramConditional, paramEventFilter, paramLong);
/*      */       }
/*  995 */       if (this.dispatchThread == null) {
/*  996 */         initDispatchThread();
/*      */       }
/*  998 */       return new WaitDispatchSupport(this.dispatchThread, paramConditional, paramEventFilter, paramLong);
/*      */     } finally {
/* 1000 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static boolean isDispatchThread()
/*      */   {
/* 1025 */     EventQueue localEventQueue = Toolkit.getEventQueue();
/* 1026 */     return localEventQueue.isDispatchThreadImpl();
/*      */   }
/*      */ 
/*      */   final boolean isDispatchThreadImpl() {
/* 1030 */     Object localObject1 = this;
/* 1031 */     this.pushPopLock.lock();
/*      */     try {
/* 1033 */       EventQueue localEventQueue = ((EventQueue)localObject1).nextQueue;
/* 1034 */       while (localEventQueue != null) {
/* 1035 */         localObject1 = localEventQueue;
/* 1036 */         localEventQueue = ((EventQueue)localObject1).nextQueue;
/*      */       }
/* 1038 */       return Thread.currentThread() == ((EventQueue)localObject1).dispatchThread;
/*      */     } finally {
/* 1040 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   final void initDispatchThread() {
/* 1045 */     this.pushPopLock.lock();
/*      */     try {
/* 1047 */       if ((this.dispatchThread == null) && (!this.threadGroup.isDestroyed()) && (!this.appContext.isDisposed())) {
/* 1048 */         this.dispatchThread = ((EventDispatchThread)AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public EventDispatchThread run() {
/* 1051 */             EventDispatchThread localEventDispatchThread = new EventDispatchThread(EventQueue.this.threadGroup, EventQueue.this.name, EventQueue.this);
/*      */ 
/* 1055 */             localEventDispatchThread.setContextClassLoader(EventQueue.this.classLoader);
/* 1056 */             localEventDispatchThread.setPriority(6);
/* 1057 */             localEventDispatchThread.setDaemon(false);
/* 1058 */             AWTAutoShutdown.getInstance().notifyThreadBusy(localEventDispatchThread);
/* 1059 */             return localEventDispatchThread;
/*      */           }
/*      */         }));
/* 1063 */         this.dispatchThread.start();
/*      */       }
/*      */     } finally {
/* 1066 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   final boolean detachDispatchThread(EventDispatchThread paramEventDispatchThread, boolean paramBoolean)
/*      */   {
/* 1079 */     this.pushPopLock.lock();
/*      */     try
/*      */     {
/*      */       boolean bool;
/* 1081 */       if (paramEventDispatchThread == this.dispatchThread)
/*      */       {
/* 1089 */         if (((!paramBoolean) && (peekEvent() != null)) || (!SunToolkit.isPostEventQueueEmpty())) {
/* 1090 */           return false;
/*      */         }
/* 1092 */         this.dispatchThread = null;
/*      */       }
/* 1094 */       AWTAutoShutdown.getInstance().notifyThreadFree(paramEventDispatchThread);
/* 1095 */       return true;
/*      */     } finally {
/* 1097 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   final EventDispatchThread getDispatchThread()
/*      */   {
/* 1111 */     this.pushPopLock.lock();
/*      */     try {
/* 1113 */       return this.dispatchThread;
/*      */     } finally {
/* 1115 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   final void removeSourceEvents(Object paramObject, boolean paramBoolean)
/*      */   {
/* 1132 */     SunToolkit.flushPendingEvents(this.appContext);
/* 1133 */     this.pushPopLock.lock();
/*      */     try {
/* 1135 */       for (int i = 0; i < 4; i++) {
/* 1136 */         EventQueueItem localEventQueueItem1 = this.queues[i].head;
/* 1137 */         EventQueueItem localEventQueueItem2 = null;
/* 1138 */         while (localEventQueueItem1 != null) {
/* 1139 */           if ((localEventQueueItem1.event.getSource() == paramObject) && ((paramBoolean) || ((!(localEventQueueItem1.event instanceof SequencedEvent)) && (!(localEventQueueItem1.event instanceof SentEvent)) && (!(localEventQueueItem1.event instanceof FocusEvent)) && (!(localEventQueueItem1.event instanceof WindowEvent)) && (!(localEventQueueItem1.event instanceof KeyEvent)) && (!(localEventQueueItem1.event instanceof InputMethodEvent)))))
/*      */           {
/* 1148 */             if ((localEventQueueItem1.event instanceof SequencedEvent)) {
/* 1149 */               ((SequencedEvent)localEventQueueItem1.event).dispose();
/*      */             }
/* 1151 */             if ((localEventQueueItem1.event instanceof SentEvent)) {
/* 1152 */               ((SentEvent)localEventQueueItem1.event).dispose();
/*      */             }
/* 1154 */             if ((localEventQueueItem1.event instanceof InvocationEvent)) {
/* 1155 */               AWTAccessor.getInvocationEventAccessor().dispose((InvocationEvent)localEventQueueItem1.event);
/*      */             }
/*      */ 
/* 1158 */             if (localEventQueueItem2 == null)
/* 1159 */               this.queues[i].head = localEventQueueItem1.next;
/*      */             else {
/* 1161 */               localEventQueueItem2.next = localEventQueueItem1.next;
/*      */             }
/* 1163 */             uncacheEQItem(localEventQueueItem1);
/*      */           } else {
/* 1165 */             localEventQueueItem2 = localEventQueueItem1;
/*      */           }
/* 1167 */           localEventQueueItem1 = localEventQueueItem1.next;
/*      */         }
/* 1169 */         this.queues[i].tail = localEventQueueItem2;
/*      */       }
/*      */     } finally {
/* 1172 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   static void setCurrentEventAndMostRecentTime(AWTEvent paramAWTEvent) {
/* 1177 */     Toolkit.getEventQueue().setCurrentEventAndMostRecentTimeImpl(paramAWTEvent);
/*      */   }
/*      */   private void setCurrentEventAndMostRecentTimeImpl(AWTEvent paramAWTEvent) {
/* 1180 */     this.pushPopLock.lock();
/*      */     try {
/* 1182 */       if (Thread.currentThread() != this.dispatchThread)
/*      */       {
/*      */         return;
/*      */       }
/* 1186 */       this.currentEvent = new WeakReference(paramAWTEvent);
/*      */ 
/* 1196 */       long l = -9223372036854775808L;
/*      */       Object localObject1;
/* 1197 */       if ((paramAWTEvent instanceof InputEvent)) {
/* 1198 */         localObject1 = (InputEvent)paramAWTEvent;
/* 1199 */         l = ((InputEvent)localObject1).getWhen();
/* 1200 */       } else if ((paramAWTEvent instanceof InputMethodEvent)) {
/* 1201 */         localObject1 = (InputMethodEvent)paramAWTEvent;
/* 1202 */         l = ((InputMethodEvent)localObject1).getWhen();
/* 1203 */       } else if ((paramAWTEvent instanceof ActionEvent)) {
/* 1204 */         localObject1 = (ActionEvent)paramAWTEvent;
/* 1205 */         l = ((ActionEvent)localObject1).getWhen();
/* 1206 */       } else if ((paramAWTEvent instanceof InvocationEvent)) {
/* 1207 */         localObject1 = (InvocationEvent)paramAWTEvent;
/* 1208 */         l = ((InvocationEvent)localObject1).getWhen();
/*      */       }
/* 1210 */       this.mostRecentEventTime = Math.max(this.mostRecentEventTime, l);
/*      */     } finally {
/* 1212 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void invokeLater(Runnable paramRunnable)
/*      */   {
/* 1233 */     Toolkit.getEventQueue().postEvent(new InvocationEvent(Toolkit.getDefaultToolkit(), paramRunnable));
/*      */   }
/*      */ 
/*      */   public static void invokeAndWait(Runnable paramRunnable)
/*      */     throws InterruptedException, InvocationTargetException
/*      */   {
/* 1263 */     invokeAndWait(Toolkit.getDefaultToolkit(), paramRunnable);
/*      */   }
/*      */ 
/*      */   static void invokeAndWait(Object paramObject, Runnable paramRunnable)
/*      */     throws InterruptedException, InvocationTargetException
/*      */   {
/* 1269 */     if (isDispatchThread()) {
/* 1270 */       throw new Error("Cannot call invokeAndWait from the event dispatcher thread");
/*      */     }
/*      */ 
/* 1274 */     Object local1AWTInvocationLock = new Object()
/*      */     {
/*      */     };
/* 1276 */     InvocationEvent localInvocationEvent = new InvocationEvent(paramObject, paramRunnable, local1AWTInvocationLock, true);
/*      */ 
/* 1279 */     synchronized (local1AWTInvocationLock) {
/* 1280 */       Toolkit.getEventQueue().postEvent(localInvocationEvent);
/* 1281 */       while (!localInvocationEvent.isDispatched()) {
/* 1282 */         local1AWTInvocationLock.wait();
/*      */       }
/*      */     }
/*      */ 
/* 1286 */     ??? = localInvocationEvent.getThrowable();
/* 1287 */     if (??? != null)
/* 1288 */       throw new InvocationTargetException((Throwable)???);
/*      */   }
/*      */ 
/*      */   private void wakeup(boolean paramBoolean)
/*      */   {
/* 1299 */     this.pushPopLock.lock();
/*      */     try {
/* 1301 */       if (this.nextQueue != null)
/*      */       {
/* 1303 */         this.nextQueue.wakeup(paramBoolean);
/* 1304 */       } else if (this.dispatchThread != null)
/* 1305 */         this.pushPopCond.signalAll();
/* 1306 */       else if (!paramBoolean)
/* 1307 */         initDispatchThread();
/*      */     }
/*      */     finally {
/* 1310 */       this.pushPopLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  194 */     AWTAccessor.setEventQueueAccessor(new AWTAccessor.EventQueueAccessor()
/*      */     {
/*      */       public Thread getDispatchThread(EventQueue paramAnonymousEventQueue) {
/*  197 */         return paramAnonymousEventQueue.getDispatchThread();
/*      */       }
/*      */       public boolean isDispatchThreadImpl(EventQueue paramAnonymousEventQueue) {
/*  200 */         return paramAnonymousEventQueue.isDispatchThreadImpl();
/*      */       }
/*      */ 
/*      */       public void removeSourceEvents(EventQueue paramAnonymousEventQueue, Object paramAnonymousObject, boolean paramAnonymousBoolean)
/*      */       {
/*  206 */         paramAnonymousEventQueue.removeSourceEvents(paramAnonymousObject, paramAnonymousBoolean);
/*      */       }
/*      */       public boolean noEvents(EventQueue paramAnonymousEventQueue) {
/*  209 */         return paramAnonymousEventQueue.noEvents();
/*      */       }
/*      */       public void wakeup(EventQueue paramAnonymousEventQueue, boolean paramAnonymousBoolean) {
/*  212 */         paramAnonymousEventQueue.wakeup(paramAnonymousBoolean);
/*      */       }
/*      */ 
/*      */       public void invokeAndWait(Object paramAnonymousObject, Runnable paramAnonymousRunnable) throws InterruptedException, InvocationTargetException
/*      */       {
/*  217 */         EventQueue.invokeAndWait(paramAnonymousObject, paramAnonymousRunnable);
/*      */       }
/*      */ 
/*      */       public long getMostRecentEventTime(EventQueue paramAnonymousEventQueue)
/*      */       {
/*  222 */         return paramAnonymousEventQueue.getMostRecentEventTimeImpl();
/*      */       }
/*      */     });
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.EventQueue
 * JD-Core Version:    0.6.2
 */