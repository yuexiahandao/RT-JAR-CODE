/*     */ package com.sun.corba.se.impl.orbutil.concurrent;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ 
/*     */ public class CondVar
/*     */ {
/*     */   protected boolean debug_;
/*     */   protected final Sync mutex_;
/*     */   protected final ReentrantMutex remutex_;
/*     */ 
/*     */   private int releaseMutex()
/*     */   {
/* 167 */     int i = 1;
/*     */ 
/* 169 */     if (this.remutex_ != null)
/* 170 */       i = this.remutex_.releaseAll();
/*     */     else {
/* 172 */       this.mutex_.release();
/*     */     }
/* 174 */     return i;
/*     */   }
/*     */ 
/*     */   private void acquireMutex(int paramInt) throws InterruptedException
/*     */   {
/* 179 */     if (this.remutex_ != null)
/* 180 */       this.remutex_.acquireAll(paramInt);
/*     */     else
/* 182 */       this.mutex_.acquire();
/*     */   }
/*     */ 
/*     */   public CondVar(Sync paramSync, boolean paramBoolean)
/*     */   {
/* 202 */     this.debug_ = paramBoolean;
/* 203 */     this.mutex_ = paramSync;
/* 204 */     if ((paramSync instanceof ReentrantMutex))
/* 205 */       this.remutex_ = ((ReentrantMutex)paramSync);
/*     */     else
/* 207 */       this.remutex_ = null;
/*     */   }
/*     */ 
/*     */   public CondVar(Sync paramSync) {
/* 211 */     this(paramSync, false);
/*     */   }
/*     */ 
/*     */   public void await()
/*     */     throws InterruptedException
/*     */   {
/* 226 */     int i = 0;
/* 227 */     if (Thread.interrupted())
/* 228 */       throw new InterruptedException();
/*     */     try
/*     */     {
/* 231 */       if (this.debug_) {
/* 232 */         ORBUtility.dprintTrace(this, "await enter");
/*     */       }
/* 234 */       synchronized (this) {
/* 235 */         i = releaseMutex();
/*     */         try {
/* 237 */           wait();
/*     */         } catch (InterruptedException localInterruptedException1) {
/* 239 */           notify();
/* 240 */           throw localInterruptedException1;
/*     */         }
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/*     */       int j;
/* 245 */       int k = 0;
/*     */       while (true) {
/*     */         try {
/* 248 */           acquireMutex(i);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException3) {
/* 251 */           k = 1;
/*     */         }
/*     */       }
/*     */ 
/* 255 */       if (k != 0) {
/* 256 */         Thread.currentThread().interrupt();
/*     */       }
/*     */ 
/* 259 */       if (this.debug_)
/* 260 */         ORBUtility.dprintTrace(this, "await exit");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean timedwait(long paramLong)
/*     */     throws InterruptedException
/*     */   {
/* 283 */     if (Thread.interrupted()) {
/* 284 */       throw new InterruptedException();
/*     */     }
/* 286 */     boolean bool = false;
/* 287 */     int i = 0;
/*     */     try
/*     */     {
/* 290 */       if (this.debug_) {
/* 291 */         ORBUtility.dprintTrace(this, "timedwait enter");
/*     */       }
/* 293 */       synchronized (this) {
/* 294 */         i = releaseMutex();
/*     */         try {
/* 296 */           if (paramLong > 0L) {
/* 297 */             long l = System.currentTimeMillis();
/* 298 */             wait(paramLong);
/* 299 */             bool = System.currentTimeMillis() - l <= paramLong;
/*     */           }
/*     */         } catch (InterruptedException localInterruptedException1) {
/* 302 */           notify();
/* 303 */           throw localInterruptedException1;
/*     */         }
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/*     */       int j;
/* 308 */       int k = 0;
/*     */       while (true) {
/*     */         try {
/* 311 */           acquireMutex(i);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException3) {
/* 314 */           k = 1;
/*     */         }
/*     */       }
/*     */ 
/* 318 */       if (k != 0) {
/* 319 */         Thread.currentThread().interrupt();
/*     */       }
/*     */ 
/* 322 */       if (this.debug_)
/* 323 */         ORBUtility.dprintTrace(this, "timedwait exit");
/*     */     }
/* 325 */     return bool;
/*     */   }
/*     */ 
/*     */   public synchronized void signal()
/*     */   {
/* 334 */     notify();
/*     */   }
/*     */ 
/*     */   public synchronized void broadcast()
/*     */   {
/* 339 */     notifyAll();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.concurrent.CondVar
 * JD-Core Version:    0.6.2
 */