/*     */ package com.sun.corba.se.impl.orbutil.concurrent;
/*     */ 
/*     */ public class Mutex
/*     */   implements Sync
/*     */ {
/* 137 */   protected boolean inuse_ = false;
/*     */ 
/*     */   public void acquire() throws InterruptedException {
/* 140 */     if (Thread.interrupted()) throw new InterruptedException();
/* 141 */     synchronized (this) {
/*     */       try {
/* 143 */         while (this.inuse_) wait();
/* 144 */         this.inuse_ = true;
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/* 147 */         notify();
/* 148 */         throw localInterruptedException;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void release() {
/* 154 */     this.inuse_ = false;
/* 155 */     notify();
/*     */   }
/*     */ 
/*     */   public boolean attempt(long paramLong) throws InterruptedException
/*     */   {
/* 160 */     if (Thread.interrupted()) throw new InterruptedException();
/* 161 */     synchronized (this) {
/* 162 */       if (!this.inuse_) {
/* 163 */         this.inuse_ = true;
/* 164 */         return true;
/*     */       }
/* 166 */       if (paramLong <= 0L) {
/* 167 */         return false;
/*     */       }
/* 169 */       long l1 = paramLong;
/* 170 */       long l2 = System.currentTimeMillis();
/*     */       try {
/*     */         do {
/* 173 */           wait(l1);
/* 174 */           if (!this.inuse_) {
/* 175 */             this.inuse_ = true;
/* 176 */             return true;
/*     */           }
/*     */ 
/* 179 */           l1 = paramLong - (System.currentTimeMillis() - l2);
/* 180 */         }while (l1 > 0L);
/* 181 */         return false;
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/* 186 */         notify();
/* 187 */         throw localInterruptedException;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.concurrent.Mutex
 * JD-Core Version:    0.6.2
 */