/*     */ package com.sun.corba.se.impl.orbutil.concurrent;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import org.omg.CORBA.INTERNAL;
/*     */ 
/*     */ public class ReentrantMutex
/*     */   implements Sync
/*     */ {
/* 146 */   protected Thread holder_ = null;
/*     */ 
/* 149 */   protected int counter_ = 0;
/*     */ 
/* 151 */   protected boolean debug = false;
/*     */ 
/*     */   public ReentrantMutex()
/*     */   {
/* 155 */     this(false);
/*     */   }
/*     */ 
/*     */   public ReentrantMutex(boolean paramBoolean)
/*     */   {
/* 160 */     this.debug = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void acquire() throws InterruptedException {
/* 164 */     if (Thread.interrupted()) {
/* 165 */       throw new InterruptedException();
/*     */     }
/* 167 */     synchronized (this) {
/*     */       try {
/* 169 */         if (this.debug) {
/* 170 */           ORBUtility.dprintTrace(this, "acquire enter: holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
/*     */         }
/*     */ 
/* 175 */         Thread localThread = Thread.currentThread();
/* 176 */         if (this.holder_ != localThread) {
/*     */           try {
/* 178 */             while (this.counter_ > 0) {
/* 179 */               wait();
/*     */             }
/*     */ 
/* 182 */             if (this.counter_ != 0) {
/* 183 */               throw new INTERNAL("counter not 0 when first acquiring mutex");
/*     */             }
/*     */ 
/* 186 */             this.holder_ = localThread;
/*     */           } catch (InterruptedException localInterruptedException) {
/* 188 */             notify();
/* 189 */             throw localInterruptedException;
/*     */           }
/*     */         }
/*     */ 
/* 193 */         this.counter_ += 1;
/*     */       } finally {
/* 195 */         if (this.debug)
/* 196 */           ORBUtility.dprintTrace(this, "acquire exit: holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void acquireAll(int paramInt)
/*     */     throws InterruptedException
/*     */   {
/* 205 */     if (Thread.interrupted()) {
/* 206 */       throw new InterruptedException();
/*     */     }
/* 208 */     synchronized (this) {
/*     */       try {
/* 210 */         if (this.debug) {
/* 211 */           ORBUtility.dprintTrace(this, "acquireAll enter: count=" + paramInt + " holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
/*     */         }
/*     */ 
/* 215 */         Thread localThread = Thread.currentThread();
/* 216 */         if (this.holder_ == localThread) {
/* 217 */           throw new INTERNAL("Cannot acquireAll while holding the mutex");
/*     */         }
/*     */         try
/*     */         {
/* 221 */           while (this.counter_ > 0) {
/* 222 */             wait();
/*     */           }
/*     */ 
/* 225 */           if (this.counter_ != 0) {
/* 226 */             throw new INTERNAL("counter not 0 when first acquiring mutex");
/*     */           }
/*     */ 
/* 229 */           this.holder_ = localThread;
/*     */         } catch (InterruptedException localInterruptedException) {
/* 231 */           notify();
/* 232 */           throw localInterruptedException;
/*     */         }
/*     */ 
/* 236 */         this.counter_ = paramInt;
/*     */       } finally {
/* 238 */         if (this.debug)
/* 239 */           ORBUtility.dprintTrace(this, "acquireAll exit: count=" + paramInt + " holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void release()
/*     */   {
/*     */     try
/*     */     {
/* 249 */       if (this.debug) {
/* 250 */         ORBUtility.dprintTrace(this, "release enter:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
/*     */       }
/*     */ 
/* 254 */       Thread localThread = Thread.currentThread();
/* 255 */       if (localThread != this.holder_) {
/* 256 */         throw new INTERNAL("Attempt to release Mutex by thread not holding the Mutex");
/*     */       }
/*     */ 
/* 259 */       this.counter_ -= 1;
/*     */ 
/* 261 */       if (this.counter_ == 0) {
/* 262 */         this.holder_ = null;
/* 263 */         notify();
/*     */       }
/*     */     } finally {
/* 266 */       if (this.debug)
/* 267 */         ORBUtility.dprintTrace(this, "release exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized int releaseAll()
/*     */   {
/*     */     try
/*     */     {
/* 276 */       if (this.debug) {
/* 277 */         ORBUtility.dprintTrace(this, "releaseAll enter:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
/*     */       }
/*     */ 
/* 281 */       Thread localThread = Thread.currentThread();
/* 282 */       if (localThread != this.holder_) {
/* 283 */         throw new INTERNAL("Attempt to releaseAll Mutex by thread not holding the Mutex");
/*     */       }
/*     */ 
/* 286 */       int i = this.counter_;
/* 287 */       this.counter_ = 0;
/* 288 */       this.holder_ = null;
/* 289 */       notify();
/* 290 */       return i;
/*     */     } finally {
/* 292 */       if (this.debug)
/* 293 */         ORBUtility.dprintTrace(this, "releaseAll exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean attempt(long paramLong)
/*     */     throws InterruptedException
/*     */   {
/* 300 */     if (Thread.interrupted()) {
/* 301 */       throw new InterruptedException();
/*     */     }
/* 303 */     synchronized (this) {
/*     */       try {
/* 305 */         if (this.debug) {
/* 306 */           ORBUtility.dprintTrace(this, "attempt enter: msecs=" + paramLong + " holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
/*     */         }
/*     */ 
/* 311 */         Thread localThread = Thread.currentThread();
/*     */         boolean bool1;
/* 313 */         if (this.counter_ == 0) {
/* 314 */           this.holder_ = localThread;
/* 315 */           this.counter_ = 1;
/* 316 */           bool1 = true;
/*     */ 
/* 343 */           if (this.debug)
/* 344 */             ORBUtility.dprintTrace(this, "attempt exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_); return bool1;
/*     */         }
/* 317 */         if (paramLong <= 0L) {
/* 318 */           bool1 = false;
/*     */ 
/* 343 */           if (this.debug)
/* 344 */             ORBUtility.dprintTrace(this, "attempt exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_); return bool1;
/*     */         }
/* 320 */         long l1 = paramLong;
/* 321 */         long l2 = System.currentTimeMillis();
/*     */         try {
/*     */           do {
/* 324 */             wait(l1);
/* 325 */             if (this.counter_ == 0) {
/* 326 */               this.holder_ = localThread;
/* 327 */               this.counter_ = 1;
/* 328 */               bool2 = true;
/*     */ 
/* 343 */               if (this.debug)
/* 344 */                 ORBUtility.dprintTrace(this, "attempt exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_); return bool2;
/*     */             }
/* 330 */             l1 = paramLong - (System.currentTimeMillis() - l2);
/*     */           }
/*     */ 
/* 333 */           while (l1 > 0L);
/* 334 */           boolean bool2 = false;
/*     */ 
/* 343 */           if (this.debug)
/* 344 */             ORBUtility.dprintTrace(this, "attempt exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_); return bool2;
/*     */         }
/*     */         catch (InterruptedException localInterruptedException)
/*     */         {
/* 338 */           notify();
/* 339 */           throw localInterruptedException;
/*     */         }
/*     */       }
/*     */       finally {
/* 343 */         if (this.debug)
/* 344 */           ORBUtility.dprintTrace(this, "attempt exit:  holder_=" + ORBUtility.getThreadName(this.holder_) + " counter_=" + this.counter_);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.concurrent.ReentrantMutex
 * JD-Core Version:    0.6.2
 */