/*     */ package java.awt;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import sun.awt.PeerEvent;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ class WaitDispatchSupport
/*     */   implements SecondaryLoop
/*     */ {
/*  50 */   private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.event.WaitDispatchSupport");
/*     */   private EventDispatchThread dispatchThread;
/*     */   private EventFilter filter;
/*     */   private volatile Conditional extCondition;
/*     */   private volatile Conditional condition;
/*     */   private long interval;
/*     */   private static Timer timer;
/*     */   private TimerTask timerTask;
/*  66 */   private AtomicBoolean keepBlockingEDT = new AtomicBoolean(false);
/*  67 */   private AtomicBoolean keepBlockingCT = new AtomicBoolean(false);
/*     */ 
/* 286 */   private final Runnable wakingRunnable = new Runnable() {
/*     */     public void run() {
/* 288 */       WaitDispatchSupport.log.fine("Wake up EDT");
/* 289 */       synchronized (WaitDispatchSupport.access$900()) {
/* 290 */         WaitDispatchSupport.this.keepBlockingCT.set(false);
/* 291 */         WaitDispatchSupport.access$900().notifyAll();
/*     */       }
/* 293 */       WaitDispatchSupport.log.fine("Wake up EDT done");
/*     */     }
/* 286 */   };
/*     */ 
/*     */   private static synchronized void initializeTimer()
/*     */   {
/*  70 */     if (timer == null)
/*  71 */       timer = new Timer("AWT-WaitDispatchSupport-Timer", true);
/*     */   }
/*     */ 
/*     */   public WaitDispatchSupport(EventDispatchThread paramEventDispatchThread)
/*     */   {
/*  85 */     this(paramEventDispatchThread, null);
/*     */   }
/*     */ 
/*     */   public WaitDispatchSupport(EventDispatchThread paramEventDispatchThread, Conditional paramConditional)
/*     */   {
/* 102 */     if (paramEventDispatchThread == null) {
/* 103 */       throw new IllegalArgumentException("The dispatchThread can not be null");
/*     */     }
/*     */ 
/* 106 */     this.dispatchThread = paramEventDispatchThread;
/* 107 */     this.extCondition = paramConditional;
/* 108 */     this.condition = new Conditional()
/*     */     {
/*     */       public boolean evaluate() {
/* 111 */         if (WaitDispatchSupport.log.isLoggable(300)) {
/* 112 */           WaitDispatchSupport.log.finest("evaluate(): blockingEDT=" + WaitDispatchSupport.this.keepBlockingEDT.get() + ", blockingCT=" + WaitDispatchSupport.this.keepBlockingCT.get());
/*     */         }
/*     */ 
/* 115 */         int i = WaitDispatchSupport.this.extCondition != null ? WaitDispatchSupport.this.extCondition.evaluate() : 1;
/*     */ 
/* 117 */         if ((!WaitDispatchSupport.this.keepBlockingEDT.get()) || (i == 0)) {
/* 118 */           if (WaitDispatchSupport.this.timerTask != null) {
/* 119 */             WaitDispatchSupport.this.timerTask.cancel();
/* 120 */             WaitDispatchSupport.this.timerTask = null;
/*     */           }
/* 122 */           return false;
/*     */         }
/* 124 */         return true;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public WaitDispatchSupport(EventDispatchThread paramEventDispatchThread, Conditional paramConditional, EventFilter paramEventFilter, long paramLong)
/*     */   {
/* 152 */     this(paramEventDispatchThread, paramConditional);
/* 153 */     this.filter = paramEventFilter;
/* 154 */     if (paramLong < 0L) {
/* 155 */       throw new IllegalArgumentException("The interval value must be >= 0");
/*     */     }
/* 157 */     this.interval = paramLong;
/* 158 */     if (paramLong != 0L)
/* 159 */       initializeTimer();
/*     */   }
/*     */ 
/*     */   public boolean enter()
/*     */   {
/* 168 */     log.fine("enter(): blockingEDT=" + this.keepBlockingEDT.get() + ", blockingCT=" + this.keepBlockingCT.get());
/*     */ 
/* 171 */     if (!this.keepBlockingEDT.compareAndSet(false, true)) {
/* 172 */       log.fine("The secondary loop is already running, aborting");
/* 173 */       return false;
/*     */     }
/*     */ 
/* 176 */     final Runnable local2 = new Runnable() {
/*     */       public void run() {
/* 178 */         WaitDispatchSupport.log.fine("Starting a new event pump");
/* 179 */         if (WaitDispatchSupport.this.filter == null)
/* 180 */           WaitDispatchSupport.this.dispatchThread.pumpEvents(WaitDispatchSupport.this.condition);
/*     */         else
/* 182 */           WaitDispatchSupport.this.dispatchThread.pumpEventsForFilter(WaitDispatchSupport.this.condition, WaitDispatchSupport.this.filter);
/*     */       }
/*     */     };
/* 191 */     Thread localThread = Thread.currentThread();
/* 192 */     if (localThread == this.dispatchThread) {
/* 193 */       log.finest("On dispatch thread: " + this.dispatchThread);
/* 194 */       if (this.interval != 0L) {
/* 195 */         log.finest("scheduling the timer for " + this.interval + " ms");
/* 196 */         timer.schedule(this.timerTask = new TimerTask()
/*     */         {
/*     */           public void run() {
/* 199 */             if (WaitDispatchSupport.this.keepBlockingEDT.compareAndSet(true, false))
/* 200 */               WaitDispatchSupport.this.wakeupEDT();
/*     */           }
/*     */         }
/*     */         , this.interval);
/*     */       }
/*     */ 
/* 207 */       SequencedEvent localSequencedEvent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getCurrentSequencedEvent();
/*     */ 
/* 209 */       if (localSequencedEvent != null) {
/* 210 */         log.fine("Dispose current SequencedEvent: " + localSequencedEvent);
/* 211 */         localSequencedEvent.dispose();
/*     */       }
/*     */ 
/* 219 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Object run() {
/* 221 */           local2.run();
/* 222 */           return null;
/*     */         } } );
/*     */     }
/*     */     else {
/* 226 */       log.finest("On non-dispatch thread: " + localThread);
/* 227 */       synchronized (getTreeLock()) {
/* 228 */         if (this.filter != null)
/* 229 */           this.dispatchThread.addEventFilter(this.filter);
/*     */         try
/*     */         {
/* 232 */           EventQueue localEventQueue = this.dispatchThread.getEventQueue();
/* 233 */           localEventQueue.postEvent(new PeerEvent(this, local2, 1L));
/* 234 */           this.keepBlockingCT.set(true);
/* 235 */           if (this.interval > 0L) {
/* 236 */             long l = System.currentTimeMillis();
/*     */ 
/* 238 */             while ((this.keepBlockingCT.get()) && ((this.extCondition == null) || (this.extCondition.evaluate())) && (l + this.interval > System.currentTimeMillis()))
/*     */             {
/* 241 */               getTreeLock().wait(this.interval);
/*     */             }
/*     */           } else {
/* 244 */             while ((this.keepBlockingCT.get()) && ((this.extCondition == null) || (this.extCondition.evaluate())))
/*     */             {
/* 247 */               getTreeLock().wait();
/*     */             }
/*     */           }
/* 250 */           log.fine("waitDone " + this.keepBlockingEDT.get() + " " + this.keepBlockingCT.get());
/*     */         } catch (InterruptedException localInterruptedException) {
/* 252 */           log.fine("Exception caught while waiting: " + localInterruptedException);
/*     */         } finally {
/* 254 */           if (this.filter != null) {
/* 255 */             this.dispatchThread.removeEventFilter(this.filter);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 261 */         this.keepBlockingEDT.set(false);
/* 262 */         this.keepBlockingCT.set(false);
/*     */       }
/*     */     }
/*     */ 
/* 266 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean exit()
/*     */   {
/* 273 */     log.fine("exit(): blockingEDT=" + this.keepBlockingEDT.get() + ", blockingCT=" + this.keepBlockingCT.get());
/*     */ 
/* 275 */     if (this.keepBlockingEDT.compareAndSet(true, false)) {
/* 276 */       wakeupEDT();
/* 277 */       return true;
/*     */     }
/* 279 */     return false;
/*     */   }
/*     */ 
/*     */   private static final Object getTreeLock() {
/* 283 */     return Component.LOCK;
/*     */   }
/*     */ 
/*     */   private void wakeupEDT()
/*     */   {
/* 298 */     log.finest("wakeupEDT(): EDT == " + this.dispatchThread);
/* 299 */     EventQueue localEventQueue = this.dispatchThread.getEventQueue();
/* 300 */     localEventQueue.postEvent(new PeerEvent(this, this.wakingRunnable, 1L));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.WaitDispatchSupport
 * JD-Core Version:    0.6.2
 */