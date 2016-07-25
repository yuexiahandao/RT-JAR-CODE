/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.pept.transport.EventHandler;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.NoSuchThreadPoolException;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.NoSuchWorkQueueException;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.Work;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.WorkQueue;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import org.omg.CORBA.INTERNAL;
/*     */ 
/*     */ public abstract class EventHandlerBase
/*     */   implements EventHandler
/*     */ {
/*     */   protected ORB orb;
/*     */   protected Work work;
/*     */   protected boolean useWorkerThreadForEvent;
/*     */   protected boolean useSelectThreadToWait;
/*     */   protected SelectionKey selectionKey;
/*     */ 
/*     */   public void setUseSelectThreadToWait(boolean paramBoolean)
/*     */   {
/*  60 */     this.useSelectThreadToWait = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean shouldUseSelectThreadToWait()
/*     */   {
/*  65 */     return this.useSelectThreadToWait;
/*     */   }
/*     */ 
/*     */   public void setSelectionKey(SelectionKey paramSelectionKey)
/*     */   {
/*  70 */     this.selectionKey = paramSelectionKey;
/*     */   }
/*     */ 
/*     */   public SelectionKey getSelectionKey()
/*     */   {
/*  75 */     return this.selectionKey;
/*     */   }
/*     */ 
/*     */   public void handleEvent()
/*     */   {
/*  86 */     if (this.orb.transportDebugFlag) {
/*  87 */       dprint(".handleEvent->: " + this);
/*     */     }
/*  89 */     getSelectionKey().interestOps(getSelectionKey().interestOps() & (getInterestOps() ^ 0xFFFFFFFF));
/*     */ 
/*  91 */     if (shouldUseWorkerThreadForEvent()) {
/*  92 */       Object localObject = null;
/*     */       try {
/*  94 */         if (this.orb.transportDebugFlag) {
/*  95 */           dprint(".handleEvent: addWork to pool: 0");
/*     */         }
/*  97 */         this.orb.getThreadPoolManager().getThreadPool(0).getWorkQueue(0).addWork(getWork());
/*     */       }
/*     */       catch (NoSuchThreadPoolException localNoSuchThreadPoolException) {
/* 100 */         localObject = localNoSuchThreadPoolException;
/*     */       } catch (NoSuchWorkQueueException localNoSuchWorkQueueException) {
/* 102 */         localObject = localNoSuchWorkQueueException;
/*     */       }
/*     */ 
/* 105 */       if (localObject != null) {
/* 106 */         if (this.orb.transportDebugFlag) {
/* 107 */           dprint(".handleEvent: " + localObject);
/*     */         }
/* 109 */         INTERNAL localINTERNAL = new INTERNAL("NoSuchThreadPoolException");
/* 110 */         localINTERNAL.initCause(localObject);
/* 111 */         throw localINTERNAL;
/*     */       }
/*     */     } else {
/* 114 */       if (this.orb.transportDebugFlag) {
/* 115 */         dprint(".handleEvent: doWork");
/*     */       }
/* 117 */       getWork().doWork();
/*     */     }
/* 119 */     if (this.orb.transportDebugFlag)
/* 120 */       dprint(".handleEvent<-: " + this);
/*     */   }
/*     */ 
/*     */   public boolean shouldUseWorkerThreadForEvent()
/*     */   {
/* 126 */     return this.useWorkerThreadForEvent;
/*     */   }
/*     */ 
/*     */   public void setUseWorkerThreadForEvent(boolean paramBoolean)
/*     */   {
/* 131 */     this.useWorkerThreadForEvent = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setWork(Work paramWork)
/*     */   {
/* 136 */     this.work = paramWork;
/*     */   }
/*     */ 
/*     */   public Work getWork()
/*     */   {
/* 141 */     return this.work;
/*     */   }
/*     */ 
/*     */   private void dprint(String paramString)
/*     */   {
/* 146 */     ORBUtility.dprint("EventHandlerBase", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.EventHandlerBase
 * JD-Core Version:    0.6.2
 */