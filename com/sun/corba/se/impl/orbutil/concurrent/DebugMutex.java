/*     */ package com.sun.corba.se.impl.orbutil.concurrent;
/*     */ 
/*     */ import org.omg.CORBA.INTERNAL;
/*     */ 
/*     */ public class DebugMutex
/*     */   implements Sync
/*     */ {
/* 142 */   protected boolean inuse_ = false;
/* 143 */   protected Thread holder_ = null;
/*     */ 
/*     */   public void acquire() throws InterruptedException {
/* 146 */     if (Thread.interrupted()) throw new InterruptedException();
/* 147 */     synchronized (this) {
/* 148 */       Thread localThread = Thread.currentThread();
/* 149 */       if (this.holder_ == localThread) {
/* 150 */         throw new INTERNAL("Attempt to acquire Mutex by thread holding the Mutex");
/*     */       }
/*     */       try
/*     */       {
/* 154 */         while (this.inuse_) wait();
/* 155 */         this.inuse_ = true;
/* 156 */         this.holder_ = Thread.currentThread();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/* 159 */         notify();
/* 160 */         throw localInterruptedException;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void release() {
/* 166 */     Thread localThread = Thread.currentThread();
/* 167 */     if (localThread != this.holder_) {
/* 168 */       throw new INTERNAL("Attempt to release Mutex by thread not holding the Mutex");
/*     */     }
/* 170 */     this.holder_ = null;
/* 171 */     this.inuse_ = false;
/* 172 */     notify();
/*     */   }
/*     */ 
/*     */   public boolean attempt(long paramLong) throws InterruptedException
/*     */   {
/* 177 */     if (Thread.interrupted()) throw new InterruptedException();
/* 178 */     synchronized (this) {
/* 179 */       Thread localThread = Thread.currentThread();
/*     */ 
/* 181 */       if (!this.inuse_) {
/* 182 */         this.inuse_ = true;
/* 183 */         this.holder_ = localThread;
/* 184 */         return true;
/* 185 */       }if (paramLong <= 0L) {
/* 186 */         return false;
/*     */       }
/* 188 */       long l1 = paramLong;
/* 189 */       long l2 = System.currentTimeMillis();
/*     */       try {
/*     */         do {
/* 192 */           wait(l1);
/* 193 */           if (!this.inuse_) {
/* 194 */             this.inuse_ = true;
/* 195 */             this.holder_ = localThread;
/* 196 */             return true;
/*     */           }
/*     */ 
/* 199 */           l1 = paramLong - (System.currentTimeMillis() - l2);
/* 200 */         }while (l1 > 0L);
/* 201 */         return false;
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/* 206 */         notify();
/* 207 */         throw localInterruptedException;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.concurrent.DebugMutex
 * JD-Core Version:    0.6.2
 */