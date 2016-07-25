/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.pept.transport.Acceptor;
/*     */ import com.sun.corba.se.pept.transport.Connection;
/*     */ import com.sun.corba.se.pept.transport.EventHandler;
/*     */ import com.sun.corba.se.pept.transport.ListenerThread;
/*     */ import com.sun.corba.se.pept.transport.ReaderThread;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.NoSuchThreadPoolException;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.NoSuchWorkQueueException;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.Work;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.WorkQueue;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ class SelectorImpl extends Thread
/*     */   implements com.sun.corba.se.pept.transport.Selector
/*     */ {
/*     */   private ORB orb;
/*     */   private java.nio.channels.Selector selector;
/*     */   private long timeout;
/*     */   private List deferredRegistrations;
/*     */   private List interestOpsList;
/*     */   private HashMap listenerThreads;
/*     */   private Map readerThreads;
/*     */   private boolean selectorStarted;
/*     */   private volatile boolean closed;
/*     */   private ORBUtilSystemException wrapper;
/*     */ 
/*     */   public SelectorImpl(ORB paramORB)
/*     */   {
/*  78 */     this.orb = paramORB;
/*  79 */     this.selector = null;
/*  80 */     this.selectorStarted = false;
/*  81 */     this.timeout = 60000L;
/*  82 */     this.deferredRegistrations = new ArrayList();
/*  83 */     this.interestOpsList = new ArrayList();
/*  84 */     this.listenerThreads = new HashMap();
/*  85 */     this.readerThreads = Collections.synchronizedMap(new HashMap());
/*  86 */     this.closed = false;
/*  87 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.transport");
/*     */   }
/*     */ 
/*     */   public void setTimeout(long paramLong)
/*     */   {
/*  92 */     this.timeout = paramLong;
/*     */   }
/*     */ 
/*     */   public long getTimeout()
/*     */   {
/*  97 */     return this.timeout;
/*     */   }
/*     */ 
/*     */   public void registerInterestOps(EventHandler paramEventHandler)
/*     */   {
/* 102 */     if (this.orb.transportDebugFlag) {
/* 103 */       dprint(".registerInterestOps:-> " + paramEventHandler);
/*     */     }
/*     */ 
/* 106 */     SelectionKey localSelectionKey = paramEventHandler.getSelectionKey();
/* 107 */     if (localSelectionKey.isValid()) {
/* 108 */       int i = paramEventHandler.getInterestOps();
/* 109 */       SelectionKeyAndOp localSelectionKeyAndOp = new SelectionKeyAndOp(localSelectionKey, i);
/* 110 */       synchronized (this.interestOpsList) {
/* 111 */         this.interestOpsList.add(localSelectionKeyAndOp);
/*     */       }
/*     */ 
/* 114 */       this.selector.wakeup();
/*     */     }
/*     */     else {
/* 117 */       this.wrapper.selectionKeyInvalid(paramEventHandler.toString());
/* 118 */       if (this.orb.transportDebugFlag) {
/* 119 */         dprint(".registerInterestOps: EventHandler SelectionKey not valid " + paramEventHandler);
/*     */       }
/*     */     }
/*     */ 
/* 123 */     if (this.orb.transportDebugFlag)
/* 124 */       dprint(".registerInterestOps:<- ");
/*     */   }
/*     */ 
/*     */   public void registerForEvent(EventHandler paramEventHandler)
/*     */   {
/* 130 */     if (this.orb.transportDebugFlag) {
/* 131 */       dprint(".registerForEvent: " + paramEventHandler);
/*     */     }
/*     */ 
/* 134 */     if (isClosed()) {
/* 135 */       if (this.orb.transportDebugFlag) {
/* 136 */         dprint(".registerForEvent: closed: " + paramEventHandler);
/*     */       }
/* 138 */       return;
/*     */     }
/*     */ 
/* 141 */     if (paramEventHandler.shouldUseSelectThreadToWait()) {
/* 142 */       synchronized (this.deferredRegistrations) {
/* 143 */         this.deferredRegistrations.add(paramEventHandler);
/*     */       }
/* 145 */       if (!this.selectorStarted) {
/* 146 */         startSelector();
/*     */       }
/* 148 */       this.selector.wakeup();
/* 149 */       return;
/*     */     }
/*     */ 
/* 152 */     switch (paramEventHandler.getInterestOps()) {
/*     */     case 16:
/* 154 */       createListenerThread(paramEventHandler);
/* 155 */       break;
/*     */     case 1:
/* 157 */       createReaderThread(paramEventHandler);
/* 158 */       break;
/*     */     default:
/* 160 */       if (this.orb.transportDebugFlag) {
/* 161 */         dprint(".registerForEvent: default: " + paramEventHandler);
/*     */       }
/* 163 */       throw new RuntimeException("SelectorImpl.registerForEvent: unknown interest ops");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unregisterForEvent(EventHandler paramEventHandler)
/*     */   {
/* 170 */     if (this.orb.transportDebugFlag) {
/* 171 */       dprint(".unregisterForEvent: " + paramEventHandler);
/*     */     }
/*     */ 
/* 174 */     if (isClosed()) {
/* 175 */       if (this.orb.transportDebugFlag) {
/* 176 */         dprint(".unregisterForEvent: closed: " + paramEventHandler);
/*     */       }
/* 178 */       return;
/*     */     }
/*     */ 
/* 181 */     if (paramEventHandler.shouldUseSelectThreadToWait())
/*     */     {
/*     */       SelectionKey localSelectionKey;
/* 183 */       synchronized (this.deferredRegistrations) {
/* 184 */         localSelectionKey = paramEventHandler.getSelectionKey();
/*     */       }
/* 186 */       if (localSelectionKey != null) {
/* 187 */         localSelectionKey.cancel();
/*     */       }
/* 189 */       this.selector.wakeup();
/* 190 */       return;
/*     */     }
/*     */ 
/* 193 */     switch (paramEventHandler.getInterestOps()) {
/*     */     case 16:
/* 195 */       destroyListenerThread(paramEventHandler);
/* 196 */       break;
/*     */     case 1:
/* 198 */       destroyReaderThread(paramEventHandler);
/* 199 */       break;
/*     */     default:
/* 201 */       if (this.orb.transportDebugFlag) {
/* 202 */         dprint(".unregisterForEvent: default: " + paramEventHandler);
/*     */       }
/* 204 */       throw new RuntimeException("SelectorImpl.uregisterForEvent: unknown interest ops");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 211 */     if (this.orb.transportDebugFlag) {
/* 212 */       dprint(".close");
/*     */     }
/*     */ 
/* 215 */     if (isClosed()) {
/* 216 */       if (this.orb.transportDebugFlag) {
/* 217 */         dprint(".close: already closed");
/*     */       }
/* 219 */       return;
/*     */     }
/*     */ 
/* 222 */     setClosed(true);
/*     */ 
/* 228 */     Iterator localIterator = this.listenerThreads.values().iterator();
/*     */     Object localObject;
/* 229 */     while (localIterator.hasNext()) {
/* 230 */       localObject = (ListenerThread)localIterator.next();
/* 231 */       ((ListenerThread)localObject).close();
/*     */     }
/*     */ 
/* 236 */     localIterator = this.readerThreads.values().iterator();
/* 237 */     while (localIterator.hasNext()) {
/* 238 */       localObject = (ReaderThread)localIterator.next();
/* 239 */       ((ReaderThread)localObject).close();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 245 */       if (this.selector != null)
/*     */       {
/* 247 */         this.selector.wakeup();
/*     */       }
/*     */     } catch (Throwable localThrowable) {
/* 250 */       if (this.orb.transportDebugFlag)
/* 251 */         dprint(".close: selector.close: " + localThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 263 */     setName("SelectorThread");
/* 264 */     while (!this.closed)
/*     */       try {
/* 266 */         int i = 0;
/* 267 */         if ((this.timeout == 0L) && (this.orb.transportDebugFlag)) {
/* 268 */           dprint(".run: Beginning of selection cycle");
/*     */         }
/* 270 */         handleDeferredRegistrations();
/* 271 */         enableInterestOps();
/*     */         try {
/* 273 */           i = this.selector.select(this.timeout);
/*     */         } catch (IOException localIOException) {
/* 275 */           if (this.orb.transportDebugFlag) {
/* 276 */             dprint(".run: selector.select: " + localIOException);
/*     */           }
/*     */         }
/* 279 */         if (this.closed) {
/* 280 */           this.selector.close();
/* 281 */           if (this.orb.transportDebugFlag) {
/* 282 */             dprint(".run: closed - .run return");
/*     */           }
/* 284 */           return;
/*     */         }
/*     */ 
/* 294 */         Iterator localIterator = this.selector.selectedKeys().iterator();
/* 295 */         if ((this.orb.transportDebugFlag) && 
/* 296 */           (localIterator.hasNext())) {
/* 297 */           dprint(".run: n = " + i);
/*     */         }
/*     */ 
/* 300 */         while (localIterator.hasNext()) {
/* 301 */           SelectionKey localSelectionKey = (SelectionKey)localIterator.next();
/* 302 */           localIterator.remove();
/* 303 */           EventHandler localEventHandler = (EventHandler)localSelectionKey.attachment();
/*     */           try
/*     */           {
/* 306 */             localEventHandler.handleEvent();
/*     */           } catch (Throwable localThrowable2) {
/* 308 */             if (this.orb.transportDebugFlag) {
/* 309 */               dprint(".run: eventHandler.handleEvent", localThrowable2);
/*     */             }
/*     */           }
/*     */         }
/* 313 */         if ((this.timeout == 0L) && (this.orb.transportDebugFlag)) {
/* 314 */           dprint(".run: End of selection cycle");
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 319 */         if (this.orb.transportDebugFlag)
/* 320 */           dprint(".run: ignoring", localThrowable1);
/*     */       }
/*     */   }
/*     */ 
/*     */   private synchronized boolean isClosed()
/*     */   {
/* 333 */     return this.closed;
/*     */   }
/*     */ 
/*     */   private synchronized void setClosed(boolean paramBoolean)
/*     */   {
/* 338 */     this.closed = paramBoolean;
/*     */   }
/*     */ 
/*     */   private void startSelector()
/*     */   {
/*     */     try {
/* 344 */       this.selector = java.nio.channels.Selector.open();
/*     */     } catch (IOException localIOException) {
/* 346 */       if (this.orb.transportDebugFlag) {
/* 347 */         dprint(".startSelector: Selector.open: IOException: " + localIOException);
/*     */       }
/*     */ 
/* 350 */       RuntimeException localRuntimeException = new RuntimeException(".startSelector: Selector.open exception");
/*     */ 
/* 352 */       localRuntimeException.initCause(localIOException);
/* 353 */       throw localRuntimeException;
/*     */     }
/* 355 */     setDaemon(true);
/* 356 */     start();
/* 357 */     this.selectorStarted = true;
/* 358 */     if (this.orb.transportDebugFlag)
/* 359 */       dprint(".startSelector: selector.start completed.");
/*     */   }
/*     */ 
/*     */   private void handleDeferredRegistrations()
/*     */   {
/* 365 */     synchronized (this.deferredRegistrations) {
/* 366 */       int i = this.deferredRegistrations.size();
/* 367 */       for (int j = 0; j < i; j++) {
/* 368 */         EventHandler localEventHandler = (EventHandler)this.deferredRegistrations.get(j);
/*     */ 
/* 370 */         if (this.orb.transportDebugFlag) {
/* 371 */           dprint(".handleDeferredRegistrations: " + localEventHandler);
/*     */         }
/* 373 */         SelectableChannel localSelectableChannel = localEventHandler.getChannel();
/* 374 */         SelectionKey localSelectionKey = null;
/*     */         try {
/* 376 */           localSelectionKey = localSelectableChannel.register(this.selector, localEventHandler.getInterestOps(), localEventHandler);
/*     */         }
/*     */         catch (ClosedChannelException localClosedChannelException)
/*     */         {
/* 381 */           if (this.orb.transportDebugFlag) {
/* 382 */             dprint(".handleDeferredRegistrations: " + localClosedChannelException);
/*     */           }
/*     */         }
/* 385 */         localEventHandler.setSelectionKey(localSelectionKey);
/*     */       }
/* 387 */       this.deferredRegistrations.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void enableInterestOps()
/*     */   {
/* 393 */     synchronized (this.interestOpsList) {
/* 394 */       int i = this.interestOpsList.size();
/* 395 */       if (i > 0) {
/* 396 */         if (this.orb.transportDebugFlag) {
/* 397 */           dprint(".enableInterestOps:->");
/*     */         }
/* 399 */         SelectionKey localSelectionKey = null;
/* 400 */         SelectionKeyAndOp localSelectionKeyAndOp = null;
/* 401 */         int k = 0;
/* 402 */         for (int m = 0; m < i; m++) {
/* 403 */           localSelectionKeyAndOp = (SelectionKeyAndOp)this.interestOpsList.get(m);
/* 404 */           localSelectionKey = localSelectionKeyAndOp.selectionKey;
/*     */ 
/* 416 */           if (localSelectionKey.isValid()) {
/* 417 */             if (this.orb.transportDebugFlag) {
/* 418 */               dprint(".enableInterestOps: " + localSelectionKeyAndOp);
/*     */             }
/* 420 */             int j = localSelectionKeyAndOp.keyOp;
/* 421 */             k = localSelectionKey.interestOps();
/* 422 */             localSelectionKey.interestOps(k | j);
/*     */           }
/*     */         }
/* 425 */         this.interestOpsList.clear();
/* 426 */         if (this.orb.transportDebugFlag)
/* 427 */           dprint(".enableInterestOps:<-");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void createListenerThread(EventHandler paramEventHandler)
/*     */   {
/* 435 */     if (this.orb.transportDebugFlag) {
/* 436 */       dprint(".createListenerThread: " + paramEventHandler);
/*     */     }
/* 438 */     Acceptor localAcceptor = paramEventHandler.getAcceptor();
/* 439 */     ListenerThreadImpl localListenerThreadImpl = new ListenerThreadImpl(this.orb, localAcceptor, this);
/*     */ 
/* 441 */     this.listenerThreads.put(paramEventHandler, localListenerThreadImpl);
/* 442 */     Object localObject = null;
/*     */     try {
/* 444 */       this.orb.getThreadPoolManager().getThreadPool(0).getWorkQueue(0).addWork((Work)localListenerThreadImpl);
/*     */     }
/*     */     catch (NoSuchThreadPoolException localNoSuchThreadPoolException) {
/* 447 */       localObject = localNoSuchThreadPoolException;
/*     */     } catch (NoSuchWorkQueueException localNoSuchWorkQueueException) {
/* 449 */       localObject = localNoSuchWorkQueueException;
/*     */     }
/* 451 */     if (localObject != null) {
/* 452 */       RuntimeException localRuntimeException = new RuntimeException(localObject.toString());
/* 453 */       localRuntimeException.initCause(localObject);
/* 454 */       throw localRuntimeException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void destroyListenerThread(EventHandler paramEventHandler)
/*     */   {
/* 460 */     if (this.orb.transportDebugFlag) {
/* 461 */       dprint(".destroyListenerThread: " + paramEventHandler);
/*     */     }
/* 463 */     ListenerThread localListenerThread = (ListenerThread)this.listenerThreads.get(paramEventHandler);
/*     */ 
/* 465 */     if (localListenerThread == null) {
/* 466 */       if (this.orb.transportDebugFlag) {
/* 467 */         dprint(".destroyListenerThread: cannot find ListenerThread - ignoring.");
/*     */       }
/* 469 */       return;
/*     */     }
/* 471 */     this.listenerThreads.remove(paramEventHandler);
/* 472 */     localListenerThread.close();
/*     */   }
/*     */ 
/*     */   private void createReaderThread(EventHandler paramEventHandler)
/*     */   {
/* 477 */     if (this.orb.transportDebugFlag) {
/* 478 */       dprint(".createReaderThread: " + paramEventHandler);
/*     */     }
/* 480 */     Connection localConnection = paramEventHandler.getConnection();
/* 481 */     ReaderThreadImpl localReaderThreadImpl = new ReaderThreadImpl(this.orb, localConnection, this);
/*     */ 
/* 483 */     this.readerThreads.put(paramEventHandler, localReaderThreadImpl);
/* 484 */     Object localObject = null;
/*     */     try {
/* 486 */       this.orb.getThreadPoolManager().getThreadPool(0).getWorkQueue(0).addWork((Work)localReaderThreadImpl);
/*     */     }
/*     */     catch (NoSuchThreadPoolException localNoSuchThreadPoolException) {
/* 489 */       localObject = localNoSuchThreadPoolException;
/*     */     } catch (NoSuchWorkQueueException localNoSuchWorkQueueException) {
/* 491 */       localObject = localNoSuchWorkQueueException;
/*     */     }
/* 493 */     if (localObject != null) {
/* 494 */       RuntimeException localRuntimeException = new RuntimeException(localObject.toString());
/* 495 */       localRuntimeException.initCause(localObject);
/* 496 */       throw localRuntimeException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void destroyReaderThread(EventHandler paramEventHandler)
/*     */   {
/* 502 */     if (this.orb.transportDebugFlag) {
/* 503 */       dprint(".destroyReaderThread: " + paramEventHandler);
/*     */     }
/* 505 */     ReaderThread localReaderThread = (ReaderThread)this.readerThreads.get(paramEventHandler);
/*     */ 
/* 507 */     if (localReaderThread == null) {
/* 508 */       if (this.orb.transportDebugFlag) {
/* 509 */         dprint(".destroyReaderThread: cannot find ReaderThread - ignoring.");
/*     */       }
/* 511 */       return;
/*     */     }
/* 513 */     this.readerThreads.remove(paramEventHandler);
/* 514 */     localReaderThread.close();
/*     */   }
/*     */ 
/*     */   private void dprint(String paramString)
/*     */   {
/* 519 */     ORBUtility.dprint("SelectorImpl", paramString);
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString, Throwable paramThrowable)
/*     */   {
/* 524 */     dprint(paramString);
/* 525 */     paramThrowable.printStackTrace(System.out);
/*     */   }
/*     */ 
/*     */   private class SelectionKeyAndOp
/*     */   {
/*     */     public int keyOp;
/*     */     public SelectionKey selectionKey;
/*     */ 
/*     */     public SelectionKeyAndOp(SelectionKey paramInt, int arg3)
/*     */     {
/* 541 */       this.selectionKey = paramInt;
/*     */       int i;
/* 542 */       this.keyOp = i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.SelectorImpl
 * JD-Core Version:    0.6.2
 */