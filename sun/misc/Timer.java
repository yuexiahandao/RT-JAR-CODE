/*     */ package sun.misc;
/*     */ 
/*     */ public class Timer
/*     */ {
/*     */   public Timeable owner;
/*     */   long interval;
/*     */   long sleepUntil;
/*     */   long remainingTime;
/*     */   boolean regular;
/*     */   boolean stopped;
/*     */   Timer next;
/* 148 */   static TimerThread timerThread = null;
/*     */ 
/*     */   public Timer(Timeable paramTimeable, long paramLong)
/*     */   {
/* 160 */     this.owner = paramTimeable;
/* 161 */     this.interval = paramLong;
/* 162 */     this.remainingTime = paramLong;
/* 163 */     this.regular = true;
/* 164 */     this.sleepUntil = System.currentTimeMillis();
/* 165 */     this.stopped = true;
/* 166 */     synchronized (getClass()) {
/* 167 */       if (timerThread == null)
/* 168 */         timerThread = new TimerThread();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean isStopped()
/*     */   {
/* 177 */     return this.stopped;
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */   {
/* 191 */     long l = System.currentTimeMillis();
/*     */ 
/* 193 */     synchronized (timerThread) {
/* 194 */       synchronized (this) {
/* 195 */         if (!this.stopped) {
/* 196 */           TimerThread.dequeue(this);
/* 197 */           this.remainingTime = Math.max(0L, this.sleepUntil - l);
/* 198 */           this.sleepUntil = l;
/* 199 */           this.stopped = true;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void cont()
/*     */   {
/* 214 */     synchronized (timerThread) {
/* 215 */       synchronized (this) {
/* 216 */         if (this.stopped)
/*     */         {
/* 223 */           this.sleepUntil = Math.max(this.sleepUntil + 1L, System.currentTimeMillis() + this.remainingTime);
/*     */ 
/* 225 */           TimerThread.enqueue(this);
/* 226 */           this.stopped = false;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 237 */     synchronized (timerThread) {
/* 238 */       synchronized (this) {
/* 239 */         setRemainingTime(this.interval);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized long getStopTime()
/*     */   {
/* 249 */     return this.sleepUntil;
/*     */   }
/*     */ 
/*     */   public synchronized long getInterval()
/*     */   {
/* 256 */     return this.interval;
/*     */   }
/*     */ 
/*     */   public synchronized void setInterval(long paramLong)
/*     */   {
/* 267 */     this.interval = paramLong;
/*     */   }
/*     */ 
/*     */   public synchronized long getRemainingTime()
/*     */   {
/* 275 */     return this.remainingTime;
/*     */   }
/*     */ 
/*     */   public void setRemainingTime(long paramLong)
/*     */   {
/* 287 */     synchronized (timerThread) {
/* 288 */       synchronized (this) {
/* 289 */         if (this.stopped) {
/* 290 */           this.remainingTime = paramLong;
/*     */         } else {
/* 292 */           stop();
/* 293 */           this.remainingTime = paramLong;
/* 294 */           cont();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void setRegular(boolean paramBoolean)
/*     */   {
/* 312 */     this.regular = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected Thread getTimerThread()
/*     */   {
/* 319 */     return TimerThread.timerThread;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Timer
 * JD-Core Version:    0.6.2
 */