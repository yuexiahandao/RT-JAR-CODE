/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import sun.misc.ThreadGroupUtils;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public final class AWTAutoShutdown
/*     */   implements Runnable
/*     */ {
/*  66 */   private static final AWTAutoShutdown theInstance = new AWTAutoShutdown();
/*     */ 
/*  71 */   private final Object mainLock = new Object();
/*     */ 
/*  79 */   private final Object activationLock = new Object();
/*     */ 
/*  87 */   private final HashSet busyThreadSet = new HashSet(7);
/*     */ 
/*  93 */   private boolean toolkitThreadBusy = false;
/*     */ 
/*  99 */   private final Map peerMap = new IdentityHashMap();
/*     */ 
/* 105 */   private Thread blockerThread = null;
/*     */ 
/* 111 */   private boolean timeoutPassed = false;
/*     */   private static final int SAFETY_TIMEOUT = 1000;
/*     */ 
/*     */   public static AWTAutoShutdown getInstance()
/*     */   {
/* 131 */     return theInstance;
/*     */   }
/*     */ 
/*     */   public static void notifyToolkitThreadBusy()
/*     */   {
/* 143 */     getInstance().setToolkitBusy(true);
/*     */   }
/*     */ 
/*     */   public static void notifyToolkitThreadFree()
/*     */   {
/* 155 */     getInstance().setToolkitBusy(false);
/*     */   }
/*     */ 
/*     */   public void notifyThreadBusy(Thread paramThread)
/*     */   {
/* 168 */     if (paramThread == null) {
/* 169 */       return;
/*     */     }
/* 171 */     synchronized (this.activationLock) {
/* 172 */       synchronized (this.mainLock) {
/* 173 */         if (this.blockerThread == null) {
/* 174 */           activateBlockerThread();
/* 175 */         } else if (isReadyToShutdown()) {
/* 176 */           this.mainLock.notifyAll();
/* 177 */           this.timeoutPassed = false;
/*     */         }
/* 179 */         this.busyThreadSet.add(paramThread);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void notifyThreadFree(Thread paramThread)
/*     */   {
/* 194 */     if (paramThread == null) {
/* 195 */       return;
/*     */     }
/* 197 */     synchronized (this.activationLock) {
/* 198 */       synchronized (this.mainLock) {
/* 199 */         this.busyThreadSet.remove(paramThread);
/* 200 */         if (isReadyToShutdown()) {
/* 201 */           this.mainLock.notifyAll();
/* 202 */           this.timeoutPassed = false;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void notifyPeerMapUpdated()
/*     */   {
/* 215 */     synchronized (this.activationLock) {
/* 216 */       synchronized (this.mainLock) {
/* 217 */         if ((!isReadyToShutdown()) && (this.blockerThread == null)) {
/* 218 */           AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Void run() {
/* 221 */               AWTAutoShutdown.this.activateBlockerThread();
/* 222 */               return null;
/*     */             } } );
/*     */         }
/*     */         else {
/* 226 */           this.mainLock.notifyAll();
/* 227 */           this.timeoutPassed = false;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isReadyToShutdown()
/*     */   {
/* 242 */     return (!this.toolkitThreadBusy) && (this.peerMap.isEmpty()) && (this.busyThreadSet.isEmpty());
/*     */   }
/*     */ 
/*     */   private void setToolkitBusy(boolean paramBoolean)
/*     */   {
/* 257 */     if (paramBoolean != this.toolkitThreadBusy)
/* 258 */       synchronized (this.activationLock) {
/* 259 */         synchronized (this.mainLock) {
/* 260 */           if (paramBoolean != this.toolkitThreadBusy)
/* 261 */             if (paramBoolean) {
/* 262 */               if (this.blockerThread == null) {
/* 263 */                 activateBlockerThread();
/* 264 */               } else if (isReadyToShutdown()) {
/* 265 */                 this.mainLock.notifyAll();
/* 266 */                 this.timeoutPassed = false;
/*     */               }
/* 268 */               this.toolkitThreadBusy = paramBoolean;
/*     */             } else {
/* 270 */               this.toolkitThreadBusy = paramBoolean;
/* 271 */               if (isReadyToShutdown()) {
/* 272 */                 this.mainLock.notifyAll();
/* 273 */                 this.timeoutPassed = false;
/*     */               }
/*     */             }
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 289 */     Thread localThread = Thread.currentThread();
/* 290 */     int i = 0;
/* 291 */     synchronized (this.mainLock)
/*     */     {
/*     */       try {
/* 294 */         this.mainLock.notifyAll();
/* 295 */         label83: while (this.blockerThread == localThread) {
/* 296 */           this.mainLock.wait();
/* 297 */           this.timeoutPassed = false;
/*     */           while (true)
/*     */           {
/* 308 */             if (!isReadyToShutdown()) break label83;
/* 309 */             if (this.timeoutPassed) {
/* 310 */               this.timeoutPassed = false;
/* 311 */               this.blockerThread = null;
/* 312 */               break;
/*     */             }
/* 314 */             this.timeoutPassed = true;
/* 315 */             this.mainLock.wait(1000L);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 321 */         if (this.blockerThread == localThread)
/* 322 */           this.blockerThread = null;
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/* 319 */         i = 1;
/*     */ 
/* 321 */         if (this.blockerThread == localThread)
/* 322 */           this.blockerThread = null;
/*     */       }
/*     */       finally
/*     */       {
/* 321 */         if (this.blockerThread == localThread) {
/* 322 */           this.blockerThread = null;
/*     */         }
/*     */       }
/*     */     }
/* 326 */     if (i == 0)
/* 327 */       AppContext.stopEventDispatchThreads();
/*     */   }
/*     */ 
/*     */   static AWTEvent getShutdownEvent()
/*     */   {
/* 332 */     return new AWTEvent(getInstance(), 0)
/*     */     {
/*     */     };
/*     */   }
/*     */ 
/*     */   private void activateBlockerThread()
/*     */   {
/* 342 */     Thread localThread = new Thread(ThreadGroupUtils.getRootThreadGroup(), this, "AWT-Shutdown");
/* 343 */     localThread.setContextClassLoader(null);
/* 344 */     localThread.setDaemon(false);
/* 345 */     this.blockerThread = localThread;
/* 346 */     localThread.start();
/*     */     try
/*     */     {
/* 349 */       this.mainLock.wait();
/*     */     } catch (InterruptedException localInterruptedException) {
/* 351 */       System.err.println("AWT blocker activation interrupted:");
/* 352 */       localInterruptedException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   final void registerPeer(Object paramObject1, Object paramObject2) {
/* 357 */     synchronized (this.activationLock) {
/* 358 */       synchronized (this.mainLock) {
/* 359 */         this.peerMap.put(paramObject1, paramObject2);
/* 360 */         notifyPeerMapUpdated();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final void unregisterPeer(Object paramObject1, Object paramObject2) {
/* 366 */     synchronized (this.activationLock) {
/* 367 */       synchronized (this.mainLock) {
/* 368 */         if (this.peerMap.get(paramObject1) == paramObject2) {
/* 369 */           this.peerMap.remove(paramObject1);
/* 370 */           notifyPeerMapUpdated();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final Object getPeer(Object paramObject) {
/* 377 */     synchronized (this.activationLock) {
/* 378 */       synchronized (this.mainLock) {
/*     */       }
/* 380 */       throw localObject1;
/*     */     }
/*     */   }
/*     */ 
/*     */   final void dumpPeers(PlatformLogger paramPlatformLogger) {
/* 385 */     synchronized (this.activationLock)
/*     */     {
/*     */       Iterator localIterator;
/* 386 */       synchronized (this.mainLock) {
/* 387 */         paramPlatformLogger.fine("Mapped peers:");
/* 388 */         for (localIterator = this.peerMap.keySet().iterator(); localIterator.hasNext(); ) { Object localObject1 = localIterator.next();
/* 389 */           paramPlatformLogger.fine(localObject1 + "->" + this.peerMap.get(localObject1));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.AWTAutoShutdown
 * JD-Core Version:    0.6.2
 */